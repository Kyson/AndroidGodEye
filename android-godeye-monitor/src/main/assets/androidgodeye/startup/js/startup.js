/**
 * Created by kysonchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    refresh();
    setInterval(refresh, interval)
});

var interval = 5000;

function refresh() {
    requestUtil.getData("/startup", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(startupInfo) {
    var startupType;
    var startupTime;
    if (startupInfo) {
        startupType = startupInfo.startupType;
        startupTime = startupInfo.startupTime;
    } else {
        startupType = "**";
        startupTime = "**";
    }
    $("#startup_detail").html("启动类型&nbsp;&nbsp;" + startupType + "&nbsp;,&nbsp;耗时&nbsp;&nbsp;" + startupTime + "&nbsp;ms");
}