/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
$(document).ready(function () {
    trafficUtil.setup(document.getElementById('traffic_chart'));
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/traffic", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(traffics) {
    if (traffics.length > 0) {
        trafficUtil.refreshTraffic(traffics);
    }
}