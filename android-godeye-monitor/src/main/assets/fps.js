/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var fpsUtil = function () {

    function refreshFps(currentRate, fineRate) {
        $("#fpsStatus").text(currentRate + "/" + fineRate);
    }

    return {
        refreshFps: refreshFps
    }

}();
