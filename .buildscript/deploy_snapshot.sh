#!/usr/bin/env bash

echo "[MVN] Deploy snapshot starting >>>>>>>>>>>>>>>>>>>>"
SLUG="Kyson/AndroidGodEye"
JDK="oraclejdk8"

set -e

if [ "$TRAVIS_REPO_SLUG" != "$SLUG" ]; then
  echo "[MVN] Skipping snapshot deployment: wrong repository. Expected '$SLUG' but was '$TRAVIS_REPO_SLUG'."
elif [ "$TRAVIS_JDK_VERSION" != "$JDK" ]; then
  echo "[MVN] Skipping snapshot deployment: wrong JDK. Expected '$JDK' but was '$TRAVIS_JDK_VERSION'."
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "[MVN] Skipping snapshot deployment: was pull request."
elif [ "$TRAVIS_TAG" == "" ]; then
  echo "[MVN] Skipping snapshot deployment: no tag found."
elif [[ "$TRAVIS_TAG" =~ "SNAPSHOT" ]]; then
  echo "[MVN] Writing VERSION..."
  echo "VERSION_NAME=$TRAVIS_TAG" > VERSION
  echo "[MVN] Deploying snapshot..."
  ./gradlew clean uploadArchives
  echo "[MVN] Snapshot deployed!"
else
  echo "[MVN] Skipping snapshot deployment: no SNAPSHOT tag found."
fi

echo "[MVN] Deploy snapshot done. <<<<<<<<<<<<<<<<<<<<"
