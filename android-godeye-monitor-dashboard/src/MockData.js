/**
 * Created by kysonchao on 2019/3/9.
 */

class Mock {

    constructor() {
        this.index = 0;
        this.refreshMock = this.refreshMock.bind(this);
        this.recvFun = null;
    }

    start(recvFun) {
        this.recvFun = recvFun;
        setInterval(this.refreshMock, 2000);
        this.refreshMethodCanary();
    }

    refreshMethodCanary() {
        // this.recvFun("MethodCanaryStatus", {
        //     lowCostMethodThresholdMillis: 10,
        //     maxMethodCountSingleThreadByCost: 200,
        //     isMonitoring: true,
        //     isInstalled: true
        // });

        this.recvFun("methodCanary", {
            start: 10,
            end: 1000,
            methodInfoOfThreadInfos: [
                {
                    threadInfo: {
                        name: "thead1", id: 12, priority: 5
                    },
                    methodInfos: [
                        {
                            stack: 0,
                            start: 400,
                            end: 600,
                            className: "classA",
                            methodName: "methodA",
                            methodDesc: "descA",
                            methodAccessFlag: 1
                        },
                    ]
                },
                {
                    threadInfo: {
                        name: "main", id: 2, priority: 10
                    },
                    methodInfos: [
                        {
                            stack: 2,
                            start: 0,
                            end: 300,
                            className: "0-300",
                            methodName: "methodB",
                            methodDesc: "descB",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 0,
                            start: 0,
                            end: 0,
                            className: "xxx-xxx",
                            methodName: "methodB",
                            methodDesc: "descB",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            start: 720,
                            end: 780,
                            className: "700-780",
                            methodName: "methodD",
                            methodDesc: "descD",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 3,
                            start: 100,
                            end: 200,
                            className: "100-200",
                            methodName: "methodE",
                            methodDesc: "descE",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            start: 400,
                            end: 450,
                            className: "400-450",
                            methodName: "methodD",
                            methodDesc: "descD",
                            methodAccessFlag: 1
                        }, {
                            stack: 1,
                            start: 0,
                            end: 500,
                            className: "0-500",
                            methodName: "methodA",
                            methodDesc: "descA",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            start: 320,
                            end: 350,
                            className: "320-350",
                            methodName: "methodC",
                            methodDesc: "descC",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 1,
                            start: 700,
                            end: 800,
                            className: "700-800",
                            methodName: "methodA",
                            methodDesc: "descA",
                            methodAccessFlag: 1
                        },
                    ]
                }
            ]
        });
    }

