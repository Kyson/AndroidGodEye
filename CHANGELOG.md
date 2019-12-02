# CHANGELOG

Base on [keepachangelog](https://keepachangelog.com/en/1.0.0/)

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
