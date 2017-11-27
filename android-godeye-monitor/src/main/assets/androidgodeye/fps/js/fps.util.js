/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var fpsUtil = function () {

    function refreshFps(currentRate, fineRate) {
        $("#fps_status").text(currentRate + "/" + fineRate);
    }

    return {
        refreshFps: refreshFps
    }

}();
