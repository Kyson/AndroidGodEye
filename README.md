# AndroidGodEye

![http://photo.tech.hikyson.cn/android_god_eye_logo.png](http://photo.tech.hikyson.cn/android_god_eye_logo.png)

## 这是什么？

这是用于监控Android数据指标（比如性能指标，但是不局限于性能）的开源库。

## 为什么监控？

开发者只有对应用运行状态了如指掌才可以发现并解决问题，所以需要有一个系统对运行数据进行全方位监控。

## 如何监控？

目前，AndroidGodEye提供了11种监控模块，比如cpu、内存、卡顿、内存泄漏等等，并且提供了Debug阶段的Monitor看板实时展示这
些数据。而且提供了api供开发者在release阶段进行数据上报。

## 快速开始

### 0x00 依赖

gradle依赖

### 0x01 核心模块安装

GodEye类是AndroidGodEye的核心类，所有模块由它提供。

在应用入口安装所有模块：

```java
GodEye.instance().installAll(getApplication());
```

> 安装完之后相应的模块就开始输出数据了

在不需要的时候可以卸载所有模块：

```java
GodEye.instance().uninstallAll(getApplication());
```

> 开发者也可以根据自己的需求安装模块,注意：network和startup模块不需要安装和卸载,leak detector不需要卸载

### 0x02 Debug面板安装

GodEyeMonitor类是AndroidGodEye的Debug监控面板的主要类，用来开始或者停止Debug面板的监控。

