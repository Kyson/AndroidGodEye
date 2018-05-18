/**
 * Created by kys/onchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    pageloadUtil.setup('pageload_chart');
    //TODO KYSON
    // setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/pageload", function (data) {
        pageloadUtil.refreshPageload(data);
    }, function () {
        pageloadUtil.refreshPageload(null);
    });
}
