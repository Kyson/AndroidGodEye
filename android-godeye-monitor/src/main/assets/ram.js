/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var ramUtil = function () {

    function refreshRam(ramInfo) {
        $("#ramallocated").text(((ramInfo.totalMem - ramInfo.availMem) / 1024).toFixed(3) + "M/" + (ramInfo.totalMem / 1024).toFixed(3) + "M");
        $("#lowmemthreshold").text((ramInfo.lowMemThreshold/1024).toFixed(3) + "M");
        var ramlowstatus = $("#ramlowstatus");
        if (ramInfo.isLowMemory) {
            ramlowstatus.removeClass("greencircle");
            ramlowstatus.addClass("redcircle");
        } else {
            ramlowstatus.addClass("greencircle");
            ramlowstatus.removeClass("redcircle");
        }
    }

    return {
        refreshRam: refreshRam
    }
}();
