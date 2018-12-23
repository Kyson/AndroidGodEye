## TODO

- Thread watch
- NDK crash handler
- Startup rocket

## Deploy

### MAVEN CENTRAL 太难用了...弃用

install gpg: brew install -v gpg

配置~/gradle/gradle.properties

SONATYPE_NEXUS_USERNAME=
SONATYPE_NEXUS_PASSWORD=

cd .buildscript
sh deploy_release.sh

### jcenter

bintrayKey:https://bintray.com/profile/edit

./gradlew clean build bintrayUpload -PbintrayUser=kyson -PbintrayKey=BINTRAY_KEY -PdryRun=false