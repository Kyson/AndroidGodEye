/**
 * Created by kysonchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/startup", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(startupInfo) {
    var startupResult;
    if (result.code === 1) {
        startupResult = "启动类型: " + startupInfo.startupType + " , 耗时：" + startupInfo.startupTime + " ms"
    } else {
        startupResult = "***"
    }
    $("#startup_detail").text(startupResult);
}