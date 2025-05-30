import re

from git import Repo


def get_commits(project, env='main', repo_path="."):
    repo = Repo(repo_path)
    tags = sorted(repo.tags, key=lambda t: t.commit.committed_datetime)

    # TODO: add check for env
    if env == "main":
        pattern = rf"^{project}_\d\.\d\.\d(?:_\d+)?$"
    else:
        pattern = rf"^{project}_\d\.\d\.\d-{env}.*$"

    last_tag = next((t for t in reversed(tags) if re.match(pattern, str(t))), None)

    if last_tag:
        commit_range = f'{last_tag.commit}..HEAD'
    else:
        commit_range = f'HEAD'

    commits = repo.iter_commits(commit_range)

    return [c.message.strip() for c in commits]


def create_tag(project, version, repo_path="."):
    repo = Repo(repo_path)
    tag = f'{project}@v{version}'
    repo.create_tag(tag)
