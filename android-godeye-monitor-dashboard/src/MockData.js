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
        this.recvFun("installedModuleConfigs", {
            METHOD_CANARY: {
                maxMethodCountSingleThreadByCost: 101,
                lowCostMethodThresholdMillis: 13
            },
            SM: {
                longBlockThresholdMillis: 100,
                shortBlockThresholdMillis: 100,
                dumpIntervalMillis: 1000,
                debugNotify: true
            }
        });
        // this.recvFun("methodCanary",{
        //     "methodInfoOfThreadInfos":[
        //         {
        //             "methodInfos":[
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment",
        //                     "methodDesc":"()V",
        //                     "methodName":"lambda$makeInvocations$12",
        //                     "methodAccessFlag":10,
        //                     "endMillis":1583501823402,
        //                     "startMillis":1583501818248,
        //                     "stack":0
        //                 }
        //             ],
        //             "threadInfo":{
        //                 "name":"Thread-376",
        //                 "priority":5,
        //                 "id":376
        //             }
        //         },
        //         {
        //             "methodInfos":[
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"lambda$makeInvocations$13",
        //                     "methodAccessFlag":10,
        //                     "endMillis":0,
        //                     "startMillis":1583501818250,
        //                     "stack":0
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501818855,
        //                     "startMillis":1583501818250,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818855,
        //                     "startMillis":1583501818250,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818452,
        //                     "startMillis":1583501818250,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818653,
        //                     "startMillis":1583501818452,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818855,
        //                     "startMillis":1583501818654,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501819459,
        //                     "startMillis":1583501818855,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819459,
        //                     "startMillis":1583501818855,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819056,
        //                     "startMillis":1583501818855,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819256,
        //                     "startMillis":1583501819056,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819458,
        //                     "startMillis":1583501819257,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501820065,
        //                     "startMillis":1583501819459,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820065,
        //                     "startMillis":1583501819459,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819661,
        //                     "startMillis":1583501819459,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501819862,
        //                     "startMillis":1583501819661,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820064,
        //                     "startMillis":1583501819862,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501820673,
        //                     "startMillis":1583501820065,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820672,
        //                     "startMillis":1583501820065,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820267,
        //                     "startMillis":1583501820065,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820469,
        //                     "startMillis":1583501820268,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820672,
        //                     "startMillis":1583501820469,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501821280,
        //                     "startMillis":1583501820673,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821280,
        //                     "startMillis":1583501820673,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501820876,
        //                     "startMillis":1583501820673,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821077,
        //                     "startMillis":1583501820876,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821280,
        //                     "startMillis":1583501821077,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501821885,
        //                     "startMillis":1583501821280,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821885,
        //                     "startMillis":1583501821280,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821481,
        //                     "startMillis":1583501821280,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821683,
        //                     "startMillis":1583501821481,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501821884,
        //                     "startMillis":1583501821683,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501822490,
        //                     "startMillis":1583501821885,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822490,
        //                     "startMillis":1583501821885,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822086,
        //                     "startMillis":1583501821885,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822288,
        //                     "startMillis":1583501822086,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822489,
        //                     "startMillis":1583501822289,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501823096,
        //                     "startMillis":1583501822490,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823096,
        //                     "startMillis":1583501822490,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822691,
        //                     "startMillis":1583501822490,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501822893,
        //                     "startMillis":1583501822691,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823095,
        //                     "startMillis":1583501822893,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501823700,
        //                     "startMillis":1583501823096,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823700,
        //                     "startMillis":1583501823096,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823298,
        //                     "startMillis":1583501823096,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823499,
        //                     "startMillis":1583501823298,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823700,
        //                     "startMillis":1583501823499,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501824305,
        //                     "startMillis":1583501823701,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824305,
        //                     "startMillis":1583501823701,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501823902,
        //                     "startMillis":1583501823701,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824103,
        //                     "startMillis":1583501823902,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824304,
        //                     "startMillis":1583501824103,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501824912,
        //                     "startMillis":1583501824305,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824912,
        //                     "startMillis":1583501824305,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824506,
        //                     "startMillis":1583501824305,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824708,
        //                     "startMillis":1583501824506,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501824911,
        //                     "startMillis":1583501824708,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501825517,
        //                     "startMillis":1583501824912,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825517,
        //                     "startMillis":1583501824912,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825114,
        //                     "startMillis":1583501824912,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825314,
        //                     "startMillis":1583501825114,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825516,
        //                     "startMillis":1583501825314,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501826123,
        //                     "startMillis":1583501825517,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826123,
        //                     "startMillis":1583501825517,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825719,
        //                     "startMillis":1583501825517,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501825921,
        //                     "startMillis":1583501825720,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826122,
        //                     "startMillis":1583501825921,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501826731,
        //                     "startMillis":1583501826123,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826731,
        //                     "startMillis":1583501826123,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826325,
        //                     "startMillis":1583501826123,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826527,
        //                     "startMillis":1583501826325,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826730,
        //                     "startMillis":1583501826527,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501827338,
        //                     "startMillis":1583501826731,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827338,
        //                     "startMillis":1583501826731,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501826933,
        //                     "startMillis":1583501826731,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827134,
        //                     "startMillis":1583501826933,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827337,
        //                     "startMillis":1583501827134,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501827943,
        //                     "startMillis":1583501827338,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827943,
        //                     "startMillis":1583501827338,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827540,
        //                     "startMillis":1583501827338,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827741,
        //                     "startMillis":1583501827540,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501827942,
        //                     "startMillis":1583501827741,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501828553,
        //                     "startMillis":1583501827943,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828553,
        //                     "startMillis":1583501827943,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828145,
        //                     "startMillis":1583501827943,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828347,
        //                     "startMillis":1583501828145,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828550,
        //                     "startMillis":1583501828348,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":1583501829158,
        //                     "startMillis":1583501828553,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501829158,
        //                     "startMillis":1583501828553,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828755,
        //                     "startMillis":1583501828553,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501828956,
        //                     "startMillis":1583501828755,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501829158,
        //                     "startMillis":1583501828956,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"(Lcn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest;)V",
        //                     "methodName":"access$200",
        //                     "methodAccessFlag":8,
        //                     "endMillis":0,
        //                     "startMillis":1583501829158,
        //                     "stack":1
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodA",
        //                     "methodAccessFlag":2,
        //                     "endMillis":0,
        //                     "startMillis":1583501829158,
        //                     "stack":2
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501829359,
        //                     "startMillis":1583501829158,
        //                     "stack":3
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment$MethodCanaryTest",
        //                     "methodDesc":"()V",
        //                     "methodName":"methodB",
        //                     "methodAccessFlag":2,
        //                     "endMillis":0,
        //                     "startMillis":1583501829359,
        //                     "stack":3
        //                 }
        //             ],
        //             "threadInfo":{
        //                 "name":"Thread-377",
        //                 "priority":5,
        //                 "id":377
        //             }
        //         },
        //         {
        //             "methodInfos":[
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment",
        //                     "methodDesc":"(Landroid/view/View;)V",
        //                     "methodName":"lambda$onCreateView$4",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818895,
        //                     "startMillis":1583501818241,
        //                     "stack":0
        //                 },
        //                 {
        //                     "className":"cn/hikyson/godeye/sample/ToolsFragment",
        //                     "methodDesc":"()V",
        //                     "methodName":"makeInvocations",
        //                     "methodAccessFlag":2,
        //                     "endMillis":1583501818895,
        //                     "startMillis":1583501818242,
        //                     "stack":1
        //                 }
        //             ],
        //             "threadInfo":{
        //                 "name":"main",
        //                 "priority":5,
        //                 "id":1
        //             }
        //         }
        //     ],
        //     "endMillis":1583501829434,
        //     "startMillis":1583501812776
        // })


        this.recvFun("methodCanary", {
            startMillis: 10,
            endMillis: 790,
            methodInfoOfThreadInfos: [
                {
                    threadInfo: {
                        name: "thead1", id: 12, priority: 5
                    },
                    methodInfos: [
                        {
                            stack: 0,
                            startMillis: 400,
                            endMillis: 600,
                            className: "fasd/fsdfsd/fsdf/classA",
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
                            startMillis: 0,
                            endMillis: 300,
                            className: "/eee/aaa/bbb/0-300",
                            methodName: "methodB",
                            methodDesc: "descB",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 0,
                            startMillis: 0,
                            endMillis: 0,
                            className: "xxx-xxx",
                            methodName: "methodB",
                            methodDesc: "descB",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            startMillis: 720,
                            endMillis: 780,
                            className: "700-780",
                            methodName: "methodD",
                            methodDesc: "descD",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 3,
                            startMillis: 100,
                            endMillis: 200,
                            className: "100-200",
                            methodName: "methodE",
                            methodDesc: "descE",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            startMillis: 400,
                            endMillis: 450,
                            className: "400-450",
                            methodName: "methodD",
                            methodDesc: "descD",
                            methodAccessFlag: 1
                        }, {
                            stack: 1,
                            startMillis: 0,
                            endMillis: 500,
                            className: "0-500",
                            methodName: "methodA",
                            methodDesc: "descA",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 2,
                            startMillis: 320,
                            endMillis: 350,
                            className: "320-350",
                            methodName: "methodC",
                            methodDesc: "descC",
                            methodAccessFlag: 1
                        },
                        {
                            stack: 1,
                            startMillis: 700,
                            endMillis: 800,
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

        this.recvFun("AndroidGodEyeNotification", {
            message: "message of message of message of message of message of message of message of message of message of message of " + this.index,
            timeMillis: 1469433907836
        });
        this.recvFun("AndroidGodEyeNotification", {
            message: "message of" + this.index,
            timeMillis: 1469433907836
        });
        this.recvFun("AndroidGodEyeNotificationAction", {
            // isInstall: this.index % 2 === 0
            isInstall: true
        });
        this.recvFun("appInfo", {
            appName: "I am Name",
            labels: [
                { name: "lablel1", value: "value0000000", url: "http://www.ctrip.com" },
                { name: "lablel2", value: "value1111111", url: "http://www.trip.com" },
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

        this.recvFun("methodCanaryMonitorState", {
            isRunning: this.index % 2 === 0
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
            pageType: "FRAGMENT",
            pageClassName: "pageClassName" + this.index,
            pageHashCode: 10000,

            lifecycleEvent: "ON_LOAD",
            startTimeMillis: 1469433907836,
            endTimeMillis: 1469433907836,
            processedInfo: {
                "loadTime": 2342
            }
        });
        this.recvFun("pageLifecycle", {
            pageType: "ACTIVITY",
            pageClassName: "pageClassName" + (this.index * 2),
            pageHashCode: 20000,
            lifecycleEvent: "ON_DRAW",
            startTimeMillis: 1469433907836,
            endTimeMillis: 1469433907836,
            processedInfo: {
                "drawTime": 700
            }
        });
        this.recvFun("pageLifecycle", {
            pageType: "FRAGMENT",
            pageClassName: "ClassName3",
            pageHashCode: 12312,
            lifecycleEvent: "ON_CREATE",
            startTimeMillis: 1469433907836,
            endTimeMillis: 1469433907836,
        });
        this.recvFun("crashInfo", [
            {
                "crashTime": "1990-12-11 12:21:33.SSSZ",
                "crashMessage": "this is a message1",
                "javaCrashStacktrace": "at xcrash.NativeHandler.nativeTestCrash(Native method)\n" +
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
                "nativeCrashCode": "1 (SEGV_MAPERR)",
                "crashType": "native",
                "extras": {
                    "logcat": "--------- tail end of log main (/system/bin/logcat -b main -d -v threadtime -t 200 --pid 4965 *:D)\n11-14 19:55:12.117  4965  4965 I d.godeye.sampl: Late-enabling -Xcheck:jni\n11-14 19:55:12.465  4965  4965 I DecorView: createDecorCaptionView >> DecorView@cb83bce[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:12.500  4965  4965 D OpenGLRenderer: Skia GL Pipeline\n11-14 19:55:12.506  4965  4965 D EmergencyMode: [EmergencyManager] android createPackageContext successful\n11-14 19:55:12.521  4965  4965 D InputTransport: Input channel constructed: fd=66\n11-14 19:55:12.521  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setView = DecorView@cb83bce[SplashActivity] TM=true MM=false\n11-14 19:55:12.529  4965  4965 D InputTransport: Input channel constructed: fd=67\n11-14 19:55:12.530  4965  4965 D ViewRootImpl@a69733d[Toast]: setView = android.widget.LinearLayout{e30d732 V.E...... ......I. 0,0-0,0} TM=true MM=false\n11-14 19:55:12.534  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchAttachedToWindow\n11-14 19:55:12.544  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,0][1440,2987] new=[0,150][1440,2987] result=0x7 surface={valid=true 517003522048} changed=true\n11-14 19:55:12.545  4965  5077 I Adreno  : QUALCOMM build                   : 0a66665, If34e3488e3\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Date                       : 04/05/19\n11-14 19:55:12.545  4965  5077 I Adreno  : OpenGL ES Shader Compiler Version: EV031.25.16.00\n11-14 19:55:12.545  4965  5077 I Adreno  : Local Branch                     : \n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : refs/tags/AU_LINUX_ANDROID_LA.UM.7.1.C1.09.00.00.526.029\n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : NONE\n11-14 19:55:12.545  4965  5077 I Adreno  : Reconstruct Branch               : NOTHING\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Config                     : S P 6.0.9 AArch64\n11-14 19:55:12.548  4965  5077 I Adreno  : PFP: 0x016ee177, ME: 0x00000000\n11-14 19:55:12.551  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchAttachedToWindow\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasWideColorDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasHDRDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I OpenGLRenderer: Initialized EGL, version 1.4\n11-14 19:55:12.553  4965  5077 D OpenGLRenderer: Swap behavior 2\n11-14 19:55:12.558  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6b80, 0x785fcfb010\n11-14 19:55:12.564  4965  4965 D ViewRootImpl@a69733d[Toast]: Relayout returned: old=[0,150][1440,2987] new=[115,2522][1324,2763] result=0x7 surface={valid=true 516586946560} changed=true\n11-14 19:55:12.569  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6d00, 0x7846fb4010\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_RESIZED: frame=Rect(0, 150 - 1440, 2987) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:12.571  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.571  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.572  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:12.572  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:12.576  4965  5005 D InputTransport: Input channel constructed: fd=72\n11-14 19:55:12.619  4965  4965 D ViewRootImpl@a69733d[Toast]: MSG_RESIZED: frame=Rect(115, 2522 - 1324, 2763) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.620  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.620  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.620  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.534  4965  4965 W ActivityThread: handleWindowVisibility: no activity for token android.os.BinderProxy@9dd2c4\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/graphics/drawable/Drawable;->getOpticalInsets()Landroid/graphics/Insets; (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->left:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->right:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->top:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->bottom:I (light greylist, linking)\n11-14 19:55:13.579  4965  4965 I DecorView: createDecorCaptionView >> DecorView@3b5eae1[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/View;->computeFitSystemWindows(Landroid/graphics/Rect;Landroid/graphics/Rect;)Z (light greylist, reflection)\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/ViewGroup;->makeOptionalFitsSystemWindows()V (light greylist, reflection)\n11-14 19:55:13.614  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.635  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/widget/TextView;->getTextDirectionHeuristic()Landroid/text/TextDirectionHeuristic; (light greylist, linking)\n11-14 19:55:13.706  4965  4965 D NetworkSecurityConfig: No Network Security Config specified, using platform default\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setUseSessionTickets(Z)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setHostname(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->getAlpnSelectedProtocol()[B (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setAlpnProtocols([B)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->get()Ldalvik/system/CloseGuard; (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->open(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->warnIfOpen()V (light greylist, reflection)\n11-14 19:55:13.717  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.732  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.765  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.808  4965  4965 D InputTransport: Input channel constructed: fd=78\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: setView = DecorView@3b5eae1[Main2Activity] TM=true MM=false\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 0 1\n11-14 19:55:13.808  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:13.809  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.837  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: dispatchAttachedToWindow\n11-14 19:55:13.855  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: Relayout returned: old=[0,0][1440,3040] new=[0,0][1440,3040] result=0x7 surface={valid=true 516583780352} changed=true\n11-14 19:55:13.858  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7855d56880, 0x7846caf010\n11-14 19:55:13.863  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.864  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_RESIZED: frame=Rect(0, 0 - 1440, 3040) ci=Rect(0, 150 - 0, 53) vi=Rect(0, 150 - 0, 53) or=1\n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:13.919  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.919  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.927  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.927  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel constructed: fd=81\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel destroyed: fd=72\n11-14 19:55:13.929  4965  4965 W System.err: cn.hikyson.godeye.core.exceptions.UninstallException: module [STARTUP] is not installed.\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEye.getModule(GodEye.java:275)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEyeHelper.onAppStartEnd(GodEyeHelper.java:71)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.StartupTracer.lambda$null$0(StartupTracer.java:47)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.-$$Lambda$StartupTracer$GTWyjZ1TjmYPllNgrq9MHEXacpQ.run(Unknown Source:2)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.handleCallback(Handler.java:873)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.dispatchMessage(Handler.java:99)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Looper.loop(Looper.java:214)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.app.ActivityThread.main(ActivityThread.java:7037)\n11-14 19:55:13.929  4965  4965 W System.err: \tat java.lang.reflect.Method.invoke(Native Method)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.ZygoteInit.main(ZygoteInit.java:965)\n11-14 19:55:14.220  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6b80, 0x785fcfb000\n11-14 19:55:14.228  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,150][1440,2987] new=[0,150][1440,2987] result=0x5 surface={valid=false 0} changed=true\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setWindowStopped(true) old=false\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.WindowManagerGlobal.setStoppedState:669 android.app.Activity.performStop:7690 android.app.ActivityThread.callActivityOnStop:4378 android.app.ActivityThread.performStopActivityInner:4356 android.app.ActivityThread.handleStopActivity:4431 android.app.servertransaction.TransactionExecutor.performLifecycleSequence:192 android.app.servertransaction.TransactionExecutor.cycleToPath:165 android.app.servertransaction.TransactionExecutor.executeLifecycleState:142 \n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchDetachedFromWindow\n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.app.ActivityThread.handleDestroyActivity:4752 android.app.servertransaction.DestroyActivityItem.execute:39 android.app.servertransaction.TransactionExecutor.executeLifecycleState:145 \n11-14 19:55:14.235  4965  4965 D InputTransport: Input channel destroyed: fd=66\n11-14 19:55:14.496  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6d00, 0x7846fb4000\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchDetachedFromWindow\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.widget.Toast$TN.handleHide:1158 android.widget.Toast$TN$1.handleMessage:946 android.os.Handler.dispatchMessage:106 \n11-14 19:55:14.503  4965  4965 D InputTransport: Input channel destroyed: fd=67\n11-14 19:55:16.713  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:16.755  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:16.801  4965  4965 D AndroidGodEye: DEBUG: Cpu installed\n11-14 19:55:16.804  4965  4965 D AndroidGodEye: DEBUG: Battery installed.\n11-14 19:55:16.806  4965  4965 D AndroidGodEye: DEBUG: Fps installed.\n11-14 19:55:16.818  4965  4965 D AndroidGodEye: DEBUG: LeakDetector installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Heap installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Pss installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Ram installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Network installed.\n11-14 19:55:16.821  4965  4965 D AndroidGodEye: DEBUG: Sm installed\n11-14 19:55:16.822  4965  4965 D AndroidGodEye: DEBUG: Startup installed.\n11-14 19:55:16.823  4965  4965 D AndroidGodEye: DEBUG: Traffic installed.\n11-14 19:55:16.831  4965  4965 D AndroidGodEye: DEBUG: Crash installed.\n11-14 19:55:16.832  4965  4965 D AndroidGodEye: DEBUG: ThreadDump installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: Pageload installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: MethodCanary installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: AppSize installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: View canary size installed.\n11-14 19:55:16.836  4965  4965 D AndroidGodEye: DEBUG: GodEye install, godEyeConfig: GodEyeConfig{mCpuConfig=CpuConfig{intervalMillis=2000}, mBatteryConfig=BatteryConfig{}, mFpsConfig=FpsConfig{intervalMillis=2000}, mLeakConfig=LeakConfig{debug=true, debugNotification=true, leakRefInfoProvider=cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider}, mHeapConfig=HeapConfig{intervalMillis=2000}, mPssConfig=PssConfig{intervalMillis=2000}, mRamConfig=RamConfig{intervalMillis=3000}, mNetworkConfig=NetworkConfig{}, mSmConfig=SmConfig{debugNotification=true, longBlockThresholdMillis=500, shortBlockThresholdMillis=500, dumpIntervalMillis=1000}, mStartupConfig=StartupConfig{}, mTrafficConfig=TrafficConfig{intervalMillis=2000, sampleMillis=1000}, mCrashConfig=CrashConfig{immediate=false}, mThreadConfig=ThreadConfig{intervalMillis=2000, threadFilter=cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter}, mPageloadConfig=PageloadConfig{pageInfoProvider=cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider}, mMethodCanaryConfig=MethodCanaryConfig{maxMethodCountSingleThreadByCost=300, lowCostMethodThresholdMillis=10}, mAppSizeConfig=cn.hikyson.godeye.core.GodEyeConfig$AppSizeConfig@2b60f5a}, cost 68 ms\n11-14 19:55:16.842  4965  5334 D AndroidGodEye: DEBUG: AppSize onGetSize: cache size: 1.51MB, data size: 3.09MB, codeSize: 10.66MB\n11-14 19:55:17.194  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.235  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.269  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:17.813  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.861  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.890  4965  5339 D TcpOptimizer: TcpOptimizer-ON\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: Open AndroidGodEye dashboard [ http://localhost:5390/index.html ] or [ http://10.38.28.208:5390/index.html ] in your browser\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: GodEye monitor is working...\n11-14 19:55:25.373  4965  5339 E AndroidGodEye: !ERROR: java.io.FileNotFoundException: android-godeye-dashboard/static/js/main.4f4e3555.js\n11-14 19:55:34.254  4965  5339 D AndroidGodEye: DEBUG: ModuleDriver start running.\n11-14 19:55:34.274  4965  5339 D AndroidGodEye: DEBUG: connection build. current count:1\n11-14 19:55:34.280  4965  5324 W d.godeye.sampl: Accessing hidden field Lsun/misc/Unsafe;->theUnsafe:Lsun/misc/Unsafe; (light greylist, reflection)\n11-14 19:55:45.021  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:45.068  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:45.105  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:48.785  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:48.823  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:48.849  4965  4965 D AndroidRuntime: Shutting down VM\n--------- tail end of log system (/system/bin/logcat -b system -d -v threadtime -t 50 --pid 4965 *:W)\n--------- tail end of log events (/system/bin/logcat -b events -d -v threadtime -t 50 --pid 4965 *:I)\n11-14 19:55:12.495  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performCreate]\n11-14 19:55:12.498  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,handleStartActivity]\n11-14 19:55:12.499  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,RESUME_ACTIVITY]\n11-14 19:55:13.524  4965  4965 I am_on_paused_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performPause]\n11-14 19:55:13.710  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,performCreate]\n11-14 19:55:13.797  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,handleStartActivity]\n11-14 19:55:13.798  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,RESUME_ACTIVITY]\n11-14 19:55:14.229  4965  4965 I am_on_stop_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,LIFECYCLER_STOP_ACTIVITY]\n11-14 19:55:14.231  4965  4965 I am_on_destroy_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performDestroy]\n",
                    "App version": "1.0",
                    "OS version": "9",
                    "other threads": "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16921, name: FinalizerWatchdogDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Thread.sleep(Native Method)\n    at java.lang.Thread.sleep(Thread.java:373)\n    at java.lang.Thread.sleep(Thread.java:314)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.sleepFor(Daemons.java:342)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.waitForFinalization(Daemons.java:364)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.runInternal(Daemons.java:281)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16924, name: Binder:4965_2  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16923, name: Binder:4965_1  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16925, name: Binder:4965_3  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16920, name: FinalizerDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Object.wait(Native Method)\n    at java.lang.Object.wait(Object.java:422)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:188)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:209)\n    at java.lang.Daemons$FinalizerDaemon.runInternal(Daemons.java:232)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\ntotal JVM threads (exclude the crashed thread): 35\nJVM threads matched whitelist: 5\nJVM threads ignored by max count limit: 0\ndumped JVM threads:5\n",
                    "Brand": "samsung",
                    "App ID": "cn.hikyson.android.godeye.sample",
                    "Build fingerprint": "samsung/beyond1qltezc/beyond1q:9/PPR1.180610.011/G9730ZCS2ASJU:user/release-keys",
                    "open files": "fd 0: /dev/null\nfd 1: /dev/null\nfd 2: /dev/null\nfd 3: /proc/4965/fd/3\nfd 4: /proc/4965/fd/4\nfd 5: /sys/kernel/debug/tracing/trace_marker\nfd 6: /dev/null\nfd 7: /dev/null\nfd 8: /dev/null\nfd 9: /system/framework/core-oj.jar\nfd 10: /system/framework/core-libart.jar\nfd 11: /dev/binder\nfd 12: /system/framework/conscrypt.jar\nfd 13: /system/framework/okhttp.jar\nfd 14: /system/framework/bouncycastle.jar\nfd 15: /system/framework/apache-xml.jar\nfd 16: /system/framework/ext.jar\nfd 17: /system/framework/framework.jar\nfd 18: /system/framework/telephony-common.jar\nfd 19: /system/framework/voip-common.jar\nfd 20: /system/framework/ims-common.jar\nfd 21: /system/framework/sprengine.jar\nfd 22: /system/framework/android.hidl.base-V1.0-java.jar\nfd 23: /system/framework/android.hidl.manager-V1.0-java.jar\nfd 24: /system/framework/knoxsdk.jar\nfd 25: /system/framework/timakeystore.jar\nfd 26: /system/framework/fipstimakeystore.jar\nfd 27: /system/framework/sec_edm.jar\nfd 28: /system/framework/knoxanalyticssdk.jar\nfd 29: /system/framework/smartbondingservice.jar\nfd 30: /system/framework/ucmopensslenginehelper.jar\nfd 31: /system/framework/esecomm.jar\nfd 32: /system/framework/securetimersdk.jar\nfd 33: /system/framework/sec_sdp_sdk.jar\nfd 34: /system/framework/sec_sdp_hidden_sdk.jar\nfd 35: /system/framework/framework-oahl-backward-compatibility.jar\nfd 36: /system/framework/android.test.base.jar\nfd 37: /system/framework/knoxvpnuidtag.jar\nfd 38: /system/framework/SemAudioThumbnail.jar\nfd 39: /system/framework/knoxguard.jar\nfd 40: /system/framework/tcmiface.jar\nfd 41: /system/framework/telephony-ext.jar\nfd 42: /system/framework/libprotobuf-java_mls.jar\nfd 43: /system/framework/drutils.jar\nfd 44: /system/framework/QPerformance.jar\nfd 45: /system/framework/UxPerformance.jar\nfd 46: /system/framework/framework-res.apk\nfd 47: /proc/4965/fd/47\nfd 48: /vendor/overlay/framework-res__auto_generated_rro.apk\nfd 49: /proc/4965/fd/49\nfd 50: /proc/4965/fd/50\nfd 51: /proc/4965/fd/51\nfd 52: /proc/4965/fd/52\nfd 53: /proc/4965/fd/53\nfd 54: /proc/4965/fd/54\nfd 55: /proc/4965/fd/55\nfd 56: /data/app/cn.hikyson.android.godeye.sample-edodD1MZaK-sCRMbJpr_Wg==/base.apk\nfd 57: /dev/ashmem\nfd 58: /dev/ashmem\nfd 59: /dev/ashmem\nfd 60: /proc/4965/fd/60\nfd 61: /proc/4965/fd/61\nfd 62: /proc/4965/fd/62\nfd 63: /proc/4965/fd/63\nfd 64: /dev/ion\nfd 65: /proc/4965/fd/65\nfd 66: /proc/4965/fd/66\nfd 67: /dev/null\nfd 68: /dev/kgsl-3d0\nfd 69: /dev/ion\nfd 70: /dev/hwbinder\nfd 71: /proc/4965/fd/71\nfd 72: /proc/4965/fd/72\nfd 73: /sys/kernel/debug/tracing/trace_marker\nfd 74: /proc/4965/fd/74\nfd 75: /proc/4965/fd/75\nfd 76: /proc/4965/fd/76\nfd 77: /dev/null\nfd 78: /proc/4965/fd/78\nfd 79: /proc/4965/fd/79\nfd 80: /proc/4965/fd/80\nfd 81: /proc/4965/fd/81\nfd 82: /proc/4965/fd/82\nfd 83: /dev/null\nfd 84: /proc/4965/fd/84\nfd 85: /proc/4965/fd/85\nfd 86: /proc/4965/fd/86\nfd 87: /proc/4965/fd/87\nfd 88: /dev/null\nfd 89: /proc/4965/fd/89\nfd 90: /proc/4965/fd/90\nfd 91: /proc/4965/fd/91\nfd 92: /proc/4965/fd/92\nfd 93: /proc/4965/fd/93\nfd 94: /proc/4965/fd/94\nfd 95: /proc/4965/fd/95\nfd 96: /proc/4965/fd/96\nfd 97: /proc/4965/fd/97\nfd 98: /proc/4965/fd/98\nfd 99: /proc/4965/fd/99\nfd 100: /proc/4965/fd/100\nfd 101: /proc/4965/fd/101\nfd 102: /proc/4965/fd/102\nfd 103: /proc/4965/fd/103\nfd 104: /proc/4965/fd/104\nfd 105: /proc/4965/fd/105\nfd 106: /proc/4965/fd/106\nfd 107: /proc/4965/fd/107\nfd 108: /proc/4965/fd/108\nfd 109: /data/data/cn.hikyson.android.godeye.sample/files/tombstones/tombstone_00001573732516829000_1.0__cn.hikyson.android.godeye.sample.java.xcrash\nfd 110: /proc/4965/fd/110\nfd 111: /proc/4965/fd/111\nfd 112: /proc/4965/fd/112\nfd 113: /proc/4965/fd/113\nfd 114: /proc/4965/fd/114\n(number of FDs: 115)\n",
                    "Manufacturer": "samsung",
                    "Model": "SM-G9730",
                    "ABI list": "arm64-v8a,armeabi-v7a,armeabi",
                    "memory info": " System Summary (From: /proc/meminfo)\n  MemTotal:        7656764 kB\n  MemFree:         1294452 kB\n  MemAvailable:    3271272 kB\n  Buffers:          210272 kB\n  Cached:          2835852 kB\n  SwapCached:         6288 kB\n  Active:          2095424 kB\n  Inactive:        1511904 kB\n  Active(anon):     872904 kB\n  Inactive(anon):   573444 kB\n  Active(file):    1222520 kB\n  Inactive(file):   938460 kB\n  Unevictable:      886720 kB\n  Mlocked:          886804 kB\n  RbinTotal:        327680 kB\n  RbinAlloced:        5128 kB\n  RbinPool:              0 kB\n  RbinFree:          52200 kB\n  SwapTotal:       2621436 kB\n  SwapFree:        2136632 kB\n  Dirty:                72 kB\n  Writeback:             0 kB\n  AnonPages:       1443228 kB\n  Mapped:          1556364 kB\n  Shmem:              2568 kB\n  Slab:             342904 kB\n  SReclaimable:     104360 kB\n  SUnreclaim:       238544 kB\n  KernelStack:       56416 kB\n  PageTables:        96420 kB\n  NFS_Unstable:          0 kB\n  Bounce:                0 kB\n  WritebackTmp:          0 kB\n  CommitLimit:     6449816 kB\n  Committed_AS:   130354860 kB\n  VmallocTotal:   263061440 kB\n  VmallocUsed:           0 kB\n  VmallocChunk:          0 kB\n  CmaTotal:         270336 kB\n  CmaFree:           95292 kB\n-\n Process Status (From: /proc/PID/status)\n  Name:\td.godeye.sample\n  Umask:\t0077\n  State:\tR (running)\n  Tgid:\t4965\n  Ngid:\t0\n  Pid:\t4965\n  PPid:\t693\n  TracerPid:\t0\n  Uid:\t10431\t10431\t10431\t10431\n  Gid:\t10431\t10431\t10431\t10431\n  FDSize:\t128\n  Groups:\t3003 9997 20431 50431\n  NStgid:\t4965\n  NSpid:\t4965\n  NSpgid:\t693\n  NSsid:\t0\n  VmPeak:\t 4430764 kB\n  VmSize:\t 3956848 kB\n  VmLck:\t       0 kB\n  VmPin:\t       0 kB\n  VmHWM:\t  184796 kB\n  VmRSS:\t  180580 kB\n  RssAnon:\t   83420 kB\n  RssFile:\t   96708 kB\n  RssShmem:\t     452 kB\n  VmData:\t 1262172 kB\n  VmStk:\t    8192 kB\n  VmExe:\t      80 kB\n  VmLib:\t  145324 kB\n  VmPTE:\t    1164 kB\n  VmPMD:\t      32 kB\n  VmSwap:\t    4480 kB\n  Threads:\t40\n  SigQ:\t0/27421\n  SigPnd:\t0000000000000000\n  ShdPnd:\t0000000000000000\n  SigBlk:\t0000000000001200\n  SigIgn:\t0000000000000001\n  SigCgt:\t00000006400084fc\n  CapInh:\t0000000000000000\n  CapPrm:\t0000000000000000\n  CapEff:\t0000000000000000\n  CapBnd:\t0000000000000000\n  CapAmb:\t0000000000000000\n  NoNewPrivs:\t0\n  Seccomp:\t2\n  Speculation_Store_Bypass:\tunknown\n  Cpus_allowed:\tff\n  Cpus_allowed_list:\t0-7\n  Mems_allowed:\t1\n  Mems_allowed_list:\t0\n  voluntary_ctxt_switches:\t2444\n  nonvoluntary_ctxt_switches:\t343\n-\n Process Limits (From: /proc/PID/limits)\n  Limit                     Soft Limit           Hard Limit           Units\n  Max cpu time              unlimited            unlimited            seconds\n  Max file size             unlimited            unlimited            bytes\n  Max data size             unlimited            unlimited            bytes\n  Max stack size            8388608              unlimited            bytes\n  Max core file size        0                    unlimited            bytes\n  Max resident set          unlimited            unlimited            bytes\n  Max processes             27421                27421                processes\n  Max open files            32768                32768                files\n  Max locked memory         67108864             67108864             bytes\n  Max address space         unlimited            unlimited            bytes\n  Max file locks            unlimited            unlimited            locks\n  Max pending signals       27421                27421                signals\n  Max msgqueue size         819200               819200               bytes\n  Max nice priority         40                   40\n  Max realtime priority     0                    0\n  Max realtime timeout      unlimited            unlimited            us\n-\n Process Summary (From: android.os.Debug.MemoryInfo)\n                       Pss(KB)\n                        ------\n           Java Heap:    55364\n         Native Heap:    15960\n                Code:     9336\n               Stack:      112\n            Graphics:     4788\n       Private Other:     3812\n              System:     3720\n               TOTAL:    93092           TOTAL SWAP:     4236\n",
                    "Tombstone maker": "xCrash 2.4.6",
                    "Rooted": "No",
                    "API level": "28"
                }
            },
            {
                "crashTime": "1990-12-11 12:21:33.SSSZ",
                "crashMessage": "this is a message2",
                "javaCrashStacktrace": "at xcrash.NativeHa2342342342method)\n" +
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
                "nativeCrashCode": "1 (SEGV_MAPERR)",
                "crashType": "java",
                "extras": {
                    "logcat": "--------- tail end of log main (/system/bin/logcat -b main -d -v threadtime -t 200 --pid 4965 *:D)\n11-14 19:55:12.117  4965  4965 I d.godeye.sampl: Late-enabling -Xcheck:jni\n11-14 19:55:12.465  4965  4965 I DecorView: createDecorCaptionView >> DecorView@cb83bce[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:12.500  4965  4965 D OpenGLRenderer: Skia GL Pipeline\n11-14 19:55:12.506  4965  4965 D EmergencyMode: [EmergencyManager] android createPackageContext successful\n11-14 19:55:12.521  4965  4965 D InputTransport: Input channel constructed: fd=66\n11-14 19:55:12.521  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setView = DecorView@cb83bce[SplashActivity] TM=true MM=false\n11-14 19:55:12.529  4965  4965 D InputTransport: Input channel constructed: fd=67\n11-14 19:55:12.530  4965  4965 D ViewRootImpl@a69733d[Toast]: setView = android.widget.LinearLayout{e30d732 V.E...... ......I. 0,0-0,0} TM=true MM=false\n11-14 19:55:12.534  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchAttachedToWindow\n11-14 19:55:12.544  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,0][1440,2987] new=[0,150][1440,2987] result=0x7 surface={valid=true 517003522048} changed=true\n11-14 19:55:12.545  4965  5077 I Adreno  : QUALCOMM build                   : 0a66665, If34e3488e3\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Date                       : 04/05/19\n11-14 19:55:12.545  4965  5077 I Adreno  : OpenGL ES Shader Compiler Version: EV031.25.16.00\n11-14 19:55:12.545  4965  5077 I Adreno  : Local Branch                     : \n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : refs/tags/AU_LINUX_ANDROID_LA.UM.7.1.C1.09.00.00.526.029\n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : NONE\n11-14 19:55:12.545  4965  5077 I Adreno  : Reconstruct Branch               : NOTHING\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Config                     : S P 6.0.9 AArch64\n11-14 19:55:12.548  4965  5077 I Adreno  : PFP: 0x016ee177, ME: 0x00000000\n11-14 19:55:12.551  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchAttachedToWindow\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasWideColorDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasHDRDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I OpenGLRenderer: Initialized EGL, version 1.4\n11-14 19:55:12.553  4965  5077 D OpenGLRenderer: Swap behavior 2\n11-14 19:55:12.558  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6b80, 0x785fcfb010\n11-14 19:55:12.564  4965  4965 D ViewRootImpl@a69733d[Toast]: Relayout returned: old=[0,150][1440,2987] new=[115,2522][1324,2763] result=0x7 surface={valid=true 516586946560} changed=true\n11-14 19:55:12.569  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6d00, 0x7846fb4010\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_RESIZED: frame=Rect(0, 150 - 1440, 2987) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:12.571  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.571  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.572  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:12.572  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:12.576  4965  5005 D InputTransport: Input channel constructed: fd=72\n11-14 19:55:12.619  4965  4965 D ViewRootImpl@a69733d[Toast]: MSG_RESIZED: frame=Rect(115, 2522 - 1324, 2763) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.620  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.620  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.620  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.534  4965  4965 W ActivityThread: handleWindowVisibility: no activity for token android.os.BinderProxy@9dd2c4\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/graphics/drawable/Drawable;->getOpticalInsets()Landroid/graphics/Insets; (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->left:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->right:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->top:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->bottom:I (light greylist, linking)\n11-14 19:55:13.579  4965  4965 I DecorView: createDecorCaptionView >> DecorView@3b5eae1[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/View;->computeFitSystemWindows(Landroid/graphics/Rect;Landroid/graphics/Rect;)Z (light greylist, reflection)\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/ViewGroup;->makeOptionalFitsSystemWindows()V (light greylist, reflection)\n11-14 19:55:13.614  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.635  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/widget/TextView;->getTextDirectionHeuristic()Landroid/text/TextDirectionHeuristic; (light greylist, linking)\n11-14 19:55:13.706  4965  4965 D NetworkSecurityConfig: No Network Security Config specified, using platform default\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setUseSessionTickets(Z)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setHostname(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->getAlpnSelectedProtocol()[B (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setAlpnProtocols([B)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->get()Ldalvik/system/CloseGuard; (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->open(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->warnIfOpen()V (light greylist, reflection)\n11-14 19:55:13.717  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.732  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.765  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.808  4965  4965 D InputTransport: Input channel constructed: fd=78\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: setView = DecorView@3b5eae1[Main2Activity] TM=true MM=false\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 0 1\n11-14 19:55:13.808  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:13.809  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.837  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: dispatchAttachedToWindow\n11-14 19:55:13.855  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: Relayout returned: old=[0,0][1440,3040] new=[0,0][1440,3040] result=0x7 surface={valid=true 516583780352} changed=true\n11-14 19:55:13.858  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7855d56880, 0x7846caf010\n11-14 19:55:13.863  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.864  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_RESIZED: frame=Rect(0, 0 - 1440, 3040) ci=Rect(0, 150 - 0, 53) vi=Rect(0, 150 - 0, 53) or=1\n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:13.919  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.919  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.927  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.927  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel constructed: fd=81\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel destroyed: fd=72\n11-14 19:55:13.929  4965  4965 W System.err: cn.hikyson.godeye.core.exceptions.UninstallException: module [STARTUP] is not installed.\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEye.getModule(GodEye.java:275)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEyeHelper.onAppStartEnd(GodEyeHelper.java:71)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.StartupTracer.lambda$null$0(StartupTracer.java:47)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.-$$Lambda$StartupTracer$GTWyjZ1TjmYPllNgrq9MHEXacpQ.run(Unknown Source:2)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.handleCallback(Handler.java:873)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.dispatchMessage(Handler.java:99)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Looper.loop(Looper.java:214)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.app.ActivityThread.main(ActivityThread.java:7037)\n11-14 19:55:13.929  4965  4965 W System.err: \tat java.lang.reflect.Method.invoke(Native Method)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.ZygoteInit.main(ZygoteInit.java:965)\n11-14 19:55:14.220  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6b80, 0x785fcfb000\n11-14 19:55:14.228  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,150][1440,2987] new=[0,150][1440,2987] result=0x5 surface={valid=false 0} changed=true\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setWindowStopped(true) old=false\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.WindowManagerGlobal.setStoppedState:669 android.app.Activity.performStop:7690 android.app.ActivityThread.callActivityOnStop:4378 android.app.ActivityThread.performStopActivityInner:4356 android.app.ActivityThread.handleStopActivity:4431 android.app.servertransaction.TransactionExecutor.performLifecycleSequence:192 android.app.servertransaction.TransactionExecutor.cycleToPath:165 android.app.servertransaction.TransactionExecutor.executeLifecycleState:142 \n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchDetachedFromWindow\n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.app.ActivityThread.handleDestroyActivity:4752 android.app.servertransaction.DestroyActivityItem.execute:39 android.app.servertransaction.TransactionExecutor.executeLifecycleState:145 \n11-14 19:55:14.235  4965  4965 D InputTransport: Input channel destroyed: fd=66\n11-14 19:55:14.496  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6d00, 0x7846fb4000\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchDetachedFromWindow\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.widget.Toast$TN.handleHide:1158 android.widget.Toast$TN$1.handleMessage:946 android.os.Handler.dispatchMessage:106 \n11-14 19:55:14.503  4965  4965 D InputTransport: Input channel destroyed: fd=67\n11-14 19:55:16.713  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:16.755  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:16.801  4965  4965 D AndroidGodEye: DEBUG: Cpu installed\n11-14 19:55:16.804  4965  4965 D AndroidGodEye: DEBUG: Battery installed.\n11-14 19:55:16.806  4965  4965 D AndroidGodEye: DEBUG: Fps installed.\n11-14 19:55:16.818  4965  4965 D AndroidGodEye: DEBUG: LeakDetector installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Heap installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Pss installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Ram installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Network installed.\n11-14 19:55:16.821  4965  4965 D AndroidGodEye: DEBUG: Sm installed\n11-14 19:55:16.822  4965  4965 D AndroidGodEye: DEBUG: Startup installed.\n11-14 19:55:16.823  4965  4965 D AndroidGodEye: DEBUG: Traffic installed.\n11-14 19:55:16.831  4965  4965 D AndroidGodEye: DEBUG: Crash installed.\n11-14 19:55:16.832  4965  4965 D AndroidGodEye: DEBUG: ThreadDump installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: Pageload installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: MethodCanary installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: AppSize installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: View canary size installed.\n11-14 19:55:16.836  4965  4965 D AndroidGodEye: DEBUG: GodEye install, godEyeConfig: GodEyeConfig{mCpuConfig=CpuConfig{intervalMillis=2000}, mBatteryConfig=BatteryConfig{}, mFpsConfig=FpsConfig{intervalMillis=2000}, mLeakConfig=LeakConfig{debug=true, debugNotification=true, leakRefInfoProvider=cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider}, mHeapConfig=HeapConfig{intervalMillis=2000}, mPssConfig=PssConfig{intervalMillis=2000}, mRamConfig=RamConfig{intervalMillis=3000}, mNetworkConfig=NetworkConfig{}, mSmConfig=SmConfig{debugNotification=true, longBlockThresholdMillis=500, shortBlockThresholdMillis=500, dumpIntervalMillis=1000}, mStartupConfig=StartupConfig{}, mTrafficConfig=TrafficConfig{intervalMillis=2000, sampleMillis=1000}, mCrashConfig=CrashConfig{immediate=false}, mThreadConfig=ThreadConfig{intervalMillis=2000, threadFilter=cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter}, mPageloadConfig=PageloadConfig{pageInfoProvider=cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider}, mMethodCanaryConfig=MethodCanaryConfig{maxMethodCountSingleThreadByCost=300, lowCostMethodThresholdMillis=10}, mAppSizeConfig=cn.hikyson.godeye.core.GodEyeConfig$AppSizeConfig@2b60f5a}, cost 68 ms\n11-14 19:55:16.842  4965  5334 D AndroidGodEye: DEBUG: AppSize onGetSize: cache size: 1.51MB, data size: 3.09MB, codeSize: 10.66MB\n11-14 19:55:17.194  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.235  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.269  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:17.813  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.861  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.890  4965  5339 D TcpOptimizer: TcpOptimizer-ON\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: Open AndroidGodEye dashboard [ http://localhost:5390/index.html ] or [ http://10.38.28.208:5390/index.html ] in your browser\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: GodEye monitor is working...\n11-14 19:55:25.373  4965  5339 E AndroidGodEye: !ERROR: java.io.FileNotFoundException: android-godeye-dashboard/static/js/main.4f4e3555.js\n11-14 19:55:34.254  4965  5339 D AndroidGodEye: DEBUG: ModuleDriver start running.\n11-14 19:55:34.274  4965  5339 D AndroidGodEye: DEBUG: connection build. current count:1\n11-14 19:55:34.280  4965  5324 W d.godeye.sampl: Accessing hidden field Lsun/misc/Unsafe;->theUnsafe:Lsun/misc/Unsafe; (light greylist, reflection)\n11-14 19:55:45.021  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:45.068  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:45.105  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:48.785  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:48.823  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:48.849  4965  4965 D AndroidRuntime: Shutting down VM\n--------- tail end of log system (/system/bin/logcat -b system -d -v threadtime -t 50 --pid 4965 *:W)\n--------- tail end of log events (/system/bin/logcat -b events -d -v threadtime -t 50 --pid 4965 *:I)\n11-14 19:55:12.495  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performCreate]\n11-14 19:55:12.498  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,handleStartActivity]\n11-14 19:55:12.499  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,RESUME_ACTIVITY]\n11-14 19:55:13.524  4965  4965 I am_on_paused_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performPause]\n11-14 19:55:13.710  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,performCreate]\n11-14 19:55:13.797  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,handleStartActivity]\n11-14 19:55:13.798  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,RESUME_ACTIVITY]\n11-14 19:55:14.229  4965  4965 I am_on_stop_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,LIFECYCLER_STOP_ACTIVITY]\n11-14 19:55:14.231  4965  4965 I am_on_destroy_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performDestroy]\n",
                    "App version": "1.0",
                    "OS version": "9",
                    "other threads": "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16921, name: FinalizerWatchdogDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Thread.sleep(Native Method)\n    at java.lang.Thread.sleep(Thread.java:373)\n    at java.lang.Thread.sleep(Thread.java:314)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.sleepFor(Daemons.java:342)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.waitForFinalization(Daemons.java:364)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.runInternal(Daemons.java:281)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16924, name: Binder:4965_2  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16923, name: Binder:4965_1  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16925, name: Binder:4965_3  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16920, name: FinalizerDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Object.wait(Native Method)\n    at java.lang.Object.wait(Object.java:422)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:188)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:209)\n    at java.lang.Daemons$FinalizerDaemon.runInternal(Daemons.java:232)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\ntotal JVM threads (exclude the crashed thread): 35\nJVM threads matched whitelist: 5\nJVM threads ignored by max count limit: 0\ndumped JVM threads:5\n",
                    "Brand": "samsung",
                    "App ID": "cn.hikyson.android.godeye.sample",
                    "Build fingerprint": "samsung/beyond1qltezc/beyond1q:9/PPR1.180610.011/G9730ZCS2ASJU:user/release-keys",
                    "open files": "fd 0: /dev/null\nfd 1: /dev/null\nfd 2: /dev/null\nfd 3: /proc/4965/fd/3\nfd 4: /proc/4965/fd/4\nfd 5: /sys/kernel/debug/tracing/trace_marker\nfd 6: /dev/null\nfd 7: /dev/null\nfd 8: /dev/null\nfd 9: /system/framework/core-oj.jar\nfd 10: /system/framework/core-libart.jar\nfd 11: /dev/binder\nfd 12: /system/framework/conscrypt.jar\nfd 13: /system/framework/okhttp.jar\nfd 14: /system/framework/bouncycastle.jar\nfd 15: /system/framework/apache-xml.jar\nfd 16: /system/framework/ext.jar\nfd 17: /system/framework/framework.jar\nfd 18: /system/framework/telephony-common.jar\nfd 19: /system/framework/voip-common.jar\nfd 20: /system/framework/ims-common.jar\nfd 21: /system/framework/sprengine.jar\nfd 22: /system/framework/android.hidl.base-V1.0-java.jar\nfd 23: /system/framework/android.hidl.manager-V1.0-java.jar\nfd 24: /system/framework/knoxsdk.jar\nfd 25: /system/framework/timakeystore.jar\nfd 26: /system/framework/fipstimakeystore.jar\nfd 27: /system/framework/sec_edm.jar\nfd 28: /system/framework/knoxanalyticssdk.jar\nfd 29: /system/framework/smartbondingservice.jar\nfd 30: /system/framework/ucmopensslenginehelper.jar\nfd 31: /system/framework/esecomm.jar\nfd 32: /system/framework/securetimersdk.jar\nfd 33: /system/framework/sec_sdp_sdk.jar\nfd 34: /system/framework/sec_sdp_hidden_sdk.jar\nfd 35: /system/framework/framework-oahl-backward-compatibility.jar\nfd 36: /system/framework/android.test.base.jar\nfd 37: /system/framework/knoxvpnuidtag.jar\nfd 38: /system/framework/SemAudioThumbnail.jar\nfd 39: /system/framework/knoxguard.jar\nfd 40: /system/framework/tcmiface.jar\nfd 41: /system/framework/telephony-ext.jar\nfd 42: /system/framework/libprotobuf-java_mls.jar\nfd 43: /system/framework/drutils.jar\nfd 44: /system/framework/QPerformance.jar\nfd 45: /system/framework/UxPerformance.jar\nfd 46: /system/framework/framework-res.apk\nfd 47: /proc/4965/fd/47\nfd 48: /vendor/overlay/framework-res__auto_generated_rro.apk\nfd 49: /proc/4965/fd/49\nfd 50: /proc/4965/fd/50\nfd 51: /proc/4965/fd/51\nfd 52: /proc/4965/fd/52\nfd 53: /proc/4965/fd/53\nfd 54: /proc/4965/fd/54\nfd 55: /proc/4965/fd/55\nfd 56: /data/app/cn.hikyson.android.godeye.sample-edodD1MZaK-sCRMbJpr_Wg==/base.apk\nfd 57: /dev/ashmem\nfd 58: /dev/ashmem\nfd 59: /dev/ashmem\nfd 60: /proc/4965/fd/60\nfd 61: /proc/4965/fd/61\nfd 62: /proc/4965/fd/62\nfd 63: /proc/4965/fd/63\nfd 64: /dev/ion\nfd 65: /proc/4965/fd/65\nfd 66: /proc/4965/fd/66\nfd 67: /dev/null\nfd 68: /dev/kgsl-3d0\nfd 69: /dev/ion\nfd 70: /dev/hwbinder\nfd 71: /proc/4965/fd/71\nfd 72: /proc/4965/fd/72\nfd 73: /sys/kernel/debug/tracing/trace_marker\nfd 74: /proc/4965/fd/74\nfd 75: /proc/4965/fd/75\nfd 76: /proc/4965/fd/76\nfd 77: /dev/null\nfd 78: /proc/4965/fd/78\nfd 79: /proc/4965/fd/79\nfd 80: /proc/4965/fd/80\nfd 81: /proc/4965/fd/81\nfd 82: /proc/4965/fd/82\nfd 83: /dev/null\nfd 84: /proc/4965/fd/84\nfd 85: /proc/4965/fd/85\nfd 86: /proc/4965/fd/86\nfd 87: /proc/4965/fd/87\nfd 88: /dev/null\nfd 89: /proc/4965/fd/89\nfd 90: /proc/4965/fd/90\nfd 91: /proc/4965/fd/91\nfd 92: /proc/4965/fd/92\nfd 93: /proc/4965/fd/93\nfd 94: /proc/4965/fd/94\nfd 95: /proc/4965/fd/95\nfd 96: /proc/4965/fd/96\nfd 97: /proc/4965/fd/97\nfd 98: /proc/4965/fd/98\nfd 99: /proc/4965/fd/99\nfd 100: /proc/4965/fd/100\nfd 101: /proc/4965/fd/101\nfd 102: /proc/4965/fd/102\nfd 103: /proc/4965/fd/103\nfd 104: /proc/4965/fd/104\nfd 105: /proc/4965/fd/105\nfd 106: /proc/4965/fd/106\nfd 107: /proc/4965/fd/107\nfd 108: /proc/4965/fd/108\nfd 109: /data/data/cn.hikyson.android.godeye.sample/files/tombstones/tombstone_00001573732516829000_1.0__cn.hikyson.android.godeye.sample.java.xcrash\nfd 110: /proc/4965/fd/110\nfd 111: /proc/4965/fd/111\nfd 112: /proc/4965/fd/112\nfd 113: /proc/4965/fd/113\nfd 114: /proc/4965/fd/114\n(number of FDs: 115)\n",
                    "Manufacturer": "samsung",
                    "Model": "SM-G9730",
                    "ABI list": "arm64-v8a,armeabi-v7a,armeabi",
                    "memory info": " System Summary (From: /proc/meminfo)\n  MemTotal:        7656764 kB\n  MemFree:         1294452 kB\n  MemAvailable:    3271272 kB\n  Buffers:          210272 kB\n  Cached:          2835852 kB\n  SwapCached:         6288 kB\n  Active:          2095424 kB\n  Inactive:        1511904 kB\n  Active(anon):     872904 kB\n  Inactive(anon):   573444 kB\n  Active(file):    1222520 kB\n  Inactive(file):   938460 kB\n  Unevictable:      886720 kB\n  Mlocked:          886804 kB\n  RbinTotal:        327680 kB\n  RbinAlloced:        5128 kB\n  RbinPool:              0 kB\n  RbinFree:          52200 kB\n  SwapTotal:       2621436 kB\n  SwapFree:        2136632 kB\n  Dirty:                72 kB\n  Writeback:             0 kB\n  AnonPages:       1443228 kB\n  Mapped:          1556364 kB\n  Shmem:              2568 kB\n  Slab:             342904 kB\n  SReclaimable:     104360 kB\n  SUnreclaim:       238544 kB\n  KernelStack:       56416 kB\n  PageTables:        96420 kB\n  NFS_Unstable:          0 kB\n  Bounce:                0 kB\n  WritebackTmp:          0 kB\n  CommitLimit:     6449816 kB\n  Committed_AS:   130354860 kB\n  VmallocTotal:   263061440 kB\n  VmallocUsed:           0 kB\n  VmallocChunk:          0 kB\n  CmaTotal:         270336 kB\n  CmaFree:           95292 kB\n-\n Process Status (From: /proc/PID/status)\n  Name:\td.godeye.sample\n  Umask:\t0077\n  State:\tR (running)\n  Tgid:\t4965\n  Ngid:\t0\n  Pid:\t4965\n  PPid:\t693\n  TracerPid:\t0\n  Uid:\t10431\t10431\t10431\t10431\n  Gid:\t10431\t10431\t10431\t10431\n  FDSize:\t128\n  Groups:\t3003 9997 20431 50431\n  NStgid:\t4965\n  NSpid:\t4965\n  NSpgid:\t693\n  NSsid:\t0\n  VmPeak:\t 4430764 kB\n  VmSize:\t 3956848 kB\n  VmLck:\t       0 kB\n  VmPin:\t       0 kB\n  VmHWM:\t  184796 kB\n  VmRSS:\t  180580 kB\n  RssAnon:\t   83420 kB\n  RssFile:\t   96708 kB\n  RssShmem:\t     452 kB\n  VmData:\t 1262172 kB\n  VmStk:\t    8192 kB\n  VmExe:\t      80 kB\n  VmLib:\t  145324 kB\n  VmPTE:\t    1164 kB\n  VmPMD:\t      32 kB\n  VmSwap:\t    4480 kB\n  Threads:\t40\n  SigQ:\t0/27421\n  SigPnd:\t0000000000000000\n  ShdPnd:\t0000000000000000\n  SigBlk:\t0000000000001200\n  SigIgn:\t0000000000000001\n  SigCgt:\t00000006400084fc\n  CapInh:\t0000000000000000\n  CapPrm:\t0000000000000000\n  CapEff:\t0000000000000000\n  CapBnd:\t0000000000000000\n  CapAmb:\t0000000000000000\n  NoNewPrivs:\t0\n  Seccomp:\t2\n  Speculation_Store_Bypass:\tunknown\n  Cpus_allowed:\tff\n  Cpus_allowed_list:\t0-7\n  Mems_allowed:\t1\n  Mems_allowed_list:\t0\n  voluntary_ctxt_switches:\t2444\n  nonvoluntary_ctxt_switches:\t343\n-\n Process Limits (From: /proc/PID/limits)\n  Limit                     Soft Limit           Hard Limit           Units\n  Max cpu time              unlimited            unlimited            seconds\n  Max file size             unlimited            unlimited            bytes\n  Max data size             unlimited            unlimited            bytes\n  Max stack size            8388608              unlimited            bytes\n  Max core file size        0                    unlimited            bytes\n  Max resident set          unlimited            unlimited            bytes\n  Max processes             27421                27421                processes\n  Max open files            32768                32768                files\n  Max locked memory         67108864             67108864             bytes\n  Max address space         unlimited            unlimited            bytes\n  Max file locks            unlimited            unlimited            locks\n  Max pending signals       27421                27421                signals\n  Max msgqueue size         819200               819200               bytes\n  Max nice priority         40                   40\n  Max realtime priority     0                    0\n  Max realtime timeout      unlimited            unlimited            us\n-\n Process Summary (From: android.os.Debug.MemoryInfo)\n                       Pss(KB)\n                        ------\n           Java Heap:    55364\n         Native Heap:    15960\n                Code:     9336\n               Stack:      112\n            Graphics:     4788\n       Private Other:     3812\n              System:     3720\n               TOTAL:    93092           TOTAL SWAP:     4236\n",
                    "Tombstone maker": "xCrash 2.4.6",
                    "Rooted": "No",
                    "API level": "28"
                }
            },
            {
                "crashTime": "1920-12-11 12:21:33.SSSZ",
                "crashMessage": "this is a message3",
                "javaCrashStacktrace": "at xcrash.4123f34334fveTestCrash(Native method)\n" +
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
                "nativeCrashCode": "1 (SEGV_MAPERR)",
                "crashType": "java",
                "extras": {
                    "logcat": "--------- tail end of log main (/system/bin/logcat -b main -d -v threadtime -t 200 --pid 4965 *:D)\n11-14 19:55:12.117  4965  4965 I d.godeye.sampl: Late-enabling -Xcheck:jni\n11-14 19:55:12.465  4965  4965 I DecorView: createDecorCaptionView >> DecorView@cb83bce[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:12.500  4965  4965 D OpenGLRenderer: Skia GL Pipeline\n11-14 19:55:12.506  4965  4965 D EmergencyMode: [EmergencyManager] android createPackageContext successful\n11-14 19:55:12.521  4965  4965 D InputTransport: Input channel constructed: fd=66\n11-14 19:55:12.521  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setView = DecorView@cb83bce[SplashActivity] TM=true MM=false\n11-14 19:55:12.529  4965  4965 D InputTransport: Input channel constructed: fd=67\n11-14 19:55:12.530  4965  4965 D ViewRootImpl@a69733d[Toast]: setView = android.widget.LinearLayout{e30d732 V.E...... ......I. 0,0-0,0} TM=true MM=false\n11-14 19:55:12.534  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchAttachedToWindow\n11-14 19:55:12.544  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,0][1440,2987] new=[0,150][1440,2987] result=0x7 surface={valid=true 517003522048} changed=true\n11-14 19:55:12.545  4965  5077 I Adreno  : QUALCOMM build                   : 0a66665, If34e3488e3\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Date                       : 04/05/19\n11-14 19:55:12.545  4965  5077 I Adreno  : OpenGL ES Shader Compiler Version: EV031.25.16.00\n11-14 19:55:12.545  4965  5077 I Adreno  : Local Branch                     : \n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : refs/tags/AU_LINUX_ANDROID_LA.UM.7.1.C1.09.00.00.526.029\n11-14 19:55:12.545  4965  5077 I Adreno  : Remote Branch                    : NONE\n11-14 19:55:12.545  4965  5077 I Adreno  : Reconstruct Branch               : NOTHING\n11-14 19:55:12.545  4965  5077 I Adreno  : Build Config                     : S P 6.0.9 AArch64\n11-14 19:55:12.548  4965  5077 I Adreno  : PFP: 0x016ee177, ME: 0x00000000\n11-14 19:55:12.551  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchAttachedToWindow\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasWideColorDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I ConfigStore: android::hardware::configstore::V1_0::ISurfaceFlingerConfigs::hasHDRDisplay retrieved: 1\n11-14 19:55:12.553  4965  5077 I OpenGLRenderer: Initialized EGL, version 1.4\n11-14 19:55:12.553  4965  5077 D OpenGLRenderer: Swap behavior 2\n11-14 19:55:12.558  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6b80, 0x785fcfb010\n11-14 19:55:12.564  4965  4965 D ViewRootImpl@a69733d[Toast]: Relayout returned: old=[0,150][1440,2987] new=[115,2522][1324,2763] result=0x7 surface={valid=true 516586946560} changed=true\n11-14 19:55:12.569  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7846da6d00, 0x7846fb4010\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_RESIZED: frame=Rect(0, 150 - 1440, 2987) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.570  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:12.571  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.571  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.572  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.572  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:12.572  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:12.576  4965  5005 D InputTransport: Input channel constructed: fd=72\n11-14 19:55:12.619  4965  4965 D ViewRootImpl@a69733d[Toast]: MSG_RESIZED: frame=Rect(115, 2522 - 1324, 2763) ci=Rect(0, 0 - 0, 0) vi=Rect(0, 0 - 0, 0) or=1\n11-14 19:55:12.620  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:12.620  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:12.620  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.534  4965  4965 W ActivityThread: handleWindowVisibility: no activity for token android.os.BinderProxy@9dd2c4\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/graphics/drawable/Drawable;->getOpticalInsets()Landroid/graphics/Insets; (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->left:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->right:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->top:I (light greylist, linking)\n11-14 19:55:13.572  4965  4965 W d.godeye.sampl: Accessing hidden field Landroid/graphics/Insets;->bottom:I (light greylist, linking)\n11-14 19:55:13.579  4965  4965 I DecorView: createDecorCaptionView >> DecorView@3b5eae1[], isFloating: false, isApplication: true, hasWindowDecorCaption: false, hasWindowControllerCallback: true\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/View;->computeFitSystemWindows(Landroid/graphics/Rect;Landroid/graphics/Rect;)Z (light greylist, reflection)\n11-14 19:55:13.594  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/view/ViewGroup;->makeOptionalFitsSystemWindows()V (light greylist, reflection)\n11-14 19:55:13.614  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.635  4965  4965 W d.godeye.sampl: Accessing hidden method Landroid/widget/TextView;->getTextDirectionHeuristic()Landroid/text/TextDirectionHeuristic; (light greylist, linking)\n11-14 19:55:13.706  4965  4965 D NetworkSecurityConfig: No Network Security Config specified, using platform default\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setUseSessionTickets(Z)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setHostname(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->getAlpnSelectedProtocol()[B (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Lcom/android/org/conscrypt/OpenSSLSocketImpl;->setAlpnProtocols([B)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->get()Ldalvik/system/CloseGuard; (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->open(Ljava/lang/String;)V (light greylist, reflection)\n11-14 19:55:13.708  4965  4965 W d.godeye.sampl: Accessing hidden method Ldalvik/system/CloseGuard;->warnIfOpen()V (light greylist, reflection)\n11-14 19:55:13.717  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.732  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.765  4965  4965 D ScrollView: initGoToTop\n11-14 19:55:13.808  4965  4965 D InputTransport: Input channel constructed: fd=78\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: setView = DecorView@3b5eae1[Main2Activity] TM=true MM=false\n11-14 19:55:13.808  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: MSG_WINDOW_FOCUS_CHANGED 0 1\n11-14 19:55:13.808  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@cb83bce[SplashActivity]\n11-14 19:55:13.809  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.837  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: dispatchAttachedToWindow\n11-14 19:55:13.855  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: Relayout returned: old=[0,0][1440,3040] new=[0,0][1440,3040] result=0x7 surface={valid=true 516583780352} changed=true\n11-14 19:55:13.858  4965  5077 D OpenGLRenderer: eglCreateWindowSurface = 0x7855d56880, 0x7846caf010\n11-14 19:55:13.863  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.864  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_RESIZED: frame=Rect(0, 0 - 1440, 3040) ci=Rect(0, 150 - 0, 53) vi=Rect(0, 150 - 0, 53) or=1\n11-14 19:55:13.903  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: MSG_WINDOW_FOCUS_CHANGED 1 1\n11-14 19:55:13.919  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.919  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: prepareNavigationBarInfo() DecorView@3b5eae1[Main2Activity]\n11-14 19:55:13.927  4965  4965 D InputMethodManager: getNavigationBarColor() -855310\n11-14 19:55:13.927  4965  4965 D InputMethodManager: startInputInner - Id : 0\n11-14 19:55:13.927  4965  4965 I InputMethodManager: startInputInner - mService.startInputOrWindowGainedFocus\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel constructed: fd=81\n11-14 19:55:13.928  4965  4965 D InputTransport: Input channel destroyed: fd=72\n11-14 19:55:13.929  4965  4965 W System.err: cn.hikyson.godeye.core.exceptions.UninstallException: module [STARTUP] is not installed.\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEye.getModule(GodEye.java:275)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.godeye.core.GodEyeHelper.onAppStartEnd(GodEyeHelper.java:71)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.StartupTracer.lambda$null$0(StartupTracer.java:47)\n11-14 19:55:13.929  4965  4965 W System.err: \tat cn.hikyson.android.godeye.sample.-$$Lambda$StartupTracer$GTWyjZ1TjmYPllNgrq9MHEXacpQ.run(Unknown Source:2)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.handleCallback(Handler.java:873)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Handler.dispatchMessage(Handler.java:99)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.os.Looper.loop(Looper.java:214)\n11-14 19:55:13.929  4965  4965 W System.err: \tat android.app.ActivityThread.main(ActivityThread.java:7037)\n11-14 19:55:13.929  4965  4965 W System.err: \tat java.lang.reflect.Method.invoke(Native Method)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:494)\n11-14 19:55:13.929  4965  4965 W System.err: \tat com.android.internal.os.ZygoteInit.main(ZygoteInit.java:965)\n11-14 19:55:14.220  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6b80, 0x785fcfb000\n11-14 19:55:14.228  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Relayout returned: old=[0,150][1440,2987] new=[0,150][1440,2987] result=0x5 surface={valid=false 0} changed=true\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: setWindowStopped(true) old=false\n11-14 19:55:14.229  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.WindowManagerGlobal.setStoppedState:669 android.app.Activity.performStop:7690 android.app.ActivityThread.callActivityOnStop:4378 android.app.ActivityThread.performStopActivityInner:4356 android.app.ActivityThread.handleStopActivity:4431 android.app.servertransaction.TransactionExecutor.performLifecycleSequence:192 android.app.servertransaction.TransactionExecutor.cycleToPath:165 android.app.servertransaction.TransactionExecutor.executeLifecycleState:142 \n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: dispatchDetachedFromWindow\n11-14 19:55:14.232  4965  4965 D ViewRootImpl@fcbf6a6[SplashActivity]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.app.ActivityThread.handleDestroyActivity:4752 android.app.servertransaction.DestroyActivityItem.execute:39 android.app.servertransaction.TransactionExecutor.executeLifecycleState:145 \n11-14 19:55:14.235  4965  4965 D InputTransport: Input channel destroyed: fd=66\n11-14 19:55:14.496  4965  5077 D OpenGLRenderer: eglDestroySurface = 0x7846da6d00, 0x7846fb4000\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: dispatchDetachedFromWindow\n11-14 19:55:14.496  4965  4965 D ViewRootImpl@a69733d[Toast]: Surface release. android.view.ViewRootImpl.doDie:7967 android.view.ViewRootImpl.die:7935 android.view.WindowManagerGlobal.removeViewLocked:497 android.view.WindowManagerGlobal.removeView:435 android.view.WindowManagerImpl.removeViewImmediate:124 android.widget.Toast$TN.handleHide:1158 android.widget.Toast$TN$1.handleMessage:946 android.os.Handler.dispatchMessage:106 \n11-14 19:55:14.503  4965  4965 D InputTransport: Input channel destroyed: fd=67\n11-14 19:55:16.713  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:16.755  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:16.801  4965  4965 D AndroidGodEye: DEBUG: Cpu installed\n11-14 19:55:16.804  4965  4965 D AndroidGodEye: DEBUG: Battery installed.\n11-14 19:55:16.806  4965  4965 D AndroidGodEye: DEBUG: Fps installed.\n11-14 19:55:16.818  4965  4965 D AndroidGodEye: DEBUG: LeakDetector installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Heap installed.\n11-14 19:55:16.819  4965  4965 D AndroidGodEye: DEBUG: Pss installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Ram installed.\n11-14 19:55:16.820  4965  4965 D AndroidGodEye: DEBUG: Network installed.\n11-14 19:55:16.821  4965  4965 D AndroidGodEye: DEBUG: Sm installed\n11-14 19:55:16.822  4965  4965 D AndroidGodEye: DEBUG: Startup installed.\n11-14 19:55:16.823  4965  4965 D AndroidGodEye: DEBUG: Traffic installed.\n11-14 19:55:16.831  4965  4965 D AndroidGodEye: DEBUG: Crash installed.\n11-14 19:55:16.832  4965  4965 D AndroidGodEye: DEBUG: ThreadDump installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: Pageload installed.\n11-14 19:55:16.834  4965  4965 D AndroidGodEye: DEBUG: MethodCanary installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: AppSize installed.\n11-14 19:55:16.835  4965  4965 D AndroidGodEye: DEBUG: View canary size installed.\n11-14 19:55:16.836  4965  4965 D AndroidGodEye: DEBUG: GodEye install, godEyeConfig: GodEyeConfig{mCpuConfig=CpuConfig{intervalMillis=2000}, mBatteryConfig=BatteryConfig{}, mFpsConfig=FpsConfig{intervalMillis=2000}, mLeakConfig=LeakConfig{debug=true, debugNotification=true, leakRefInfoProvider=cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider}, mHeapConfig=HeapConfig{intervalMillis=2000}, mPssConfig=PssConfig{intervalMillis=2000}, mRamConfig=RamConfig{intervalMillis=3000}, mNetworkConfig=NetworkConfig{}, mSmConfig=SmConfig{debugNotification=true, longBlockThresholdMillis=500, shortBlockThresholdMillis=500, dumpIntervalMillis=1000}, mStartupConfig=StartupConfig{}, mTrafficConfig=TrafficConfig{intervalMillis=2000, sampleMillis=1000}, mCrashConfig=CrashConfig{immediate=false}, mThreadConfig=ThreadConfig{intervalMillis=2000, threadFilter=cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter}, mPageloadConfig=PageloadConfig{pageInfoProvider=cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider}, mMethodCanaryConfig=MethodCanaryConfig{maxMethodCountSingleThreadByCost=300, lowCostMethodThresholdMillis=10}, mAppSizeConfig=cn.hikyson.godeye.core.GodEyeConfig$AppSizeConfig@2b60f5a}, cost 68 ms\n11-14 19:55:16.842  4965  5334 D AndroidGodEye: DEBUG: AppSize onGetSize: cache size: 1.51MB, data size: 3.09MB, codeSize: 10.66MB\n11-14 19:55:17.194  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.235  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.269  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:17.813  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:17.861  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:17.890  4965  5339 D TcpOptimizer: TcpOptimizer-ON\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: Open AndroidGodEye dashboard [ http://localhost:5390/index.html ] or [ http://10.38.28.208:5390/index.html ] in your browser\n11-14 19:55:17.895  4965  4965 D AndroidGodEye: DEBUG: GodEye monitor is working...\n11-14 19:55:25.373  4965  5339 E AndroidGodEye: !ERROR: java.io.FileNotFoundException: android-godeye-dashboard/static/js/main.4f4e3555.js\n11-14 19:55:34.254  4965  5339 D AndroidGodEye: DEBUG: ModuleDriver start running.\n11-14 19:55:34.274  4965  5339 D AndroidGodEye: DEBUG: connection build. current count:1\n11-14 19:55:34.280  4965  5324 W d.godeye.sampl: Accessing hidden field Lsun/misc/Unsafe;->theUnsafe:Lsun/misc/Unsafe; (light greylist, reflection)\n11-14 19:55:45.021  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:45.068  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:45.105  4965  4965 D ScrollView:  onsize change changed \n11-14 19:55:48.785  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 0\n11-14 19:55:48.823  4965  4965 D ViewRootImpl@86c0ea[Main2Activity]: ViewPostIme pointer 1\n11-14 19:55:48.849  4965  4965 D AndroidRuntime: Shutting down VM\n--------- tail end of log system (/system/bin/logcat -b system -d -v threadtime -t 50 --pid 4965 *:W)\n--------- tail end of log events (/system/bin/logcat -b events -d -v threadtime -t 50 --pid 4965 *:I)\n11-14 19:55:12.495  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performCreate]\n11-14 19:55:12.498  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,handleStartActivity]\n11-14 19:55:12.499  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,RESUME_ACTIVITY]\n11-14 19:55:13.524  4965  4965 I am_on_paused_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performPause]\n11-14 19:55:13.710  4965  4965 I am_on_create_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,performCreate]\n11-14 19:55:13.797  4965  4965 I am_on_start_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,handleStartActivity]\n11-14 19:55:13.798  4965  4965 I am_on_resume_called: [0,cn.hikyson.android.godeye.sample.Main2Activity,RESUME_ACTIVITY]\n11-14 19:55:14.229  4965  4965 I am_on_stop_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,LIFECYCLER_STOP_ACTIVITY]\n11-14 19:55:14.231  4965  4965 I am_on_destroy_called: [0,cn.hikyson.android.godeye.sample.SplashActivity,performDestroy]\n",
                    "App version": "1.0",
                    "OS version": "9",
                    "other threads": "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16921, name: FinalizerWatchdogDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Thread.sleep(Native Method)\n    at java.lang.Thread.sleep(Thread.java:373)\n    at java.lang.Thread.sleep(Thread.java:314)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.sleepFor(Daemons.java:342)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.waitForFinalization(Daemons.java:364)\n    at java.lang.Daemons$FinalizerWatchdogDaemon.runInternal(Daemons.java:281)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16924, name: Binder:4965_2  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16923, name: Binder:4965_1  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16925, name: Binder:4965_3  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\npid: 4965, tid: 16920, name: FinalizerDaemon  >>> cn.hikyson.android.godeye.sample <<<\n\njava stacktrace:\n    at java.lang.Object.wait(Native Method)\n    at java.lang.Object.wait(Object.java:422)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:188)\n    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:209)\n    at java.lang.Daemons$FinalizerDaemon.runInternal(Daemons.java:232)\n    at java.lang.Daemons$Daemon.run(Daemons.java:103)\n    at java.lang.Thread.run(Thread.java:764)\n\ntotal JVM threads (exclude the crashed thread): 35\nJVM threads matched whitelist: 5\nJVM threads ignored by max count limit: 0\ndumped JVM threads:5\n",
                    "Brand": "samsung",
                    "App ID": "cn.hikyson.android.godeye.sample",
                    "Build fingerprint": "samsung/beyond1qltezc/beyond1q:9/PPR1.180610.011/G9730ZCS2ASJU:user/release-keys",
                    "open files": "fd 0: /dev/null\nfd 1: /dev/null\nfd 2: /dev/null\nfd 3: /proc/4965/fd/3\nfd 4: /proc/4965/fd/4\nfd 5: /sys/kernel/debug/tracing/trace_marker\nfd 6: /dev/null\nfd 7: /dev/null\nfd 8: /dev/null\nfd 9: /system/framework/core-oj.jar\nfd 10: /system/framework/core-libart.jar\nfd 11: /dev/binder\nfd 12: /system/framework/conscrypt.jar\nfd 13: /system/framework/okhttp.jar\nfd 14: /system/framework/bouncycastle.jar\nfd 15: /system/framework/apache-xml.jar\nfd 16: /system/framework/ext.jar\nfd 17: /system/framework/framework.jar\nfd 18: /system/framework/telephony-common.jar\nfd 19: /system/framework/voip-common.jar\nfd 20: /system/framework/ims-common.jar\nfd 21: /system/framework/sprengine.jar\nfd 22: /system/framework/android.hidl.base-V1.0-java.jar\nfd 23: /system/framework/android.hidl.manager-V1.0-java.jar\nfd 24: /system/framework/knoxsdk.jar\nfd 25: /system/framework/timakeystore.jar\nfd 26: /system/framework/fipstimakeystore.jar\nfd 27: /system/framework/sec_edm.jar\nfd 28: /system/framework/knoxanalyticssdk.jar\nfd 29: /system/framework/smartbondingservice.jar\nfd 30: /system/framework/ucmopensslenginehelper.jar\nfd 31: /system/framework/esecomm.jar\nfd 32: /system/framework/securetimersdk.jar\nfd 33: /system/framework/sec_sdp_sdk.jar\nfd 34: /system/framework/sec_sdp_hidden_sdk.jar\nfd 35: /system/framework/framework-oahl-backward-compatibility.jar\nfd 36: /system/framework/android.test.base.jar\nfd 37: /system/framework/knoxvpnuidtag.jar\nfd 38: /system/framework/SemAudioThumbnail.jar\nfd 39: /system/framework/knoxguard.jar\nfd 40: /system/framework/tcmiface.jar\nfd 41: /system/framework/telephony-ext.jar\nfd 42: /system/framework/libprotobuf-java_mls.jar\nfd 43: /system/framework/drutils.jar\nfd 44: /system/framework/QPerformance.jar\nfd 45: /system/framework/UxPerformance.jar\nfd 46: /system/framework/framework-res.apk\nfd 47: /proc/4965/fd/47\nfd 48: /vendor/overlay/framework-res__auto_generated_rro.apk\nfd 49: /proc/4965/fd/49\nfd 50: /proc/4965/fd/50\nfd 51: /proc/4965/fd/51\nfd 52: /proc/4965/fd/52\nfd 53: /proc/4965/fd/53\nfd 54: /proc/4965/fd/54\nfd 55: /proc/4965/fd/55\nfd 56: /data/app/cn.hikyson.android.godeye.sample-edodD1MZaK-sCRMbJpr_Wg==/base.apk\nfd 57: /dev/ashmem\nfd 58: /dev/ashmem\nfd 59: /dev/ashmem\nfd 60: /proc/4965/fd/60\nfd 61: /proc/4965/fd/61\nfd 62: /proc/4965/fd/62\nfd 63: /proc/4965/fd/63\nfd 64: /dev/ion\nfd 65: /proc/4965/fd/65\nfd 66: /proc/4965/fd/66\nfd 67: /dev/null\nfd 68: /dev/kgsl-3d0\nfd 69: /dev/ion\nfd 70: /dev/hwbinder\nfd 71: /proc/4965/fd/71\nfd 72: /proc/4965/fd/72\nfd 73: /sys/kernel/debug/tracing/trace_marker\nfd 74: /proc/4965/fd/74\nfd 75: /proc/4965/fd/75\nfd 76: /proc/4965/fd/76\nfd 77: /dev/null\nfd 78: /proc/4965/fd/78\nfd 79: /proc/4965/fd/79\nfd 80: /proc/4965/fd/80\nfd 81: /proc/4965/fd/81\nfd 82: /proc/4965/fd/82\nfd 83: /dev/null\nfd 84: /proc/4965/fd/84\nfd 85: /proc/4965/fd/85\nfd 86: /proc/4965/fd/86\nfd 87: /proc/4965/fd/87\nfd 88: /dev/null\nfd 89: /proc/4965/fd/89\nfd 90: /proc/4965/fd/90\nfd 91: /proc/4965/fd/91\nfd 92: /proc/4965/fd/92\nfd 93: /proc/4965/fd/93\nfd 94: /proc/4965/fd/94\nfd 95: /proc/4965/fd/95\nfd 96: /proc/4965/fd/96\nfd 97: /proc/4965/fd/97\nfd 98: /proc/4965/fd/98\nfd 99: /proc/4965/fd/99\nfd 100: /proc/4965/fd/100\nfd 101: /proc/4965/fd/101\nfd 102: /proc/4965/fd/102\nfd 103: /proc/4965/fd/103\nfd 104: /proc/4965/fd/104\nfd 105: /proc/4965/fd/105\nfd 106: /proc/4965/fd/106\nfd 107: /proc/4965/fd/107\nfd 108: /proc/4965/fd/108\nfd 109: /data/data/cn.hikyson.android.godeye.sample/files/tombstones/tombstone_00001573732516829000_1.0__cn.hikyson.android.godeye.sample.java.xcrash\nfd 110: /proc/4965/fd/110\nfd 111: /proc/4965/fd/111\nfd 112: /proc/4965/fd/112\nfd 113: /proc/4965/fd/113\nfd 114: /proc/4965/fd/114\n(number of FDs: 115)\n",
                    "Manufacturer": "samsung",
                    "Model": "SM-G9730",
                    "ABI list": "arm64-v8a,armeabi-v7a,armeabi",
                    "memory info": " System Summary (From: /proc/meminfo)\n  MemTotal:        7656764 kB\n  MemFree:         1294452 kB\n  MemAvailable:    3271272 kB\n  Buffers:          210272 kB\n  Cached:          2835852 kB\n  SwapCached:         6288 kB\n  Active:          2095424 kB\n  Inactive:        1511904 kB\n  Active(anon):     872904 kB\n  Inactive(anon):   573444 kB\n  Active(file):    1222520 kB\n  Inactive(file):   938460 kB\n  Unevictable:      886720 kB\n  Mlocked:          886804 kB\n  RbinTotal:        327680 kB\n  RbinAlloced:        5128 kB\n  RbinPool:              0 kB\n  RbinFree:          52200 kB\n  SwapTotal:       2621436 kB\n  SwapFree:        2136632 kB\n  Dirty:                72 kB\n  Writeback:             0 kB\n  AnonPages:       1443228 kB\n  Mapped:          1556364 kB\n  Shmem:              2568 kB\n  Slab:             342904 kB\n  SReclaimable:     104360 kB\n  SUnreclaim:       238544 kB\n  KernelStack:       56416 kB\n  PageTables:        96420 kB\n  NFS_Unstable:          0 kB\n  Bounce:                0 kB\n  WritebackTmp:          0 kB\n  CommitLimit:     6449816 kB\n  Committed_AS:   130354860 kB\n  VmallocTotal:   263061440 kB\n  VmallocUsed:           0 kB\n  VmallocChunk:          0 kB\n  CmaTotal:         270336 kB\n  CmaFree:           95292 kB\n-\n Process Status (From: /proc/PID/status)\n  Name:\td.godeye.sample\n  Umask:\t0077\n  State:\tR (running)\n  Tgid:\t4965\n  Ngid:\t0\n  Pid:\t4965\n  PPid:\t693\n  TracerPid:\t0\n  Uid:\t10431\t10431\t10431\t10431\n  Gid:\t10431\t10431\t10431\t10431\n  FDSize:\t128\n  Groups:\t3003 9997 20431 50431\n  NStgid:\t4965\n  NSpid:\t4965\n  NSpgid:\t693\n  NSsid:\t0\n  VmPeak:\t 4430764 kB\n  VmSize:\t 3956848 kB\n  VmLck:\t       0 kB\n  VmPin:\t       0 kB\n  VmHWM:\t  184796 kB\n  VmRSS:\t  180580 kB\n  RssAnon:\t   83420 kB\n  RssFile:\t   96708 kB\n  RssShmem:\t     452 kB\n  VmData:\t 1262172 kB\n  VmStk:\t    8192 kB\n  VmExe:\t      80 kB\n  VmLib:\t  145324 kB\n  VmPTE:\t    1164 kB\n  VmPMD:\t      32 kB\n  VmSwap:\t    4480 kB\n  Threads:\t40\n  SigQ:\t0/27421\n  SigPnd:\t0000000000000000\n  ShdPnd:\t0000000000000000\n  SigBlk:\t0000000000001200\n  SigIgn:\t0000000000000001\n  SigCgt:\t00000006400084fc\n  CapInh:\t0000000000000000\n  CapPrm:\t0000000000000000\n  CapEff:\t0000000000000000\n  CapBnd:\t0000000000000000\n  CapAmb:\t0000000000000000\n  NoNewPrivs:\t0\n  Seccomp:\t2\n  Speculation_Store_Bypass:\tunknown\n  Cpus_allowed:\tff\n  Cpus_allowed_list:\t0-7\n  Mems_allowed:\t1\n  Mems_allowed_list:\t0\n  voluntary_ctxt_switches:\t2444\n  nonvoluntary_ctxt_switches:\t343\n-\n Process Limits (From: /proc/PID/limits)\n  Limit                     Soft Limit           Hard Limit           Units\n  Max cpu time              unlimited            unlimited            seconds\n  Max file size             unlimited            unlimited            bytes\n  Max data size             unlimited            unlimited            bytes\n  Max stack size            8388608              unlimited            bytes\n  Max core file size        0                    unlimited            bytes\n  Max resident set          unlimited            unlimited            bytes\n  Max processes             27421                27421                processes\n  Max open files            32768                32768                files\n  Max locked memory         67108864             67108864             bytes\n  Max address space         unlimited            unlimited            bytes\n  Max file locks            unlimited            unlimited            locks\n  Max pending signals       27421                27421                signals\n  Max msgqueue size         819200               819200               bytes\n  Max nice priority         40                   40\n  Max realtime priority     0                    0\n  Max realtime timeout      unlimited            unlimited            us\n-\n Process Summary (From: android.os.Debug.MemoryInfo)\n                       Pss(KB)\n                        ------\n           Java Heap:    55364\n         Native Heap:    15960\n                Code:     9336\n               Stack:      112\n            Graphics:     4788\n       Private Other:     3812\n              System:     3720\n               TOTAL:    93092           TOTAL SWAP:     4236\n",
                    "Tombstone maker": "xCrash 2.4.6",
                    "Rooted": "No",
                    "API level": "28"
                }
            }

        ]);
        this.recvFun("blockInfo", {
            blockTime: 200,
            blockBaseinfo: { df: "sdf", vvv: "1312", bb: ["fewefwf", "fwewfe"] }
        });
        this.recvFun("blockInfo", {
            blockTime: 300,
            blockBaseinfo: { ss: "111", dd: "333", aa: ["11", "22"] }
        });
        this.recvFun("blockConfig", {
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
                { name: "DNS", time: 200 },
                { name: "RequestHeader", time: 300 },
                { name: "ResponseBody", time: 500 },

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
        this.recvFun("imageIssue", {
            imageViewHashCode: 312,
            bitmapWidth: 238,
            bitmapHeight: 114,
            imageViewWidth: 238,
            imageViewHeight: 114,
            timestamp: 1572861420958,
            activityClassName: "cn.hikyson.android.godeye.sample.LeakActivity",
            activityHashCode: 123,
            issueType: "BITMAP_QUALITY_TOO_HIGH"
        });
        this.recvFun("viewIssueInfo", {
            "activityName": "cn.hikyson.android.godeye.sample.LeakActivity",
            "maxDepth": 4,
            "overDrawAreas": [{
                "overDrawTimes": 2,
                "rect": { "bottom": 508, "left": 0, "right": 963, "top": 332 }
            }, {
                "overDrawTimes": 1,
                "rect": { "bottom": 2340, "left": 0, "right": 1080, "top": 228 }
            }, { "overDrawTimes": 1, "rect": { "bottom": 96, "left": 0, "right": 1080, "top": 0 } }, {
                "overDrawTimes": 2,
                "rect": { "bottom": 206, "left": 33, "right": 121, "top": 118 }
            }, { "overDrawTimes": 1, "rect": { "bottom": 228, "left": 0, "right": 1080, "top": 96 } }],
            "screenHeight": 2210,
            "screenWidth": 1080,
            "timestamp": 1572861420958,
            "views": [{
                "className": "android.widget.TextView",
                "depth": 4,
                "hasBackground": true,
                "id": "2131230746",
                "isViewGroup": false,
                "rect": { "bottom": 332, "left": 0, "right": 1080, "top": 228 },
                "text": "Leak will happen when you finish this activity and wait for a moment.",
                "textSize": 39.0
            }, {
                "className": "android.widget.Button",
                "depth": 3,
                "hasBackground": true,
                "id": "2131230819",
                "isViewGroup": false,
                "rect": { "bottom": 508, "left": 0, "right": 963, "top": 332 },
                "text": "Leak fragment (Android O and above)",
                "textSize": 50.0
            }, {
                "className": "android.widget.TextView",
                "depth": 6,
                "hasBackground": true,
                "id": "16908688",
                "isViewGroup": false,
                "rect": { "bottom": 196, "left": 132, "right": 502, "top": 128 },
                "text": "AndroidGodEye",
                "textSize": 50.0
            }, {
                "className": "android.widget.ImageView",
                "depth": 6,
                "hasBackground": true,
                "id": "16908332",
                "isViewGroup": false,
                "rect": { "bottom": 206, "left": 33, "right": 121, "top": 118 },
                "textSize": 0.0
            }, {
                "className": "android.widget.LinearLayout",
                "depth": 3,
                "hasBackground": true,
                "id": "-1",
                "isViewGroup": true,
                "rect": { "bottom": 2340, "left": 0, "right": 1080, "top": 228 },
                "textSize": 0.0
            }, {
                "className": "android.view.View",
                "depth": 1,
                "hasBackground": true,
                "id": "16908335",
                "isViewGroup": false,
                "rect": { "bottom": 96, "left": 0, "right": 1080, "top": 0 },
                "textSize": 0.0
            }, {
                "className": "com.android.internal.widget.ActionBarContainer",
                "depth": 2,
                "hasBackground": true,
                "id": "16908685",
                "isViewGroup": true,
                "rect": { "bottom": 228, "left": 0, "right": 1080, "top": 96 },
                "textSize": 0.0
            }]
        });
        this.recvFun("leakInfo", {
            createdTimeMillis: 1583501829434,
            durationTimeMillis: 234241,
            leakSignature: "leakSignature",
            leakObject: "leakObject",
            leakSizeByte: 2132,
            leakTrace: ["leakStack", "leakStack", "leakStack", "leakStack", "leakStack"],
            detail: "this is detail\nthis is detail\nthis is detail\nthis is detail\nthis is detail\nthis is detail\nthis is detail\n"
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
                threadTag: "UNKNOWN",
                parent: { name: "system" }
            },
            {
                id: 1,
                name: "name",
                state: "state",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
                threadTag: "APP"
            }, {
                id: 1,
                name: "name",
                state: "state",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
                threadTag: "SYSTEM"
            }
        ]);
    }
}


export default Mock;
