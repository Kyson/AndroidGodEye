/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
$(document).ready(function () {
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/ram", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(ramInfo) {
    var allocatedProgress = 0;
    var allocatedText = "**/**";
    var isLowMemory = false;
    var lowMemThresholdText = "**";

    if (ramInfo) {
        allocatedProgress = (ramInfo.totalMemKb - ramInfo.availMemKb) * 100 / ramInfo.totalMemKb;
        allocatedText = ((ramInfo.totalMemKb - ramInfo.availMemKb) / 1024).toFixed(1) + "m/" + (ramInfo.totalMemKb / 1024).toFixed(1) + "m";
        lowMemThresholdText = (ramInfo.lowMemThresholdKb / 1024).toFixed(1) + "m";
        isLowMemory = ramInfo.isLowMemory;
    }

    $("#ram_allocated").animate({
        width: allocatedProgress + "%"
    }, 300);

    $("#ram_allocated_text").text(allocatedText);

    var ramlowstatus = $("#ram_low_status");
    if (isLowMemory) {
        ramlowstatus.removeClass("greencircle");
        ramlowstatus.addClass("redcircle");
    } else {
        ramlowstatus.addClass("greencircle");
        ramlowstatus.removeClass("redcircle");
    }
    $("#ram_low_memory").text(lowMemThresholdText);
}