# Deploy

1. 修改gradle.properties文件中的版本号
2. 改动合并到master，并打版本号命名的tag
3. PUSH即可

## MAVEN CENTRAL 太难用了...弃用

install gpg: brew install -v gpg

配置~/gradle/gradle.properties

SONATYPE_NEXUS_USERNAME=
SONATYPE_NEXUS_PASSWORD=

cd .buildscript
sh deploy_release.sh

## jcenter，不需要手动调用

bintrayKey:https://bintray.com/profile/edit API Key

./gradlew clean build bintrayUpload -PbintrayUser=kyson -PbintrayKey=BINTRAY_KEY -PdryRun=false

## react-boostrap vs antd

6.8M vs 19.9M