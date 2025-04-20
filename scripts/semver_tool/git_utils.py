from git import Repo


def get_commits(project, env='main', repo_path="."):
    repo = Repo(repo_path)
    tags = sorted(repo.tags, key=lambda t: t.commit.committed_datetime)

    # TODO: add check for env
    last_tag = next((t for t in reversed(tags) if str(t).startswith(f"{project}@")), None)

    if last_tag:
        commit_range = f'{last_tag.commit}..HEAD'
    else:
        commit_range = f'HEAD'

    commits = repo.iter_commits(commit_range, paths=project)

    return [c.message.strip() for c in commits]


def create_tag(project, version, repo_path="."):
    repo = Repo(repo_path)
    tag = f'{project}@v{version}'
    repo.create_tag(tag)
