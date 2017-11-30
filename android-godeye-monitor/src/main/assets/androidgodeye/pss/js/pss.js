/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
$(document).ready(function () {
    setInterval(refresh, interval)
});

var interval = 2000;

function refresh() {
    requestUtil.getData("/pss", function (data) {
        refreshView(data);
    }, function () {
        refreshView(null);
    });
}

function refreshView(pssInfo) {
    var dalvikProgress = 0;
    var nativeProgress = 0;
    var otherProgress = 0;
    var unknownProgress = 0;

    var dalvikText = "**";
    var nativeText = "**";
    var otherText = "**";
    var unknownText = "**";

    if (pssInfo) {
        dalvikProgress = pssInfo.dalvikPssKb * 100 / pssInfo.totalPssKb;
        nativeProgress = pssInfo.nativePssKb * 100 / pssInfo.totalPssKb;
        otherProgress = pssInfo.otherPssKb * 100 / pssInfo.totalPssKb;
        unknownProgress = 100 - dalvikProgress - nativeProgress - otherProgress;

        dalvikText = (pssInfo.dalvikPssKb / 1024).toFixed(1) + "m";
        nativeText = (pssInfo.nativePssKb / 1024).toFixed(1) + "m";
        otherText = (pssInfo.otherPssKb / 1024).toFixed(1) + "m";
        unknownText = ((pssInfo.totalPssKb - pssInfo.dalvikPssKb - pssInfo.nativePssKb - pssInfo.otherPssKb) / 1024).toFixed(1) + "m";

        // $('#pss_dalvik_line').css('top', "0%");
        $("#pss_dalvik_line").animate({
            height: dalvikProgress + "%"
        }, 300, function () {
            // $('#pss_native_line').css('top', dalvikProgress + "%");
            $("#pss_native_line").animate({
                height: nativeProgress + "%"
            }, 300, function () {
                // $('#pss_other_line').css('top', (dalvikProgress + nativeProgress) + "%");
                $("#pss_other_line").animate({
                    height: otherProgress + "%"
                }, 300, function () {
                    // $('#pss_unknown_line').css('top', (dalvikProgress + nativeProgress + otherProgress) + "%");
                    $("#pss_unknown_line").animate({
                        height: unknownProgress + "%"
                    }, 300);
                });
            });
        });
    }
    $('#pss_dalvik_text').text("dalvik:" + dalvikText);
    $('#pss_native_text').text("native:" + nativeText);
    $('#pss_other_text').text("other:" + otherText);
    $('#pss_unknown_text').text("unknown:" + unknownText);

}