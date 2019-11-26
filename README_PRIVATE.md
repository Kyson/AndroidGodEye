# Deploy

1. update CHANGELOG.md
2. 修改gradle.properties文件中的版本号
3. 改动合并到master，并打版本号命名的tag
4. PUSH即可，查看编译状态：[AndroidGodEye-travis-ci](https://travis-ci.org/Kyson/AndroidGodEye/builds)

## jcenter，不需要手动调用

bintrayKey:https://bintray.com/profile/edit API Key

./gradlew clean build bintrayUpload -PbintrayUser=kyson -PbintrayKey=BINTRAY_KEY -PdryRun=false

## react-boostrap vs antd

6.8M vs 19.9M

## Run emulator

cd ~/Library/Android/sdk/emulator
./emulator -avd 4.65_720p_Galaxy_Nexus_API_25 -dns-server 8.8.8.8,8.8.4.4

## MAVEN CENTRAL 太难用了...弃用

install gpg: brew install -v gpg

配置~/gradle/gradle.properties

SONATYPE_NEXUS_USERNAME=
SONATYPE_NEXUS_PASSWORD=

cd .buildscript
sh deploy_release.sh