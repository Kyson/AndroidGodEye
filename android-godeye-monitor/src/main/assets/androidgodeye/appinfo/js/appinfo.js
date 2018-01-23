/**
 * Created by kysonchao on 2017/11/30.
 */
'use strict';
$(document).ready(function () {
    refresh();
    setInterval(refresh, interval)
});

var interval = 5000;

function refresh() {
    requestUtil.getData("/appinfo", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(data) {
    if (data) {
        $("#app_info_name").text(data.appName);
    } else {
        $("#app_info_name").text("**");
    }
    var extensions = [];
    if (data && data.extentions && data.extentions.length > 0) {
        extensions = data.extentions;
    }
    $(".appinfo-label").remove();
    for (var i = 0; i < extensions.length; i++) {
        var label = "<span class=\"label label-success appinfo-label\" >" + extensions[i] + "</span>\n";
        $("#app_info_label_first").after(label);
    }
}