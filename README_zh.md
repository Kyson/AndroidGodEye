<p align="center">
  <img src="ART/android_god_eye_logo.png" width="256" height="256" />
</p>

<h1 align="center">AndroidGodEye</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/AndroidGodEye" target="_blank"><img src="https://travis-ci.org/Kyson/AndroidGodEye.svg?branch=master"></img></a>
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

> Android开发者在性能检测方面的工具一直比较匮乏，仅有的一些工具，比如Android Device Monitor，使用起来也有些繁琐，对开发者能力有一定的要求。而线上的App监控更无从谈起。所以需要有一个系统能够提供Debug和Release阶段全方位的监控，更深入地了解对App运行时的状态。

## 概览

![android_godeye_connect](ART/android_god_eye_connect.jpg)

AndroidGodEye是一个可以在PC浏览器中实时监控Android性能数据指标的工具，你可以通过wifi/usb连接手机和pc，通过pc浏览器实时监控手机性能。

系统分为三部分：

1. Core 核心部分，提供所有模块
2. Debug Monitor部分，提供Debug阶段开发者面板
3. Toolbox 快速接入工具集，给开发者提供各种便捷接入的工具

AndroidGodEye提供了多种监控模块，比如cpu、内存、卡顿、内存泄漏等等，并且提供了Debug阶段的Monitor看板实时展示这些数据。而且提供了api供开发者在release阶段进行数据上报。

## 支持功能

```java
public static final String CPU = "CPU";                         // 手机和App Cpu检测
public static final String BATTERY = "BATTERY";                 // 电池检测
public static final String FPS = "FPS";                         // 帧率检测
public static final String LEAK = "LEAK";                       // 内存泄漏检测
public static final String HEAP = "HEAP";                       // 运行堆内存占用检测
public static final String PSS = "PSS";                         // 实际物理共享内存占用检测
public static final String RAM = "RAM";                         // 手机内存
public static final String NETWORK = "NETWORK";                 // 网络请求检测
public static final String SM = "SM";                           // 卡顿检测
public static final String STARTUP = "STARTUP";                 // 启动检测
public static final String TRAFFIC = "TRAFFIC";                 // 手机和App流量检测
public static final String CRASH = "CRASH";                     // Java、Native崩溃/ANR
public static final String THREAD = "THREAD";                   // App线程即堆栈Dump检测
public static final String PAGELOAD = "PAGELOAD";               // 页面加载和生命周期检测
public static final String METHOD_CANARY = "METHOD_CANARY";     // 方法耗时检测
public static final String APP_SIZE = "APP_SIZE";               // App大小，包括apk、存储和缓存
public static final String VIEW_CANARY = "VIEW_CANARY";         // 视图层级、过度绘制检测
public static final String IMAGE_CANARY = "IMAGE_CANARY";       // 图片不合理内存占用检测
```

## 使用

[快速开始Wiki](https://github.com/Kyson/AndroidGodEye/wiki/0x00-QuickStart_zh)

[更多信息Wiki](https://github.com/Kyson/AndroidGodEye/wiki#%E4%B8%AD%E6%96%87)

[Demo APK](https://fir.im/5k67)，可以先看看效果 :-)

[Demo Project:https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

## 许可协议

AndroidGodEye使用 Apache2.0 许可协议。

## 贡献者

- [ahhbzyz](https://github.com/ahhbzyz)
- [Xiangxingqian](https://github.com/Xiangxingqian)

## 关于我

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)
