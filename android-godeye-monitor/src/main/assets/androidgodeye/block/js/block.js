/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
$(document).ready(function () {
    blockUtil.setup(document.getElementById('block_chart'));
    setInterval(refresh, interval);
});

var interval = 3000;

function refresh() {
    requestUtil.getData("/block", function (data) {
        refreshView(data);
    }, function () {

    });
}

function refreshView(blockInfos) {
    if (blockInfos.length > 0) {
        blockUtil.refreshBlock(blockInfos);
    }
}