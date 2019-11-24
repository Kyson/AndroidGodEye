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

## 快速开始

[Demo APK](https://fir.im/5k67)，可以先看看效果 :-)

[Demo Project:https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

### STEP1 依赖引入

#### 1. 添加AndroidGodEye依赖

在需要的Module的`build.gradle`中添加

```groovy
dependencies {
  implementation 'cn.hikyson.godeye:godeye-core:VERSION_NAME'
  debugImplementation 'cn.hikyson.godeye:godeye-monitor:VERSION_NAME'
  releaseImplementation 'cn.hikyson.godeye:godeye-monitor-no-op:VERSION_NAME'
  implementation 'cn.hikyson.godeye:godeye-toolbox:VERSION_NAME'
}
```

> VERSION_NAME参考 [Github release](https://github.com/Kyson/AndroidGodEye/releases)

#### 2. 添加MethodCanary依赖

在Root Project的`build.gradle`中添加

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

> PLUGIN_VERSION_NAME参考 [MethodCanary github release](https://github.com/Kyson/MethodCanary/releases)

在Application Module Project（`'com.android.application'`）的`build.gradle`中添加

```groovy
apply plugin: 'cn.hikyson.methodcanary.plugin'
```

在项目根目录下新建js文件：`MethodCanary.js`用于配置MethodCanary的插桩逻辑，内容如下：

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

[如何配置MethodCanary.js](https://github.com/Kyson/AndroidGodEye/wiki/0x01-Module-MethodCanary)

### STEP2 初始化并安装所需模块

Application中初始化:

```java
GodEye.instance().init(this);
```

模块安装，GodEye类是AndroidGodEye的核心类，所有模块由它提供。

在需要的时候安装所有模块（建议在`application onCreate`中）：

```java
if (ProcessUtils.isMainProcess(this)) {//install in main process
    GodEye.instance().install(GodEyeConfig.fromAssets("<config path>"));
}
```

"\<config path\>"为配置文件在assets下的目录路径，内容参考：[install.config](https://github.com/Kyson/AndroidGodEye/blob/master/android-godeye-sample/src/main/assets/android-godeye-config/install.config)

[卸载模块](https://github.com/Kyson/AndroidGodEye/wiki/0x00-Work-Flow-Overview)

### STEP3 安装性能可视化面板

GodEyeMonitor类是AndroidGodEye的性能可视化面板的主要类，用来开始或者停止性能可视化面板的监控。

开启性能可视化面板：

```java
GodEyeMonitor.work(context)
```

关闭面板：

```java
GodEyeMonitor.shutDown()
```

usb连上你的手机，接下来可以开始运行你的项目了！

[生产环境使用AndroidGodEye](https://github.com/Kyson/AndroidGodEye/wiki/0x00-Work-Flow-Overview)

### STEP4 安装IDE插件

在AndroidStudio中安装AndroidGodEye插件，在AndroidStudio plugin中直接搜索AndroidGodEye即可，安装完之后会在工具栏中出现AndroidGodEye的icon，点击即可在浏览器中打开性能监控面板。

![https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png](https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png)

[没有USB线?]()

[没有安装Android Studio?]()

#### 可选部分

手机和电脑用USB连接，并执行`adb forward tcp:5390 tcp:5390`，然后在pc浏览器中访问`http://localhost:5390/index.html`(**注意：/index.html 是必须加上的**)就可以看到开发者面板了。如果没有USB线，也可以直接访问`http://手机ip+端口+/index.html`，当然，必须确保手机和pc在同一局域网网段。

> 端口默认是5390，也可以在`GodEyeMonitor.work(context,port)`中指定，一般在开发者在调用`GodEyeMonitor.work(context,port)`之后可以看到日志输出 'Open AndroidGodEye dashboard [ http://ip:port/index.html" ] in your browser...' 中包含了访问地址。

Done!

## 性能可视化面板

###### 点击下面预览↓

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

## 框架

下图可以更清楚地解释AndroidGodEye是如何工作的：

![android_god_eye_framework_2](ART/android_god_eye_framework_2.jpg)

## 许可协议

AndroidGodEye使用 Apache2.0 许可协议。

## 贡献者

- [ahhbzyz](https://github.com/ahhbzyz)
- [Xiangxingqian](https://github.com/Xiangxingqian)

## 关于我

- Github: [Kyson](https://github.com/Kyson)
- Weibo: [hikyson](https://weibo.com/hikyson)
- Blog: [tech.hikyson.cn](https://tech.hikyson.cn/)









