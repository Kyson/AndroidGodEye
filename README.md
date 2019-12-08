<p align="center">
  <img src="ART/android_god_eye_logo.png" width="256" height="256" />
</p>

<h1 align="center">AndroidGodEye</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/AndroidGodEye" target="_blank"><img src="https://travis-ci.org/Kyson/AndroidGodEye.svg?branch=master"></img></a>
<a href="https://app.codacy.com/app/Kyson/AndroidGodEye?utm_source=github.com&utm_medium=referral&utm_content=Kyson/AndroidGodEye&utm_campaign=Badge_Grade_Settings" target="_blank"><img src="https://api.codacy.com/project/badge/Grade/e5f4ed2cb65c4e6d87587e8287fe7945"></img></a>
<a href="https://github.com/Kyson/AndroidGodEye/tags" target="_blank"><img src="https://img.shields.io/github/v/tag/Kyson/AndroidGodEye?label=version"></img></a>
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

It is divided into 3 parts:

1. Core provide all performance modules and produce performance datas.
2. Debug Monitor provide a dashboard to show these performance datas.
3. Toolbox make developers easy to use this library.

AndroidGodEye prodive several modules, such as cpu, heap, block, leak memory and so on.

## Features

```java
public static final String CPU = "CPU";                         // cpu info of device and app
public static final String BATTERY = "BATTERY";                 // battery info
public static final String FPS = "FPS";                         // fps info
public static final String LEAK = "LEAK";                       // detect memory leak
public static final String HEAP = "HEAP";                       // heap memory
public static final String PSS = "PSS";                         // pss
public static final String RAM = "RAM";                         // ram
public static final String NETWORK = "NETWORK";                 // network info
public static final String SM = "SM";                           // detect jam
public static final String STARTUP = "STARTUP";                 // startup metric
public static final String TRAFFIC = "TRAFFIC";                 // traffic of device and app
public static final String CRASH = "CRASH";                     // detect java、native crash and ANR
public static final String THREAD = "THREAD";                   // thread dump of app
public static final String PAGELOAD = "PAGELOAD";               // page(Activity and Fragment) lifecycle and load time metric
public static final String METHOD_CANARY = "METHOD_CANARY";     // methods time cost metric
public static final String APP_SIZE = "APP_SIZE";               // App size of apk code、storage and cache
public static final String VIEW_CANARY = "VIEW_CANARY";         // detect complex layout hierarchy and overdraw
public static final String IMAGE_CANARY = "IMAGE_CANARY";       // detect unreasonable memory use of image
```

## Quickstart

**[Quickstart Wiki](https://github.com/Kyson/AndroidGodEye/wiki/0x00-QuickStart_en)**

[Demo APK](https://fir.im/5k67)

[Demo Project:https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

## License

AndroidGodEye is under Apache2.0.

## Contributors

- [ahhbzyz](https://github.com/ahhbzyz)
- [Xiangxingqian](https://github.com/Xiangxingqian)

## About Me

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)
