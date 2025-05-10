set +e
VERSION=$(python scripts/semver_tool/semver_tool.py -e dev di-framework)
CODE=$?
set -e

if [ "$CODE" -eq 0 ]; then
  echo "release_needed=true" >> $GITHUB_OUTPUT
  echo "version=$VERSION" >> $GITHUB_OUTPUT
else
  echo "release_needed=false" >> $GITHUB_OUTPUT
fi

echo $GITHUB_OUTPUT
echo $VERSION