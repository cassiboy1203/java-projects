import copy

import yaml

from classes import Project, Version

envs = {
    "main": "",
    "rc": "RC",
    "dev": "DEV",
}

types = [
    "fix",
    "feat",
    "build",
    "ci",
    "docs",
    "perf",
    "refactor",
    "test"
]


def get_version(version: Version, main_version: Version, commits, scope):
    major, minor, patch, build = 0, 0, 0, 0

    for message in commits:
        message_scope = get_scope(message)

        if message_scope:
            scopes = message_scope.split(",")
        else:
            scopes = []

        if len(scopes) > 1 and not scopes.__contains__(scope):
            continue

        bump = determine_bump(message)

        if bump == "major":
            major += 1
        elif bump == "minor":
            minor += 1
        elif bump == "patch":
            patch += 1
        elif bump == "build":
            build += 1

    new_version = copy.deepcopy(version)
    new_version.main = main_version
    if major:
        return new_version.bump_major()
    elif minor:
        return new_version.bump_minor()
    elif patch:
        return new_version.bump_patch()
    elif build:
        return new_version.bump_build()
    return new_version


def determine_bump(message):
    if "BREAKING CHANGE" in message:
        return "major"
    elif message.startswith("feat"):
        return "minor"
    elif message.startswith("fix"):
        return "patch"
    elif message.startswith("build"):
        return "build"
    return None


def get_scope(message):
    for t in types:
        if message.startswith(t):
            return message[len(t) + 1:message.find(":") - 1]
    return None


def get_current_version(project, env):
    try:
        with open("version.yaml", "r") as stream:
            version_yaml = yaml.safe_load(stream)

            if version_yaml is None:
                return Version(env=env)

            try:
                return Version.from_string(version_yaml[env][project])
            except KeyError:
                return Version(env=env)
    except FileNotFoundError:
        return Version(env=env)


def write_new_version(versions: list[Project], env: str):
    if env not in envs.keys():
        raise Exception(f"Environment {env} does not exist.")

    try:
        with open(f"./version.yaml", "r") as f:
            version_yaml = yaml.safe_load(f.read()) or {}
    except FileNotFoundError:
        version_yaml = {}

    if env not in version_yaml:
        version_yaml[env] = {}

    for version in versions:
        if version.version is not None or version.version != "":
            version_yaml[env][version.project] = str(version.version)

    with open(f"./version.yaml", "w") as stream:
        stream.write(yaml.dump(version_yaml))
