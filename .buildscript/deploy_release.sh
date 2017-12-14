#!/usr/bin/env bash

echo "[MVN] Deploy release starting >>>>>>>>>>>>>>>>>>>>"
echo "Make sure VERSION has updated."
cd .. && ./gradlew clean uploadArchives
echo "[MVN] Deploy release done. <<<<<<<<<<<<<<<<<<<<"