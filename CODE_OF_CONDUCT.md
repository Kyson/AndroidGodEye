# Code of Conduct

## Build

### Build Sample APK

Clone then build, then run `android android-godeye-sample`

### Build Debug Dashboard

1. `cd android-godeye-monitor-dashboard`
2. `npm install` or `cnpm install` if needed.
3. [`npm run bd`] or [`npm run build` then `npm run deploy`]
4. Build and run `android android-godeye-sample`

## Deploy

1. Exec `./gradlew generateChangelog -PchangelogVersion=3.1.12` to update CHANGELOG.md, 添加或删除模块需要更新README.md和[wiki](https://github.com/Kyson/AndroidGodEye/wiki)
2. Merge to master branch then tag with version name then push.(本地测试会使用gradle.properties文件中的版本号)
3. 查看编译状态：[AndroidGodEye-travis-ci](https://travis-ci.org/Kyson/AndroidGodEye/builds)

## Code

Model数据类，包含Config类型

1. 必须实现Serializable接口
2. 必须@Keep
3. 内容必须可序列化
4. 必须添加toString方法

## Changelog

Based on [keepachangelog](https://keepachangelog.com/en/1.0.0/)

- Added for new features.
- Changed for changes in existing functionality.
- Deprecated for soon-to-be removed features.
- Removed for now removed features.
- Fixed for any bug fixes.
- Security in case of vulnerabilities.

