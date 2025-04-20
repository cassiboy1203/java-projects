import argparse

import git_utils
import release_notes
import version_utils


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("project")
    parser.add_argument("-e", "--env", default="main", choices=version_utils.envs.keys())
    args = parser.parse_args()

    project = args.project
    env = args.env

    current_version = version_utils.get_current_version(project, env)
    commits = git_utils.get_commits(project)
    new_version = version_utils.get_version(current_version, commits, project)

    if new_version == current_version or new_version is None:
        exit(1)
    else:
        version_utils.write_new_version(new_version, project)

        notes = release_notes.generate_release_notes(commits, new_version)
        release_notes.save_release_notes(notes)

        print(new_version)


if __name__ == "__main__":
    main()
