/**
 * Created by kysonchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    networkUtil.setup(document.getElementById('network_chart'));
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/network", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(networkInfos) {
    if (networkInfos.length > 0) {
        networkUtil.refreshNetwork(networkInfos);
    }
}