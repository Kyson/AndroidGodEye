/**
 * Created by kysonchao on 2019/3/9.
 */

class Mock {

    constructor() {
        this.pageId = 0;
        this.refreshMock = this.refreshMock.bind(this);
        this.recvFun = null;
    }

    start(recvFun) {
        this.recvFun = recvFun;
        setInterval(this.refreshMock, 2000);
    }

    refreshMock() {
        // this.pageId = this.pageId + 1;
        // this.recvFun("appInfo", {
        //     appName: "I am Name",
        //     labels: [
        //         {name: "lablel1", url: "http://www.ctrip.com"},
        //         {name: "lablel2", url: "http://www.trip.com"},
        //         {
        //             name: "lablel3"
        //         }, {
        //             name: "lablel3"
        //         }, {
        //             name: "lablel3"
        //         }, {
        //             name: "lablel3"
        //         }]
        // });
        // this.recvFun("startupInfo", {
        //     startupType: "cold",
        //     startupTime: 1003
        // });
        // this.recvFun("batteryInfo", {
        //     level: 24,
        //     status: "ok",
        //     plugged: "plugged",
        //     present: "present",
        //     health: "health",
        //     voltage: "voltage",
        //     temperature: "temperature",
        //     technology: "technology",
        //     scale: 100,
        // });
        // this.recvFun("cpuInfo", {
        //     totalUseRatio: 0.91,
        //     appCpuRatio: 0.12,
        //     userCpuRatio: 0.23,
        //     sysCpuRatio: 0.09
        // });
        // this.recvFun("heapInfo", {
        //     freeMemKb: 1024 * 520,
        //     allocatedKb: 1024 * 1520,
        //     maxMemKb: 1024 * 6000
        // });
        // this.recvFun("ramInfo", {
        //     totalMemKb: 1024 * 1024 * 3,
        //     availMemKb: 1024 * 1024 * 1.5
        // });
        // this.recvFun("pssInfo", {
        //     totalPssKb: 1024 * 300,
        //     dalvikPssKb: 1024 * 125,
        //     nativePssKb: 1024 * 200,
        //     otherPssKb: 1024 * 7,
        // });
        // this.recvFun("fpsInfo", {
        //     currentFps: "32",
        //     systemFps: "34"
        // });
        this.recvFun("pageLifecycle", {
            pageInfo: {
                pageType: "Acivity",
                pageClassName: "ClassName1",
                pageHashCode: 10000,
                extraInfo: {
                    "A1": "1111",
                    "B1": 1111
                }
            },
            pageLifecycleEventWithTime: {
                lifecycleEvent: "ON_LOAD",
                eventTimeMillis: 1469433907836,
            },
            processedInfo: {
                "loadTime": 2342
            }
        });
        this.recvFun("pageLifecycle", {
            pageInfo: {
                pageType: "Acivity",
                pageClassName: "ClassName2",
                pageHashCode: 20000,
                extraInfo: {
                    "A2": "2222",
                    "B2": 2222
                }
            },
            pageLifecycleEventWithTime: {
                lifecycleEvent: "ON_DRAW",
                eventTimeMillis: 1469433907836,
            },
            processedInfo: {
                "drawTime": 312
            }
        });
        // this.recvFun("crashInfo", {
        //     timestampMillis: new Date().getMilliseconds(),
        //     throwableMessage: "throwableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessage",
        //     throwableStacktrace: ["1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111"]
        // });
        // this.recvFun("blockInfo", {
        //     blockTime: 200,
        //     blockBaseinfo: {df: "sdf", vvv: "1312", bb: ["fewefwf", "fwewfe"]}
        // });
        // this.recvFun("blockInfo", {
        //     blockTime: 300,
        //     blockBaseinfo: {ss: "111", dd: "333", aa: ["11", "22"]}
        // });
        // this.recvFun("networkInfo", {
        //     networkInfoConnection: {
        //         cipherSuite: "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
        //         localIp: "10.0.2.15",
        //         localPort: 53010,
        //         protocol: "h2",
        //         remoteIp: "117.184.207.147",
        //         remotePort: 443,
        //         tlsVersion: "TLSv1.2"
        //     },
        //     networkInfoRequest: {
        //         method: "https://www.trip.com/",
        //         url: "GET"
        //     },
        //     networkSimplePerformance: {
        //         connectTimeMillis: 1345,
        //         dnsTimeMillis: 41,
        //         receiveBodyTimeMillis: 389,
        //         receiveHeaderTimeMillis: 713,
        //         sendBodyTimeMillis: 0,
        //         sendHeaderTimeMillis: 11,
        //         totalTimeMillis: 2527
        //     },
        //     requestBodySizeByte: 0,
        //     requestId: "117910124:https://www.trip.com/",
        //     responseBodySizeByte: 14133,
        //     resultCode: "200"
        // });
        // this.recvFun("networkInfo", {
        //     networkInfoConnection: {
        //         cipherSuite: "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
        //         localIp: "10.0.2.15",
        //         localPort: 53010,
        //         protocol: "h2",
        //         remoteIp: "117.184.207.147",
        //         remotePort: 443,
        //         tlsVersion: "TLSv1.2"
        //     },
        //     networkInfoRequest: {
        //         method: "https://www.trip.com/",
        //         url: "GET"
        //     },
        //     networkSimplePerformance: {
        //         connectTimeMillis: 1000,
        //         dnsTimeMillis: 80,
        //         receiveBodyTimeMillis: 300,
        //         receiveHeaderTimeMillis: 400,
        //         sendBodyTimeMillis: 100,
        //         sendHeaderTimeMillis: 100,
        //         totalTimeMillis: 3213
        //     },
        //     requestBodySizeByte: 1222,
        //     requestId: "22222222:https://www.baidu.com/",
        //     responseBodySizeByte: 10000,
        //     resultCode: "no connection"
        // });
        // this.recvFun("trafficInfo", {
        //     rxTotalRate: 56,
        //     txTotalRate: 48,
        //     rxUidRate: 34,
        //     txUidRate: 42
        // });
        // this.recvFun("leakInfo", {
        //     referenceKey: "referenceKey",
        //     leakTime: "leakTime",
        //     leakObjectName: "leakObjectName",
        //     statusSummary: "statusSummary",
        //     pathToGcRoot: ["leakStack", "leakStack", "leakStack", "leakStack", "leakStack"]
        // });
        // this.recvFun("threadInfo", [
        //     {
        //         id: 1,
        //         name: "name",
        //         state: "state",
        //         deadlock: "deadlock",
        //         priority: "priority",
        //         deamon: "deamon",
        //         isAlive: "isAlive",
        //         isInterrupted: "isInterrupted",
        //     },
        //     {
        //         id: 1,
        //         name: "name",
        //         state: "state",
        //         deadlock: "deadlock",
        //         priority: "priority",
        //         deamon: "deamon",
        //         isAlive: "isAlive",
        //         isInterrupted: "isInterrupted",
        //     }, {
        //         id: 1,
        //         name: "name",
        //         state: "state",
        //         deadlock: "deadlock",
        //         priority: "priority",
        //         deamon: "deamon",
        //         isAlive: "isAlive",
        //         isInterrupted: "isInterrupted",
        //     }
        // ]);
        //
        // this.recvFun("MethodCanaryStatus", {
        //     lowCostMethodThresholdMillis: 10,
        //     maxMethodCountSingleThreadByCost: 200,
        //     isMonitoring: true,
        //     isInstalled: true
        // });
        //
        //
        // this.recvFun("methodCanary", {
        //     start: 80,
        //     end: 200,
        //     methodInfoOfThreadInfos: [
        //         {
        //             threadInfo: {
        //                 name: "thead1", id: 12, priority: 5
        //             },
        //             methodInfos: [
        //                 {
        //                     stack: 0,
        //                     start: 100,
        //                     end: 102,
        //                     className: "classA",
        //                     methodName: "methodA",
        //                     methodDesc: "descA",
        //                     methodAccessFlag: 1
        //                 },
        //             ]
        //         },
        //         {
        //             threadInfo: {
        //                 name: "main", id: 2, priority: 10
        //             },
        //             methodInfos: [
        //                 {
        //                     stack: 0,
        //                     start: 90,
        //                     end: 107,
        //                     className: "classA",
        //                     methodName: "methodA",
        //                     methodDesc: "descA",
        //                     methodAccessFlag: 1
        //                 },
        //                 {
        //                     stack: 1,
        //                     start: 108,
        //                     end: 124,
        //                     className: "classB",
        //                     methodName: "methodB",
        //                     methodDesc: "descB",
        //                     methodAccessFlag: 1
        //                 },
        //                 {
        //                     stack: 2,
        //                     start: 103,
        //                     end: 104,
        //                     className: "classC",
        //                     methodName: "methodC",
        //                     methodDesc: "descC",
        //                     methodAccessFlag: 1
        //                 },
        //                 {
        //                     stack: 2,
        //                     start: 106,
        //                     end: 120,
        //                     className: "classD",
        //                     methodName: "methodD",
        //                     methodDesc: "descD",
        //                     methodAccessFlag: 1
        //                 },
        //                 {
        //                     stack: 3,
        //                     start: 107,
        //                     end: 108,
        //                     className: "classE",
        //                     methodName: "methodE",
        //                     methodDesc: "descE",
        //                     methodAccessFlag: 1
        //                 },
        //             ]
        //         }
        //     ]
        // });
    }

}


export default Mock;
