#!/usr/bin/env bash

echo "[MVN] Deploy release starting >>>>>>>>>>>>>>>>>>>>"
cd .. && ./gradlew clean uploadArchives
echo "[MVN] Deploy release done. <<<<<<<<<<<<<<<<<<<<"