def generate_release_notes(commits, version, project):
    breaking = []
    features = []
    fixes = []
    others = []

    for commit in commits:
        if "BREAKING CHANGE" in commit:
            breaking.append(commit)
        elif commit.startswith("feat"):
            features.append(commit)
        elif commit.startswith("fix"):
            fixes.append(commit)
        else:
            others.append(commit)
    notes = [f"## {project} v{version}\n"]

    if breaking:
        notes.append("### ⚠️ Breaking Changes\n" + "\n".join(f"- {c}" for c in breaking))
    if features:
        notes.append("### ✨ Features\n" + "\n".join(f"- {c}" for c in features))
    if fixes:
        notes.append("### 🐛 Fixes\n" + "\n".join(f"- {c}" for c in fixes))
    if others:
        notes.append("### Other commits\n" + "\n".join(f"- {c}" for c in others))

    return "\n\n".join(notes)


def save_release_notes(release_notes, file="release_notes.md"):
    with open(file, "w") as f:
        f.write(release_notes)


def read_release_notes(project):
    try:
        with open(f"release_notes/{project}.md", "r") as f:
            return f.read()
    except FileNotFoundError:
        return ""