from typing import Self


class Version(object):
    def __eq__(self, __value):
        return str(self) == str(__value)

    major: int
    minor: int
    patch: int
    build: int
    env: str
    main: Self

    def __init__(self, major=0, minor=0, patch=0, build=0, env="main", main_version=Self):
        self.major = major
        self.minor = minor
        self.patch = patch
        self.build = build
        self.env = env
        self.main = main_version

    def bump_major(self):
        bump_major = True

        if self.major > self.main.major:
            bump_major = False

        if bump_major:
            self.major = self.main.major + 1
        else:
            self.bump_build()
        return self

    def bump_minor(self):
        bump_minor = True

        if self.major > self.main.major:
            bump_minor = False
        elif self.minor > self.main.minor:
            bump_minor = False

        if bump_minor:
            self.minor = self.main.major + 1
        else:
            self.bump_build()
        return self

    def bump_patch(self):
        bump_patch = True

        if self.major > self.main.major:
            bump_patch = False
        elif self.minor > self.main.minor:
            bump_patch = False
        elif self.patch > self.main.patch:
            bump_patch = False

        if bump_patch:
            self.patch = self.main.patch + 1
        else:
            self.bump_build()
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
            env_dev = split[2][patch_end_index + 1:]
            end = env_dev.find("_")
            if end != -1:
                env = env_dev[:end]
            else:
                env = env_dev
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
            if self.build == 0:
                return f"{self.major}.{self.minor}.{self.patch}-{self.env}_1"
            return f"{self.major}.{self.minor}.{self.patch}-{self.env}_{self.build}"
        else:
            if self.build > 1:
                return f"{self.major}.{self.minor}.{self.patch}-{self.build}"
            return f"{self.major}.{self.minor}.{self.patch}"


class Project(object):
    project: str
    version: str

    def __init__(self, project: str, version=""):
        self.project = project
        self.version = version