    refreshMock() {
        this.index = this.index + 1;
        this.recvFun("appInfo", {
            appName: "I am Name",
            labels: [
                {name: "lablel1", value: "value0000000", url: "http://www.ctrip.com"},
                {name: "lablel2", value: "value1111111", url: "http://www.trip.com"},
                {
                    name: "lablel3lablel3lablel3lablel3lablel3lablel3lablel3", value: "value2222222"
                }, {
                    name: "lablel3lablel3lablel3", value: "value333333333"
                }, {
                    name: "lablel3", value: "value44444444"
                }, {
                    name: "lablel3", value: "value5555555"
                }, {
                    name: "lablel3", value: "value66666"
                }]
        });
        this.recvFun("startupInfo", {
            startupType: "cold",
            startupTime: 1003
        });
        this.recvFun("batteryInfo", {
            level: 24,
            status: "ok",
            plugged: "plugged",
            present: "present",
            health: "health",
            voltage: "voltage",
            temperature: "temperature",
            technology: "technology",
            scale: 100,
        });
        this.recvFun("cpuInfo", {
            totalUseRatio: 0.31,
            appCpuRatio: 0.12,
            userCpuRatio: 0.13,
            sysCpuRatio: 0.09
        });
        this.recvFun("heapInfo", {
            freeMemKb: 1024 * 520,
            allocatedKb: 1024 * 1520,
            maxMemKb: 1024 * 6000
        });
        this.recvFun("ramInfo", {
            totalMemKb: 1024 * 1024 * 3,
            availMemKb: 1024 * 1024 * 1.5
        });
        this.recvFun("pssInfo", {
            totalPssKb: 1024 * 300,
            dalvikPssKb: 1024 * 125,
            nativePssKb: 1024 * 200,
            otherPssKb: 1024 * 7,
        });
        this.recvFun("fpsInfo", {
            currentFps: "32",
            systemFps: "34"
        });
        this.recvFun("pageLifecycle", {
            pageType: "Acivity",
            pageClassName: "pageClassName" + this.index,
            pageHashCode: 10000,

            lifecycleEvent: "ON_LOAD",
            eventTimeMillis: 1469433907836,
            processedInfo: {
                "loadTime": 2342
            }
        });
        this.recvFun("pageLifecycle", {
            pageType: "Acivity",
            pageClassName: "pageClassName" + (this.index * 2),
            pageHashCode: 20000,
            lifecycleEvent: "ON_DRAW",
            eventTimeMillis: 1469433907836,
            processedInfo: {
                "drawTime": 700
            }
        });
        this.recvFun("pageLifecycle", {
            pageType: "Acivity",
            pageClassName: "ClassName3",
            pageHashCode: 12312,
            lifecycleEvent: "ON_CREATE",
            eventTimeMillis: 1469433907836,
        });
        this.recvFun("crashInfo", [
            {
                "Crash time": "1990-12-11 12:21:33.SSSZ",
                "crash_message":"this is a message1",
                "java stacktrace": "at xcrash.NativeHandler.nativeTestCrash(Native method)\n" +
                "at xcrash.NativeHandler.testNativeCrash(NativeHandler.java:156)\n" +
                "at xcrash.XCrash.testNativeCrash(XCrash.java:860)\n" +
                "at cn.hikyson.android.godeye.sample.ToolsFragment.lambda$onCreateView$6(ToolsFragment.java:71)\n" +
                "at cn.hikyson.android.godeye.sample.-$$Lambda$ToolsFragment$nyitYGrD0T4yinuvC2G7kCsG5II.onClick(lambda:-1)\n" +
                "at android.view.View.performClick(View.java:7352)\n" +
                "at android.widget.TextView.performClick(TextView.java:14177)\n" +
                "at android.view.View.performClickInternal(View.java:7318)\n" +
                "at android.view.View.access$3200(View.java:846)\n" +
                "at android.view.View$PerformClick.run(View.java:27807)\n" +
                "at android.os.Handler.handleCallback(Handler.java:873)\n" +
                "at android.os.Handler.dispatchMessage(Handler.java:99)\n" +
                "at android.os.Looper.loop(Looper.java:214)\n" +
                "at android.app.ActivityThread.main(ActivityThread.java:7037)\n" +
                "at java.lang.reflect.Method.invoke(Native method)\n" +
                "at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)",
                "code": "1 (SEGV_MAPERR)",
                "Crash type":"native"
            },
            {
                "Crash time": "1990-12-11 12:21:33.SSSZ",
                "crash_message":"this is a message2",
                "java stacktrace": "at xcrash.NativeHa2342342342method)\n" +
                "at xcrash.NativeHandler.testNativeCrash(NativeHandler.java:156)\n" +
                "at xcrash.XCrash.testNativeCrash(XCrash.java:860)\n" +
                "at cn.hikyson.android.godeye.sample.ToolsFragment.lambda$onCreateView$6(ToolsFragment.java:71)\n" +
                "at cn.hikyson.android.godeye.sample.-$$Lambda$ToolsFragment$nyitYGrD0T4yinuvC2G7kCsG5II.onClick(lambda:-1)\n" +
                "at android.view.View.performClick(View.java:7352)\n" +
                "at android.widget.TextView.performClick(TextView.java:14177)\n" +
                "at android.view.View.performClickInternal(View.java:7318)\n" +
                "at android.view.View.access$3200(View.java:846)\n" +
                "at android.view.View$PerformClick.run(View.java:27807)\n" +
                "at android.os.Handler.handleCallback(Handler.java:873)\n" +
                "at android.os.Handler.dispatchMessage(Handler.java:99)\n" +
                "at android.os.Looper.loop(Looper.java:214)\n" +
                "at android.app.ActivityThread.main(ActivityThread.java:7037)\n" +
                "at java.lang.reflect.Method.invoke(Native method)\n" +
                "at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)",
                "code": "1 (SEGV_MAPERR)",
                "Crash type":"java"
            },
            {
                "Crash time": "1920-12-11 12:21:33.SSSZ",
                "crash_message":"this is a message3",
                "java stacktrace": "at xcrash.4123f34334fveTestCrash(Native method)\n" +
                "at xcrash.NativeHandler.testNativeCrash(NativeHandler.java:156)\n" +
                "at xcrash.XCrash.testNativeCrash(XCrash.java:860)\n" +
                "at cn.hikyson.android.godeye.sample.ToolsFragment.lambda$onCreateView$6(ToolsFragment.java:71)\n" +
                "at cn.hikyson.android.godeye.sample.-$$Lambda$ToolsFragment$nyitYGrD0T4yinuvC2G7kCsG5II.onClick(lambda:-1)\n" +
                "at android.view.View.performClick(View.java:7352)\n" +
                "at android.widget.TextView.performClick(TextView.java:14177)\n" +
                "at android.view.View.performClickInternal(View.java:7318)\n" +
                "at android.view.View.access$3200(View.java:846)\n" +
                "at android.view.View$PerformClick.run(View.java:27807)\n" +
                "at android.os.Handler.handleCallback(Handler.java:873)\n" +
                "at android.os.Handler.dispatchMessage(Handler.java:99)\n" +
                "at android.os.Looper.loop(Looper.java:214)\n" +
                "at android.app.ActivityThread.main(ActivityThread.java:7037)\n" +
                "at java.lang.reflect.Method.invoke(Native method)\n" +
                "at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)",
                "code": "1 (SEGV_MAPERR)",
                "Crash type":"java"
            }

        ]);
        this.recvFun("blockInfo", {
            blockTime: 200,
            blockBaseinfo: {df: "sdf", vvv: "1312", bb: ["fewefwf", "fwewfe"]}
        });
        this.recvFun("blockInfo", {
            blockTime: 300,
            blockBaseinfo: {ss: "111", dd: "333", aa: ["11", "22"]}
        });
        this.recvFun("reinstallBlock", {
            longBlockThresholdMillis: 100,
            shortBlockThresholdMillis: 100,
            dumpIntervalMillis: 1000,
            debugNotify: true
        });

        this.recvFun("networkInfo", {
            summary: "POST www.trip.com" + this.index,
            isSuccessful: true,
            message: "OK",
            totalTime: 1200,
            networkTime: [
                {name: "DNS", time: 200},
                {name: "RequestHeader", time: 300},
                {name: "ResponseBody", time: 500},

            ],
            networkContent: {
                networkType: "Http",
                requestContent: "this is requestContent",
                responseContent: "this is responseContent",
            },
            extraInfo: {
                key1: "value1",
                key2: "value2"
            }
        });
        this.recvFun("trafficInfo", {
            rxTotalRate: 56,
            txTotalRate: 48,
            rxUidRate: 34,
            txUidRate: 42
        });
        this.recvFun("appSizeInfo", {
            cacheSize: 16,
            dataSize: 238,
            codeSize: 114
        });
        this.recvFun("viewIssueInfo", {"activityName":"cn.hikyson.android.godeye.sample.LeakActivity","maxDepth":10,"overDrawAreas":[{"overDrawTimes":2,"rect":{"bottom":508,"left":0,"right":963,"top":332}},{"overDrawTimes":1,"rect":{"bottom":2340,"left":0,"right":1080,"top":228}},{"overDrawTimes":1,"rect":{"bottom":96,"left":0,"right":1080,"top":0}},{"overDrawTimes":2,"rect":{"bottom":206,"left":33,"right":121,"top":118}},{"overDrawTimes":1,"rect":{"bottom":228,"left":0,"right":1080,"top":96}}],"screenHeight":2210,"screenWidth":1080,"timestamp":1572861420958,"views":[{"className":"android.widget.TextView","depth":4,"hasBackground":true,"id":"2131230746","isViewGroup":false,"rect":{"bottom":332,"left":0,"right":1080,"top":228},"text":"Leak will happen when you finish this activity and wait for a moment.","textOverDrawTimes":2,"textSize":39.0},{"className":"android.widget.Button","depth":4,"hasBackground":true,"id":"2131230819","isViewGroup":false,"rect":{"bottom":508,"left":0,"right":963,"top":332},"text":"Leak fragment (Android O and above)","textOverDrawTimes":3,"textSize":50.0},{"className":"android.widget.TextView","depth":6,"hasBackground":true,"id":"16908688","isViewGroup":false,"rect":{"bottom":196,"left":132,"right":502,"top":128},"text":"AndroidGodEye","textOverDrawTimes":2,"textSize":50.0},{"className":"android.widget.ImageView","depth":6,"hasBackground":true,"id":"16908332","isViewGroup":false,"rect":{"bottom":206,"left":33,"right":121,"top":118},"textOverDrawTimes":0,"textSize":0.0},{"className":"android.widget.LinearLayout","depth":3,"hasBackground":true,"id":"-1","isViewGroup":true,"rect":{"bottom":2340,"left":0,"right":1080,"top":228},"textOverDrawTimes":0,"textSize":0.0},{"className":"android.view.View","depth":1,"hasBackground":true,"id":"16908335","isViewGroup":false,"rect":{"bottom":96,"left":0,"right":1080,"top":0},"textOverDrawTimes":0,"textSize":0.0},{"className":"com.android.internal.widget.ActionBarContainer","depth":2,"hasBackground":true,"id":"16908685","isViewGroup":true,"rect":{"bottom":228,"left":0,"right":1080,"top":96},"textOverDrawTimes":0,"textSize":0.0}]});
        this.recvFun("leakInfo", {
            referenceKey: "referenceKey",
            leakTime: "leakTime",
            leakObjectName: "leakObjectName",
            statusSummary: "statusSummary",
            pathToGcRoot: ["leakStack", "leakStack", "leakStack", "leakStack", "leakStack"]
        });
        this.recvFun("threadInfo", [
            {
                id: 1,
                name: "name",
                state: "state",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
                threadRunningProcess: "UNKNOWN"
            },
            {
                id: 1,
                name: "name",
                state: "state",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
                threadRunningProcess: "APP"
            }, {
                id: 1,
                name: "name",
                state: "state",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
                threadRunningProcess: "SYSTEM"
            }
        ]);
    }
}


export default Mock;
