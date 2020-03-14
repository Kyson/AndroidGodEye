<p align="center">
  <img src="ART/android_god_eye_logo.png" width="256" height="256" />
</p>

<h1 align="center">AndroidGodEye</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/AndroidGodEye" target="_blank"><img src="https://travis-ci.org/Kyson/AndroidGodEye.svg?branch=master"></img></a>
<a href="https://github.com/Kyson/AndroidGodEye/tags" target="_blank"><img src="https://img.shields.io/github/v/tag/Kyson/AndroidGodEye?label=version"></img></a>
<a href="https://codecov.io/gh/Kyson/AndroidGodEye"><img src="https://codecov.io/gh/Kyson/AndroidGodEye/branch/master/graph/badge.svg" /></a>
<a href="http://androidweekly.net/issues/issue-293" target="_blank"><img src="https://img.shields.io/badge/Android%20Weekly-%23293-blue.svg"></img></a>
<a href="https://android-arsenal.com/details/1/6561" target="_blank"><img src="https://img.shields.io/badge/Android%20Arsenal-AndroidGodEye-brightgreen.svg?style=flat"></img></a>
<a href="LICENSE" target="_blank"><img src="http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat"></img></a>
</p>
<br/>

<p>
<a href="README.md">English README.md</a>&nbsp;&nbsp;&nbsp;
<a href="README_zh.md">中文 README_zh.md</a>
</p>

> Android developer lack of monitoring of performance data,especially in production environment. so we need "AndroidGodEye".

## Overview

![android_godeye_connect](ART/android_god_eye_connect.jpg)

AndroidGodEye is a performance monitor tool for Android(not limited to performance data) , you can easily monitor the performance of your app in real time in pc browser.

You can also use it in production environment to monitor the online performance of your app([Apply AndroidGodEye for production](https://github.com/Kyson/AndroidGodEye/wiki/0x02b-Apply-AndroidGodEye-for-Release_en)).

> AndroidGodEye has been applied to several mature apps at present

It is divided into 3 parts:

1. Core provide all performance modules and produce performance datas.
2. Debug Monitor provide a dashboard to show these performance datas.
3. Toolbox make developers easy to use this library.

## Features

|Module Name|Desc|
|-|-|
|CPU|Cpu info of device and app|
|BATTERY|Battery info|
|FPS|Fps info|
|LEAK|Detect memory leak|
|HEAP|Heap memory|
|PSS|Pss|
|RAM|Ram|
|NETWORK|Network info|
|SM|Detect jam|
|STARTUP|Startup metric|
|TRAFFIC|Traffic of device and app|
|CRASH|Detect java、native crash and ANR|
|THREAD|Thread dump of app|
|PAGELOAD|Page(Activity and Fragment) lifecycle event and cost time|
|METHOD_CANARY|Methods time cost metric|
|APP_SIZE|App size of apk code、storage and cache|
|VIEW_CANARY|Detect complex layout hierarchy and overdraw|
|IMAGE_CANARY|Detect unreasonable memory use of image|

## Usage

[Quickstart Wiki](https://github.com/Kyson/AndroidGodEye/wiki/0x00-QuickStart_en)

[More detail Wiki](https://github.com/Kyson/AndroidGodEye/wiki#english)

[Sample APK](https://github.com/Kyson/AndroidGodEye/releases)

[Demo Project and APK](https://github.com/Kyson/AndroidGodEyeDemo/releases)

## License

AndroidGodEye is under Apache2.0.

## Contributors

- [ahhbzyz](https://github.com/ahhbzyz)
- [Xiangxingqian](https://github.com/Xiangxingqian)

## About Me

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)
