/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
$(document).ready(function () {
    pssUtil.setup('pss_chart');
    setInterval(refresh, interval)
});

var interval = 6000;

function refresh() {
    requestUtil.getData("/pss", function (data) {
        pssUtil.refreshPss(data);
    }, function () {
        pssUtil.refreshPss(null);
    });
}