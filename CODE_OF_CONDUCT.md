# Code of Conduct

## Model

Model数据类，包含Config类型

1. 必须实现Serializable接口
2. 必须@Keep
3. 内容必须可序列化
4. 必须添加toString方法

## Deploy

1. update CHANGELOG.md
2. 修改gradle.properties文件中的版本号
3. 改动合并到master，并打版本号命名的tag
4. PUSH，查看编译状态：[AndroidGodEye-travis-ci](https://travis-ci.org/Kyson/AndroidGodEye/builds)
5. 修改README.md和[wiki](https://github.com/Kyson/AndroidGodEye/wiki)

## Ignore

### Jcenter upload，不需要手动调用

bintrayKey: [https://bintray.com/profile/edit](https://bintray.com/profile/edit) API Key

./gradlew clean build bintrayUpload -PbintrayUser=kyson -PbintrayKey=BINTRAY_KEY -PdryRun=false
