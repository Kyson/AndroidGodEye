/**
 * Created by kysonchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    pageloadUtil.setup(document.getElementById('pageload_chart'));
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/pageload", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(pageloadInfos) {
    if (pageloadInfos) {
        pageloadUtil.refreshPageload(pageloadInfos);
    }
}