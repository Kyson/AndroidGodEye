/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';

$(document).ready(function () {
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/fps", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(data) {
    fpsUtil.refreshFps(data.currentFps, data.systemFps)
}