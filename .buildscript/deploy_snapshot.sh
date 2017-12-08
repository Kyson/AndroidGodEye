#!/usr/bin/env bash

echo "Deploy snapshot starting..."
SLUG="Kyson/AndroidGodEye"
JDK="oraclejdk8"
BRANCH="master"

set -e

if [ "$TRAVIS_REPO_SLUG" != "$SLUG" ]; then
  echo "Skipping snapshot deployment: wrong repository. Expected '$SLUG' but was '$TRAVIS_REPO_SLUG'."
elif [ "$TRAVIS_JDK_VERSION" != "$JDK" ]; then
  echo "Skipping snapshot deployment: wrong JDK. Expected '$JDK' but was '$TRAVIS_JDK_VERSION'."
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "Skipping snapshot deployment: was pull request."
elif [ "$TRAVIS_BRANCH" != "$BRANCH" ]; then
  echo "Skipping snapshot deployment: wrong branch. Expected '$BRANCH' but was '$TRAVIS_BRANCH'."
elif [ "$TRAVIS_TAG" == "" ]; then
  echo "Skipping snapshot deployment: no tag found."
else
  pwd
  echo "VERSION_NAME=$TRAVIS_TAG" > ../VERSION
  echo "Deploying snapshot..."
  ./gradlew clean uploadArchives
  echo "Snapshot deployed!"
fi

echo "Deploy snapshot done."
