import copy
import io
from typing import Self

import yaml

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


class Version(object):
    def __eq__(self, __value):
        return str(self) == str(__value)

    major: int
    minor: int
    patch: int
    build: int
    env: str
    main : Self

    def __init__(self, major=0, minor=0, patch=0, build=0, env="main", main_version=Self):
        self.major = major
        self.minor = minor
        self.patch = patch
        self.build = build
        self.env = env
        self.main = main_version

    def bump_major(self):
        self.major += 1
        return self

    def bump_minor(self):
        if self.minor == self.main.minor:
            self.minor += 1
        else:
            self.build += 1
        return self

    def bump_patch(self):
        if self.patch == self.main.patch:
            self.patch += 1
        else:
            self.build += 1
        self.patch += 1
        return self

    def bump_build(self):
        self.build += 1
        return self

    @staticmethod
    def from_string(string):
        split = string.split(".")

        major = int(split[0])
        minor = int(split[1])
        patch_end_index = split[2].find("-")
        env_end_index = split[2].find("_")
        env = ""
        if patch_end_index != -1:
            env = split[2][patch_end_index + 1:env_end_index]
        else:
            if env_end_index != -1:
                patch_end_index = env_end_index
            else:
                patch_end_index = len(split[2])

        patch = int(split[2][:patch_end_index])
        build = 1
        if env_end_index != -1:
            build = int(split[2][env_end_index + 1:])

        return Version(major=major, minor=minor, patch=patch, build=build, env=env)

    def __str__(self):
        if self.env != "main":
            return f"{self.major}.{self.minor}.{self.patch}-{self.env}_{self.build}"
        else:
            if self.build > 1:
                return f"{self.major}.{self.minor}.{self.patch}-{self.build}"
            return f"{self.major}.{self.minor}.{self.patch}"


def get_version(version: Version, main_version: Version, commits, scope):
    major, minor, patch, build = 0, 0, 0, 0

    for message in commits:
        message_scope = get_scope(message)
        if message_scope is not None and message_scope != scope:
            break

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
        with io.open("version.yaml", "r") as stream:
            version_yaml = yaml.safe_load(stream)

            if version_yaml is None:
                return Version(env=env)

            try:
                return Version.from_string(version_yaml[env][project])
            except KeyError:
                return Version(env=env)
    except FileNotFoundError:
        return Version(env=env)


def write_new_version(version: Version, project):
    with io.open(f"version.yaml", "w+") as stream:
        version_yaml = yaml.safe_load(stream)

        if version.env not in envs.keys():
            raise Exception(f"Environment {version.env} does not exist.")

        if version_yaml is None:
            version_yaml = {}

        version_yaml.update({f'{version.env}': {
            f'{project}': str(version),
        }})

        stream.write(yaml.dump(version_yaml))
