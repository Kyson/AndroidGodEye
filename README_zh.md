<p align="center">
  <img src="ART/android_god_eye_logo.png" width="256" height="256" />
</p>

<h1 align="center">AndroidGodEye</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/AndroidGodEye" target="_blank"><img src="https://travis-ci.org/Kyson/AndroidGodEye.svg?branch=master"></img></a>
<a href="https://app.codacy.com/app/Kyson/AndroidGodEye?utm_source=github.com&utm_medium=referral&utm_content=Kyson/AndroidGodEye&utm_campaign=Badge_Grade_Settings" target="_blank"><img src="https://api.codacy.com/project/badge/Grade/e5f4ed2cb65c4e6d87587e8287fe7945"></img></a>
<a href="http://androidweekly.net/issues/issue-293" target="_blank"><img src="https://img.shields.io/badge/Android%20Weekly-%23293-blue.svg"></img></a>
<a href="https://android-arsenal.com/details/1/6561" target="_blank"><img src="https://img.shields.io/badge/Android%20Arsenal-AndroidGodEye-brightgreen.svg?style=flat"></img></a>
<a href="LICENSE" target="_blank"><img src="http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat"></img></a>
</p>
<br/>

<p>
<a href="README.md">English README.md</a>&nbsp;&nbsp;&nbsp;
<a href="README_zh.md">中文 README_zh.md</a>
</p>

> Android开发者在性能检测方面的工具一直比较匮乏，仅有的一些工具，比如Android Device Monitor，使用起来也有些繁琐，使用起来对开发者有一定的要求。而线上的App监控更无从谈起。所以需要有一个系统能够提供Debug和Release阶段全方位的监控，更深入地了解对App运行时的状态。

## 概览

![android_godeye_connect](ART/android_god_eye_connect.jpg)

AndroidGodEye是一个可以在PC浏览器中实时监控Android数据指标（比如性能指标，但是不局限于性能）的工具，你可以通过wifi/usb连接手机和pc，通过pc浏览器实时监控手机性能。

系统分为三部分：

1. Core 核心部分，提供所有模块
2. Debug Monitor部分，提供Debug阶段开发者面板
3. Toolbox 快速接入工具集，给开发者提供各种便捷接入的工具

AndroidGodEye提供了多种监控模块，比如cpu、内存、卡顿、内存泄漏等等，并且提供了Debug阶段的Monitor看板实时展示这
些数据。而且提供了api供开发者在release阶段进行数据上报。

## 快速开始

[Demo APK](https://fir.im/5k67)，可以先看看效果 :-)

[Demo Project:https://github.com/Kyson/AndroidGodEyeDemo](https://github.com/Kyson/AndroidGodEyeDemo)

### STEP1 依赖引入

#### Module Project `build.gradle`

```groovy
dependencies {
  implementation 'cn.hikyson.godeye:godeye-core:VERSION_NAME'
  debugImplementation 'cn.hikyson.godeye:godeye-monitor:VERSION_NAME'
  releaseImplementation 'cn.hikyson.godeye:godeye-monitor-no-op:VERSION_NAME'
  implementation 'cn.hikyson.godeye:godeye-toolbox:VERSION_NAME'
}
```

> VERSION_NAME参考 [Github release](https://github.com/Kyson/AndroidGodEye/releases)

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

> PLUGIN_VERSION_NAME参考 [MethodCanary github release](https://github.com/Kyson/MethodCanary/releases)

需要配置MethodCanary的插桩逻辑，在项目根目录下新建js文件：`MethodCanary.js`，内容如下：

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

> 配置说明可以参考[MethodCanary](https://github.com/Kyson/MethodCanary)

#### Module Project `'com.android.application'` `build.gradle`

```groovy
apply plugin: 'cn.hikyson.methodcanary.plugin'
```

### STEP2 初始化并安装所需模块

Application中初始化:

```java
GodEye.instance().init(this);
```

模块安装，GodEye类是AndroidGodEye的核心类，所有模块由它提供。

在需要的时候安装所有模块（建议在`application onCreate`中）：

```java
if (ProcessUtils.isMainProcess(this)) {//安装只能在主进程
    GodEye.instance().install(GodEyeConfig.fromAssets("android-godeye-config/install.config"));
}
```

assets目录下放模块的配置文件`android-godeye-config/install.config`，内容如下：

```xml
<config>
    <battery />
    <cpu intervalMillis="2000" sampleMillis="1000"/>
    <crash crashProvider="cn.hikyson.godeye.core.internal.modules.crash.CrashFileProvider"/>
    <fps intervalMillis="2000"/>
    <heap intervalMillis="2000"/>
    <leakMemory debug="true" debugNotification="true" leakRefInfoProvider="cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider"/>
    <pageload pageInfoProvider="cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider"/>
    <pss intervalMillis="2000"/>
    <ram intervalMillis="2000"/>
    <sm debugNotify="true"
        dumpIntervalMillis="500"
        longBlockThresholdMillis="300"
        shortBlockThresholdMillis="100"/>
    <thread intervalMillis="3000"
            threadFilter="cn.hikyson.godeye.core.internal.modules.thread.SimpleThreadFilter"/>
    <traffic intervalMillis="2000" sampleMillis="1000"/>
    <methodCanary maxMethodCountSingleThreadByCost="300" lowCostMethodThresholdMillis="10"/>
</config>
```

#### 可选部分 卸载模块

不需要的时候卸载模块(不推荐)：

```java
// 卸载已经安装的所有模块
GodEye.instance().uninstall();
```

> 注意：network和startup模块不需要安装和卸载

安装完之后相应的模块就开始输出数据了，一般来说可以使用模块的consume方法进行消费，比如cpu模块：

```java
GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).subject().subscribe()
```

> 就像我们之后会提到的Debug Monitor，也是通过消费这些数据进行展示的

### STEP3 安装性能可视化面板

GodEyeMonitor类是AndroidGodEye的性能可视化面板的主要类，用来开始或者停止性能可视化面板的监控。

开启性能可视化面板，不建议在生产包中开启：

```java
GodEyeMonitor.work(context)
```

关闭面板：

```java
GodEyeMonitor.shutDown()
```

usb连上你的手机，接下来可以开始运行项目了！

### STEP4 安装IDE插件

在AndroidStudio中安装AndroidGodEye插件，在AndroidStudio plugin中直接搜索AndroidGodEye即可，安装完之后会在工具栏中出现AndroidGodEye的icon，点击即可在浏览器中打开性能监控面板。

![https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png](https://github.com/Kyson/AndroidGodEye/blob/master/ART/android-godeye-plugin-position.png)

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

### Base info

![android_godeye_summary](ART/android_god_eye_summary.png)

### 卡顿检测

![android_god_eye_block](ART/android_god_eye_block.gif)

### 内存泄漏检测

![android_god_eye_leak](ART/android_god_eye_leak.gif)

### 更多模块

![android_god_eye_cpuheaptraffic](ART/android_god_eye_cpuheaptraffic.gif)

还有更多...

## 模块详情

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









