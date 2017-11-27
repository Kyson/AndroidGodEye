/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
$(document).ready(function () {
    cpuUtil.setup(document.getElementById('cpu_chart'));
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/cpu", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(cpuInfos) {
    if (cpuInfos.length > 0) {
        cpuUtil.refreshChart(cpuInfos);
    }
}