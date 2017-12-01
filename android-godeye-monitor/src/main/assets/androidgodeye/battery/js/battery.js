/**
 * Created by kysonchao on 2017/11/27.
 */
'use strict';
$(document).ready(function () {
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/battery", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(data) {
    if (data) {
        var progress = data.level * 100 / data.scale;
        $("#battery_line").animate({
            width: progress + "%"
        }, 300);
        $("#battery_level").text(progress + "%");
        $("#battery_status").text(data.status);
        $("#battery_plugged").text(data.plugged);
        $("#battery_present").text(data.present);
        $("#battery_health").text(data.health);
        $("#battery_voltage").text(data.voltage);
        $("#battery_temperature").text(data.temperature);
        $("#battery_technology").text(data.technology);
    } else {
        $("#battery_line").animate({
            width: 0 + "%"
        }, 300);
        $("#battery_level").text("**");
        $("#battery_status").text("**");
        $("#battery_plugged").text("**");
        $("#battery_present").text("**");
        $("#battery_health").text("**");
        $("#battery_voltage").text("**");
        $("#battery_temperature").text("**");
        $("#battery_technology").text("**");
    }
}