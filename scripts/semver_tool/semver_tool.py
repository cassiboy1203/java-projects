import argparse
import multiprocessing
from collections import deque
from functools import partial

import git_utils
import release_notes
import version_utils
from classes import Project, Version


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("projects", nargs='+')
    parser.add_argument("-e", "--env", default="main", choices=version_utils.envs.keys())
    parser.add_argument("-o", "--override")
    parser.add_argument("-d", "--debug", action="store_true")
    args = parser.parse_args()

    projects = args.projects
    env = args.env
    override = args.override
    debug = args.debug

    ctx = multiprocessing.get_context("spawn")
    with ctx.Pool() as p:
        func = partial(determine_version_off_project, env=env, override=override, debug=debug)
        versions = p.map(func, projects)

    dependency_graph = version_utils.get_projects()

    v_queue = deque()

    cleaned_versions = []

    for version in versions:
        if version.version != "":
            cleaned_versions.append(version)
            v_queue.append(version)

    while v_queue:
        version = v_queue.popleft()
        dependant_projects = dependency_graph[version.project]

        if "*" in dependant_projects:
            dependant_projects = dependency_graph.keys()

        for dependant in dependant_projects:
            if dependant == version.project:
                continue
            if not any(v.project == dependant for v in cleaned_versions):
                cv = version_utils.get_current_version(dependant, env)
                cv.bump_build()
                print(dependant, cv, sep=":")
                project = Project(dependant, str(cv))
                cleaned_versions.append(project)
                v_queue.append(project)

    if not debug:
        version_utils.write_new_version(cleaned_versions, env)


def determine_version_off_project(project: str, env="dev", override="", debug=False):
    current_version = version_utils.get_current_version(project, env)
    main_version = version_utils.get_current_version(project, "main")
    commits = git_utils.get_commits(project, env)

    if override is None:
        new_version = version_utils.get_version(current_version, main_version, commits, project)
    else:
        new_version = Version.from_string(override)

    if new_version == current_version or new_version is None:
        return Project(project)

    if not debug:
        notes = release_notes.generate_release_notes(commits, new_version, project)
        current_notes = release_notes.read_release_notes(project)
        main_notes = f"{notes}\n\n{current_notes}"
        release_notes.save_release_notes(notes, f"rntmp/{project}_release_notes.md")
        if env == "main":
            release_notes.save_release_notes(main_notes, f"release_notes/{project}.md")

    print(project, new_version, sep=":")

    return Project(project, str(new_version))


if __name__ == "__main__":
    main()
