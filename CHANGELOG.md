# CHANGELOG

Base on [keepachangelog](https://keepachangelog.com/en/1.0.0/)

```text
Added for new features.
Changed for changes in existing functionality.
Deprecated for soon-to-be removed features.
Removed for now removed features.
Fixed for any bug fixes.
Security in case of vulnerabilities.
```

## 3.1.11

### Changed

- ImageCanary support "ImageView is visible or not"

## 3.1.10

### Changed

- Android Studio plugin and shell/bat script of AndroidGodEye updated and can auto identify ports

### Fixed

- Long text of MethodCanary's method tree display not complete
- Main thread color of MethodCanary should be black

## 3.1.9

### Fixed

- MethodCanary method tree show wrong

### Changed

- ImageCanary support preview

## 3.1.8

### Fixed

- Crash when ImageCanary install in not main thread 

### Changed

- Refactor thread util

## 3.1.7

### Fixed

- GodEyeHelper.stopMethodCanaryRecording error

## 3.1.6

### Changed

- Keep imageIssue class impl serializable 

## 3.1.5

### Added

- GodEyeHelper class add some helper methods:inspectView、methodCanaryStart/StopMonitor

### Changed

- Removed ImageCanary config(ImageCanaryConfigProvider):BitmapInfoAnalyzer
- ImageCanary will not be output issues which have been output before

## 3.1.4

### Added

- Image Canary

## 3.1.3

### Added

- ImageCanary

### Changed

- Migrate to androidx
- Split android-godeye-toolbox to android-godeye-okhttp and android-godeye-xcrash
- Crash output CrashInfo list instead of Map
