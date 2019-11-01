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
        this.recvFun("crashInfo", {
            timestampMillis: new Date().getMilliseconds(),
            throwableMessage: "throwableMessagethrowableMessagethrowableMessagethrowab",
            throwableStacktrace: ["1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111"]
        });
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
        this.recvFun("viewIssueInfo", {"activityName":"com.ctrip.ibu.train.module.TrainMainActivity","overDrawAreas":[{"rect":{"bottom":1466,"left":107,"right":263,"top":1412},"times":3},{"rect":{"bottom":1210,"left":970,"right":1036,"top":1186},"times":2},{"rect":{"bottom":1210,"left":970,"right":1080,"top":1100},"times":4},{"rect":{"bottom":250,"left":198,"right":275,"top":209},"times":1},{"rect":{"bottom":732,"left":487,"right":592,"top":625},"times":4},{"rect":{"bottom":1010,"left":567,"right":1036,"top":1009},"times":4},{"rect":{"bottom":1461,"left":44,"right":88,"top":1416},"times":3},{"rect":{"bottom":2448,"left":152,"right":383,"top":2403},"times":3},{"rect":{"bottom":662,"left":964,"right":1036,"top":612},"times":4},{"rect":{"bottom":2099,"left":44,"right":113,"top":2030},"times":3},{"rect":{"bottom":295,"left":286,"right":303,"top":278},"times":3},{"rect":{"bottom":1612,"left":44,"right":99,"top":1556},"times":4},{"rect":{"bottom":1776,"left":88,"right":488,"top":1708},"times":6},{"rect":{"bottom":250,"left":0,"right":1080,"top":96},"times":3},{"rect":{"bottom":2294,"left":152,"right":548,"top":2249},"times":3},{"rect":{"bottom":1373,"left":0,"right":1080,"top":380},"times":3},{"rect":{"bottom":706,"left":510,"right":568,"top":650},"times":5},{"rect":{"bottom":96,"left":0,"right":1080,"top":0},"times":3},{"rect":{"bottom":883,"left":44,"right":188,"top":833},"times":4},{"rect":{"bottom":523,"left":44,"right":816,"top":447},"times":4},{"rect":{"bottom":744,"left":926,"right":1036,"top":668},"times":4},{"rect":{"bottom":1318,"left":44,"right":1036,"top":1186},"times":4},{"rect":{"bottom":250,"left":154,"right":275,"top":209},"times":3},{"rect":{"bottom":2407,"left":44,"right":113,"top":2338},"times":3},{"rect":{"bottom":250,"left":44,"right":275,"top":209},"times":2},{"rect":{"bottom":1961,"left":0,"right":1080,"top":1510},"times":3},{"rect":{"bottom":380,"left":0,"right":1080,"top":0},"times":2},{"rect":{"bottom":48,"left":0,"right":1080,"top":15},"times":1},{"rect":{"bottom":662,"left":44,"right":116,"top":612},"times":4},{"rect":{"bottom":2392,"left":152,"right":386,"top":2338},"times":3},{"rect":{"bottom":538,"left":1051,"right":1080,"top":462},"times":4},{"rect":{"bottom":789,"left":44,"right":1036,"top":788},"times":4},{"rect":{"bottom":441,"left":44,"right":1036,"top":391},"times":4},{"rect":{"bottom":250,"left":154,"right":492,"top":96},"times":4},{"rect":{"bottom":1010,"left":44,"right":512,"top":1009},"times":4},{"rect":{"bottom":965,"left":567,"right":1036,"top":889},"times":4},{"rect":{"bottom":1856,"left":88,"right":212,"top":1794},"times":6},{"rect":{"bottom":1917,"left":44,"right":1036,"top":1647},"times":5},{"rect":{"bottom":1850,"left":114,"right":186,"top":1800},"times":7},{"rect":{"bottom":380,"left":0,"right":1080,"top":347},"times":2},{"rect":{"bottom":1614,"left":121,"right":209,"top":1554},"times":4},{"rect":{"bottom":1115,"left":110,"right":397,"top":1059},"times":4},{"rect":{"bottom":744,"left":44,"right":154,"top":668},"times":4},{"rect":{"bottom":568,"left":44,"right":1036,"top":567},"times":4},{"rect":{"bottom":2470,"left":0,"right":1080,"top":380},"times":2},{"rect":{"bottom":1373,"left":44,"right":1036,"top":1318},"times":4},{"rect":{"bottom":2253,"left":44,"right":113,"top":2184},"times":3},{"rect":{"bottom":2238,"left":152,"right":581,"top":2184},"times":3},{"rect":{"bottom":2084,"left":152,"right":620,"top":2030},"times":3},{"rect":{"bottom":965,"left":44,"right":512,"top":889},"times":4},{"rect":{"bottom":2140,"left":152,"right":449,"top":2095},"times":3},{"rect":{"bottom":883,"left":567,"right":711,"top":833},"times":4},{"rect":{"bottom":314,"left":44,"right":275,"top":209},"times":3},{"rect":{"bottom":451,"left":44,"right":816,"top":447},"times":1},{"rect":{"bottom":250,"left":536,"right":1080,"top":96},"times":4}],"statusBarHeight":96,"timestamp":1572599954584,"views":[{"className":"com.google.android.material.appbar.AppBarLayout","depth":2,"hasBackground":true,"id":"2131296539","isViewGroup":true,"rect":{"bottom":380,"left":0,"right":1080,"top":0},"textSize":0.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":10,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":1917,"left":44,"right":1036,"top":1647},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299709","isViewGroup":false,"rect":{"bottom":2294,"left":152,"right":548,"top":2249},"text":"隨時隨地輕鬆管理您的訂單","textSize":33.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302880","isViewGroup":false,"rect":{"bottom":662,"left":964,"right":1036,"top":612},"text":"到達","textSize":36.0},{"className":"com.ctrip.ibu.train.widget.TrainIconFontView","depth":9,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":1612,"left":44,"right":99,"top":1556},"text":"","textSize":55.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302780","isViewGroup":false,"rect":{"bottom":662,"left":44,"right":116,"top":612},"text":"出發","textSize":36.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":9,"hasBackground":false,"id":"2131299705","isViewGroup":false,"rect":{"bottom":2099,"left":44,"right":113,"top":2030},"textSize":0.0},{"className":"android.view.View","depth":4,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":380,"left":0,"right":1080,"top":347},"textSize":0.0},{"className":"android.widget.TextView","depth":5,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":250,"left":154,"right":492,"top":96},"text":"火車票搜索","textSize":50.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":9,"hasBackground":false,"id":"2131299707","isViewGroup":false,"rect":{"bottom":2407,"left":44,"right":113,"top":2338},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":10,"hasBackground":false,"id":"2131302559","isViewGroup":false,"rect":{"bottom":523,"left":44,"right":816,"top":447},"text":"中國","textSize":55.0},{"className":"android.view.View","depth":4,"hasBackground":true,"id":"2131302192","isViewGroup":false,"rect":{"bottom":96,"left":0,"right":1080,"top":0},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":10,"hasBackground":false,"id":"2131302562","isViewGroup":false,"rect":{"bottom":441,"left":44,"right":1036,"top":391},"text":"國家或地區","textSize":36.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":10,"hasBackground":true,"id":"2131304184","isViewGroup":false,"rect":{"bottom":1318,"left":44,"right":1036,"top":1186},"text":"搜尋","textSize":44.0},{"className":"android.widget.ScrollView","depth":5,"hasBackground":true,"id":"2131302022","isViewGroup":true,"rect":{"bottom":2591,"left":0,"right":1080,"top":380},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131304139","isViewGroup":false,"rect":{"bottom":883,"left":567,"right":711,"top":833},"text":"回程日期","textSize":36.0},{"className":"android.view.View","depth":10,"hasBackground":true,"id":"2131305157","isViewGroup":false,"rect":{"bottom":568,"left":44,"right":1036,"top":567},"textSize":0.0},{"className":"android.view.View","depth":12,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":1010,"left":567,"right":1036,"top":1009},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":11,"hasBackground":false,"id":"2131303569","isViewGroup":false,"rect":{"bottom":1115,"left":110,"right":397,"top":1059},"text":"只選擇高速列車","textSize":41.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302721","isViewGroup":false,"rect":{"bottom":744,"left":926,"right":1036,"top":668},"text":"北京","textSize":55.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299710","isViewGroup":false,"rect":{"bottom":2448,"left":152,"right":383,"top":2403},"text":"免卻排隊慳時間","textSize":33.0},{"className":"com.ctrip.ibu.train.widget.TrainNewToolbar","depth":4,"hasBackground":true,"id":"2131302598","isViewGroup":true,"rect":{"bottom":250,"left":0,"right":1080,"top":96},"textSize":0.0},{"className":"com.ctrip.ibu.framework.baseview.widget.IBUCheckBox","depth":11,"hasBackground":true,"id":"2131302242","isViewGroup":false,"rect":{"bottom":1135,"left":26,"right":114,"top":1047},"text":"","textSize":39.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131303656","isViewGroup":false,"rect":{"bottom":1466,"left":107,"right":263,"top":1412},"text":"取票指南","textSize":39.0},{"className":"android.view.View","depth":5,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":250,"left":536,"right":1080,"top":96},"textSize":0.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":9,"hasBackground":false,"id":"2131299706","isViewGroup":false,"rect":{"bottom":2253,"left":44,"right":113,"top":2184},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299712","isViewGroup":false,"rect":{"bottom":2238,"left":152,"right":581,"top":2184},"text":"支持網上更改車票及退款","textSize":39.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302761","isViewGroup":false,"rect":{"bottom":744,"left":44,"right":154,"top":668},"text":"上海","textSize":55.0},{"className":"android.widget.RelativeLayout","depth":9,"hasBackground":true,"id":"-1","isViewGroup":true,"rect":{"bottom":1917,"left":44,"right":1036,"top":1647},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":1850,"left":114,"right":186,"top":1800},"text":"詳情","textSize":36.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":5,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":314,"left":44,"right":275,"top":209},"text":"火車票","textSize":77.0},{"className":"android.view.View","depth":11,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":789,"left":44,"right":1036,"top":788},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainIconFontView","depth":9,"hasBackground":false,"id":"2131299158","isViewGroup":false,"rect":{"bottom":1461,"left":44,"right":88,"top":1416},"text":"","textSize":44.0},{"className":"android.view.View","depth":5,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":295,"left":286,"right":303,"top":278},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainIconFontView","depth":12,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":706,"left":510,"right":568,"top":650},"text":"","textSize":55.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302854","isViewGroup":false,"rect":{"bottom":965,"left":567,"right":1036,"top":889},"text":"日期","textSize":55.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":3,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":1210,"left":970,"right":1080,"top":1100},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131304137","isViewGroup":false,"rect":{"bottom":965,"left":567,"right":567,"top":889},"text":"","textSize":55.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"-1","isViewGroup":false,"rect":{"bottom":1614,"left":121,"right":209,"top":1554},"text":"指南","textSize":44.0},{"className":"android.widget.LinearLayout","depth":8,"hasBackground":true,"id":"-1","isViewGroup":true,"rect":{"bottom":1373,"left":0,"right":1080,"top":380},"textSize":0.0},{"className":"android.view.View","depth":12,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":1010,"left":44,"right":512,"top":1009},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302759","isViewGroup":false,"rect":{"bottom":965,"left":44,"right":512,"top":889},"text":"日期","textSize":55.0},{"className":"androidx.appcompat.widget.AppCompatImageView","depth":4,"hasBackground":false,"id":"2131296562","isViewGroup":false,"rect":{"bottom":380,"left":0,"right":1080,"top":0},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299713","isViewGroup":false,"rect":{"bottom":2392,"left":152,"right":386,"top":2338},"text":"支持車票送遞","textSize":39.0},{"className":"android.view.View","depth":9,"hasBackground":false,"id":"2131305156","isViewGroup":false,"rect":{"bottom":1373,"left":44,"right":1036,"top":1318},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainIconFontView","depth":12,"hasBackground":false,"id":"2131299537","isViewGroup":false,"rect":{"bottom":732,"left":487,"right":592,"top":625},"text":"","textSize":105.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":11,"hasBackground":false,"id":"2131304972","isViewGroup":false,"rect":{"bottom":1776,"left":88,"right":488,"top":1708},"text":"中國鐵路旅遊指南","textSize":50.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131304356","isViewGroup":false,"rect":{"bottom":883,"left":44,"right":188,"top":833},"text":"出發日期","textSize":36.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299708","isViewGroup":false,"rect":{"bottom":2140,"left":152,"right":449,"top":2095},"text":"值得信賴的網上平台","textSize":33.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":9,"hasBackground":false,"id":"2131299711","isViewGroup":false,"rect":{"bottom":2084,"left":152,"right":620,"top":2030},"text":"中國領先網上旅遊預訂平台","textSize":39.0},{"className":"android.widget.LinearLayout","depth":11,"hasBackground":true,"id":"-1","isViewGroup":true,"rect":{"bottom":1856,"left":88,"right":212,"top":1794},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainIconFontView","depth":10,"hasBackground":false,"id":"2131302560","isViewGroup":false,"rect":{"bottom":538,"left":1051,"right":1098,"top":462},"text":"","textSize":47.0},{"className":"androidx.appcompat.widget.AppCompatImageButton","depth":5,"hasBackground":true,"id":"-1","isViewGroup":false,"rect":{"bottom":250,"left":0,"right":154,"top":96},"textSize":0.0},{"className":"androidx.core.widget.NestedScrollView","depth":2,"hasBackground":true,"id":"-1","isViewGroup":true,"rect":{"bottom":2470,"left":0,"right":1080,"top":380},"textSize":0.0},{"className":"com.ctrip.ibu.train.widget.TrainI18nTextView","depth":12,"hasBackground":false,"id":"2131302758","isViewGroup":false,"rect":{"bottom":965,"left":44,"right":44,"top":889},"text":"","textSize":55.0},{"className":"android.widget.LinearLayout","depth":7,"hasBackground":true,"id":"2131298093","isViewGroup":true,"rect":{"bottom":1961,"left":0,"right":1080,"top":1510},"textSize":0.0}]});
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
