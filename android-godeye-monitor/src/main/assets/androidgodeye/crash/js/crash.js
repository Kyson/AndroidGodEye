/**
 * Created //by kysonchao on 2017/11/28.
 */
'use strict';
$(document).ready(function () {
    refresh();
    //TODO KYSON
    // setInterval(refresh, interval)
});

var interval = 5000;

function refresh() {
    requestUtil.getData("/crash", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(crashInfo) {
    if (crashInfo) {
        var time = DateFormat.format(new Date(crashInfo.timestampMillis), 'yyyy/MM/dd hh:mm:ss');
        var html = "<b>Last Crash&nbsp;[&nbsp;" + time + "&nbsp;]</b></br></br><b>Message:</b></br>" + crashInfo.throwableMessage + "</br></br>" + "<b>Stacktrace:</b>&nbsp;</br>"
        for (var j = 0; j < crashInfo.throwableStacktrace.length; j++) {
            var path = crashInfo.throwableStacktrace[j];
            html += "<small><small>" + path + "</small></small></br>"
        }
        $("#crash_detail").html(html);
    } else {
        $("#crash_detail").html("Last Crash **");
    }
}