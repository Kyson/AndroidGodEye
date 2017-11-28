/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
$(document).ready(function () {
    heapUtil.setup(document.getElementById('heap_chart'));
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/heap", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(heapInfos) {
    if (heapInfos.length > 0) {
        heapUtil.refreshChart(heapInfos);
    }
}
