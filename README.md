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

## Quickstart

[Demo APK](https://fir.im/5k67)

[Demo Project:https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

### Step1 Dependencies

#### Module Project `build.gradle`

```groovy
dependencies {
  implementation 'cn.hikyson.godeye:godeye-core:VERSION_NAME'
  debugImplementation 'cn.hikyson.godeye:godeye-monitor:VERSION_NAME'
  releaseImplementation 'cn.hikyson.godeye:godeye-monitor-no-op:VERSION_NAME'
  implementation 'cn.hikyson.godeye:godeye-toolbox:VERSION_NAME'
}
```

> Find VERSION_NAME in [Github release](https://github.com/Kyson/AndroidGodEye/releases)

#### Root Project `build.gradle`

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "cn.hikyson.methodcanary:plugin:PLUGIN_VERSION_NAME"
    }
}
```

> Find PLUGIN_VERSION_NAME in [MethodCanary](https://github.com/Kyson/MethodCanary) github release

You need to config the logic of method canary injection, create js file:`MethodCanary.js` in project root dir, content like following:

```
/**
    classInfo
        {int access
         String name
         String superName
         String[] interfaces}

     methodInfo
         {int access
         String name
         String desc}
**/
function isExclude(classInfo,methodInfo){
    return false
}

function isInclude(classInfo,methodInfo){
    return classInfo.name.startsWith('cn/hikyson/godeyedemo'))
}
```

> More configurations reference: [MethodCanary](https://github.com/Kyson/MethodCanary)

#### Module Project `'com.android.application'` `build.gradle`

```groovy
apply plugin: 'cn.hikyson.methodcanary.plugin'
```

### Step2 Initialize And Install Modules

Init first in your application:

```java
GodEye.instance().init(this);
```

Install modules in `application onCreate`, GodEye class is entrance for this step, all modules are provided by it.

```java
if (ProcessUtils.isMainProcess(this)) {//install in main process
    GodEye.instance().install(GodEyeConfig.fromAssets("<config path>"));
}
```

"<config path>" is assets path of config, content reference: [install.config](https://github.com/Kyson/AndroidGodEye/blob/master/android-godeye-sample/src/main/assets/android-godeye-config/install.config)

#### Optional Uninstall Modules

Uninstall modules when you don't need it(not recommend):

```java
GodEye.instance().uninstall();
```

> Note that network and startup module don't need install and uninstall.

When install finished, GodEye begin produce performance data, generally you can call consume of modules to get these datas, for example：

```java
GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).subject().subscribe()
```

> Just like we will mention later,Debug Monitor is one of these consumers.

### Step3 Install Performance Visualization Dashboard
                  
GodEyeMonitor class is entrance for this step.

Start performance visualization dashboard:

```java
GodEyeMonitor.work(context)
```

Stop it:

```java
GodEyeMonitor.shutDown()
```

### Install IDE Plugin

Install Android Studio plug-in(Search AndroidGodEye in Android Studio plugin setting),Then you can find AndroidGodEye in main toolbar,click it and it will open dashboard in browser.

![https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png](https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png)

Connect mobile phones and computers with USB, run `adb forward tcp:5390 tcp:5390`, then open `http://localhost:port/index.html`(**Note that /index.html is necessary!!!**) on PC. If you don't have a USB, you can also open `http://mobile ip:port/index.html` directly, ensure that mobile phones and PC are in the same LAN segment of course.

> Default port is 5390, you can find ip in logcat output after call `GodEyeMonitor.work(context,port)`, log is like:'Open AndroidGodEye dashboard [ http://ip:port/index.html" ] in your browser...'.

Now enjoy it!

## Performance Visualization Dashboard

###### Click  ↓  to preview

<p>
<a href="https://player.youku.com/embed/XMzIwMTgyOTI5Mg==" target:"_blank">
<img border="0" src="ART/android_god_eye_play.png" width="128" height="128" />
</a>
</p>

### MethodCanary

![android_god_eye_dashboard1](ART/android_god_eye_dashboard1.png)

### Fps/RAM/PSS/Battery...

![android_god_eye_dashboard2](ART/android_god_eye_dashboard2.png)

### Cpu/Heap/Traffic...

![android_god_eye_dashboard3](ART/android_god_eye_dashboard3.png)

### Leak Memory/App Jank(Block)

![android_god_eye_dashboard4](ART/android_god_eye_dashboard4.png)

**Leak Memory GIF**

![android_god_eye_leak](ART/android_god_eye_leak.gif)

**Jank(Block) GIF**

![android_god_eye_block](ART/android_god_eye_block.gif)

### Page Lifecycle(Page Load)/Network

![android_god_eye_dashboard5](ART/android_god_eye_dashboard5.png)

**Page Lifecycle(Page Load) GIF**

![android_god_eye_pageload](ART/android_god_eye_pageload.gif)

**Network GIF**

![android_god_eye_network](ART/android_god_eye_network.gif)

**Network Detail**

![android_god_eye_dashboard6](ART/android_god_eye_dashboard6.png)

### Thread

![android_god_eye_dashboard7](ART/android_god_eye_dashboard7.png)

## Modules

|模块名|需要安装|数据生产时机|配置|备注|
|-----|-------|---------|---|----|
|network|否|外部输入时输出|无|-|
|startup|否|外部输入时输出|无|-|
|battery|是|电池变化时输出|无|-|
|cpu|是|定时输出|intervalMillis-每隔x毫秒输出数据，sampleMillis-采样间隔|系统版本大于8.0失效|
|crash|是|安装后，输出上次崩溃|crashProvider-实现CrashProvider的类path，一般用内置cn.hikyson.godeye.core.internal.modules.crash.CrashFileProvider即可|-|
|fps|是|定时输出|intervalMillis-输出间隔|-|
|heap|是|定时输出|intervalMillis-输出间隔|-|
|leakDetector(leakMemory)|是|页面销毁且泄漏时|debug-是否需要解析gc引用链，debugNotification泄漏时是否需要通知，leakRefInfoProvider-实现LeakRefInfoProvider的类path，一般用内置cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider|-|
|pageload|是|页面create/draw/destory/load/hide/show等输出|pageInfoProvider-根据页面实例提供页面信息|fragment的显示隐藏需要手动调用show hide api,页面加载手动调用load api|
|pss|是|定时输出|intervalMillis-输出间隔|-|
|ram|是|定时输出|intervalMillis-输出间隔|-|
|sm|是|卡顿时输出|debugNotify-卡顿是否需要通知，dumpIntervalMillis-dump堆栈间隔，longBlockThresholdMillis-长卡顿阈值，shortBlockThresholdMillis-短卡顿阈值|-|
|thread|是|定时|intervalMillis-输出间隔，threadFilter-过滤器，实现ThreadFilter类path，一般用内置cn.hikyson.godeye.core.internal.modules.thread.SimpleThreadFilter即可|-|
|traffic|是|定时输出|intervalMillis-输出间隔，sampleMillis-采样间隔|-|
|methodCanary|是|停止后输出|maxMethodCountSingleThreadByCost-每个线程最多记录的方法数，lowCostMethodThresholdMillis-方法耗时阈值|-|

## Framework

How does AndroidGodEye work？As below:

![android_god_eye_framework_2](ART/android_god_eye_framework_2.jpg)

## License

AndroidGodEye is under Apache2.0.

## Contributors

- [ahhbzyz](https://github.com/ahhbzyz)
- [Xiangxingqian](https://github.com/Xiangxingqian)

## About Me

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)









