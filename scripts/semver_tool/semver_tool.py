import argparse

import git_utils
import release_notes
import version_utils


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("project")
    parser.add_argument("-e", "--env", default="main", choices=version_utils.envs.keys())
    parser.add_argument("-o", "--override")
    args = parser.parse_args()

    project = args.project
    env = args.env
    version_override = args.override

    current_version = version_utils.get_current_version(project, env)
    main_version = version_utils.get_current_version(project, "main")
    commits = git_utils.get_commits(project)

    if version_override is None:
        new_version = version_utils.get_version(current_version, main_version, commits, project)
    else:
        new_version = version_utils.Version.from_string(version_override)

    if new_version == current_version or new_version is None:
        exit(1)
    else:
        version_utils.write_new_version(new_version, project)

        notes = release_notes.generate_release_notes(commits, new_version, project)
        current_notes = release_notes.read_release_notes(project)
        main_notes = f"{notes}\n\n{current_notes}"
        release_notes.save_release_notes(notes)
        if env == "main":
            release_notes.save_release_notes(main_notes, f"release_notes/{project}.md")

        print(new_version)


if __name__ == "__main__":
    main()
