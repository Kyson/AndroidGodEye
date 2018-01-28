/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
$(document).ready(function () {
    ramUtil.setup('ram_chart');
    setInterval(refresh, interval)
});

var interval = 6000;

function refresh() {
    requestUtil.getData("/ram", function (data) {
        ramUtil.refreshRam(data);
    }, function () {
        ramUtil.refreshRam(null);
    });
}