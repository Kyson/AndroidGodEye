/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';

$(document).ready(function () {
    //TODO KYSON
    // setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/fps", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(data) {
    if (data) {
        fpsUtil.refreshFps(data.currentFps, data.systemFps)
    } else {
        $("#fps_status").text("**/**");
    }
}