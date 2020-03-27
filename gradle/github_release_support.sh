#!/usr/bin

WORK_DIR=$(dirname "$0")
cd "$WORK_DIR/.." || exit
pwd
echo "[Github Release] clean up..."
./gradlew cleanupGithubRelease
echo "[Github Release] assemble release..."
./gradlew clean && ./gradlew :android-godeye-sample:assembleRelease
echo "[Github Release] copy release..."
./gradlew copyReleaseForGithubRelease
echo "[Github Release] assemble debug..."
./gradlew clean && ./gradlew :android-godeye-sample:assembleDebug
echo "[Github Release] copy debug..."
./gradlew copyDebugForGithubRelease
echo "[Github Release] copy changelog..."
./gradlew copyChangelogForGithubRelease
echo "[Github Release] done."
