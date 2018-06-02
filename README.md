<p align="center">
  <img src="ART/android_god_eye_logo.png" width="256" height="256" />
</p>

<h1 align="center">AndroidGodEye</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/AndroidGodEye" target="_blank"><img src="https://travis-ci.org/Kyson/AndroidGodEye.svg?branch=master"></img></a>
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
2. Debug Monitor provide a panel to show these performance datas.
3. Toolbox make developers easy to use this library.

AndroidGodEye prodive several modules, such as cpu, heap, block, leak memory and so on.

## Quickstart

Demo:[https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

### Step1

In your build.gradle:

```
dependencies {
  implementation 'cn.hikyson.godeye:godeye-core:VERSION_NAME'
  debugImplementation 'cn.hikyson.godeye:godeye-monitor:VERSION_NAME'
  releaseImplementation 'cn.hikyson.godeye:godeye-monitor-no-op:VERSION_NAME'
  implementation 'cn.hikyson.godeye:godeye-toolbox:VERSION_NAME'
}
```

> You can find VERSION_NAME in the github release.

### Step2

Init first in your application:

```java
GodEye.instance().init(this);
```

Install modules , GodEye class is entrance for this step, all modules are provided by it.

```java
// before v1.7.0
// GodEye.instance().installAll(getApplication(),new CrashFileProvider(context))
// after v1.7.0 ,install one by one
GodEye.instance().install(Cpu.class, new CpuContextImpl())
                .install(Battery.class, new BatteryContextImpl(this))
                .install(Fps.class, new FpsContextImpl(this))
                .install(Heap.class, Long.valueOf(2000))
                .install(Pss.class, new PssContextImpl(this))
                .install(Ram.class, new RamContextImpl(this))
                .install(Sm.class, new SmContextImpl(this, 1000, 300, 800))
                .install(Traffic.class, new TrafficContextImpl())
                .install(Crash.class, new CrashFileProvider(this))
                .install(ThreadDump.class, new ThreadContextImpl())
                .install(DeadLock.class, new DeadLockContextImpl(GodEye.instance().getModule(ThreadDump.class).subject(), new DeadlockDefaultThreadFilter()))
                .install(Pageload.class, new PageloadContextImpl(this))
                .install(getApplication(), new RxPermissionRequest());
```

> Recommend install in application.

#### Optional

Uninstall modules when you don't need it(not recommend):

```java
// before v1.7.0
// GodEye.instance().uninstallAll()
// after v1.7.0 ,uninstall one by one
GodEye.instance().getModule(Cpu.class).uninstall();
```

> Note that network and startup module don't need install and uninstall.

When install finished, GodEye begin produce performance data, generally you can call consume of modules to get these datas, for example：

```java
// before v1.7.0
// GodEye.instance().cpu().subject().subscribe()
// after v1.7.0, get module by class
GodEye.instance().getModule(Cpu.class).subject().subscribe();
```

> Just like we will mention later,Debug Monitor is one of these consumers.

### Step3

Install debug monitor,GodEyeMonitor class is entrance for this step.

Consume data produced by GodEye modules:

```java
GodEyeMonitor.work(context)
```

Stop it:

```java
GodEyeMonitor.shutDown()
```

### Step4

You're good to go!

Make sure your android device and pc are on the same network segment, and open browser on pc, then open `Android device ip : Port`

Or if you are using it over usb, run `adb forward tcp:5390 tcp:5390`, then open `http://localhost:5390/`.

now enjoy it!

> Default port is 5390, you can find ip in logcat output after call `GodEyeMonitor.work(context)`, log is like:'Open AndroidGodEye dashboard [ http://xxx.xxx.xxx.xxx:5390" ] in your browser...'.

**Okay...If you just want to see the results, you can install [APK](https://fir.im/5k67) directly.**

## Debug Monitor

###### Click  ↓  to preview

<p>
<a href="https://player.youku.com/embed/XMzIwMTgyOTI5Mg==" target:"_blank">
<img border="0" src="ART/android_god_eye_play.png" width="128" height="128" />
</a>
</p>

### Base info

![android_godeye_summary](ART/android_god_eye_summary.png)

### Block Detector

![android_god_eye_block](ART/android_god_eye_block.gif)

### Leak Memory Detector

![android_god_eye_leak](ART/android_god_eye_leak.gif)

### More

![android_god_eye_cpuheaptraffic](ART/android_god_eye_cpuheaptraffic.gif)

and more...

## Modules

|Module Name|Need Install|Engine|Data produce time|permissions|
|-----------|------------|------|-----------------|-----------|
|cpu|yes|internal|interval|no|
|battery|yes|internal|interval|no|
|fps|yes|internal|interval|no|
|leakDetector|yes|internal|happen|WRITE_EXTERNAL_STORAGE|
|heap|yes|internal|interval|no|
|pss|yes|internal|interval|no|
|ram|yes|internal|interval|no|
|network|no|external|-|no|
|sm|yes|internal|happen|no|
|startup|no|external|-|no|
|traffic|yes|external|interval|no|
|crash|yes|external|after install,one time|no|
|thread dump|yes|internal|interval|no|
|deadlock|yes|internal|interval&happen|no|
|pageload|yes|internal|happen|no|

## Framework

How does AndroidGodEye work？As below:

![android_god_eye_framework](ART/android_god_eye_framework.jpeg)

## License

AndroidGodEye is under Apache2.0.

## About Me

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)









