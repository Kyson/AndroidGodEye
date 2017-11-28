/**
 * Created by kysonchao on 2017/8/31.
 */
'use strict';

$(document).ready(function () {
    cpuUtil.setup(document.getElementById('cpuchart'));
    heapUtil.setup(document.getElementById('heapchart'));
    pssUtil.setup(document.getElementById('psschart'));
    trafficUtil.setup(document.getElementById('trafficchart'));
    blockUtil.setup(document.getElementById('blockchart'));
    pageloadUtil.setup(document.getElementById('pageloadchart'));

    setSeesionClickLisener();
    startSeesion();
    setInterval(refreshSessionTick, REFRESH_INTERVAL);
    setInterval(refreshSessionEnd, REFRESH_INTERVAL);

    setFpsClickLisener();
    startFps();

    setInterval(refreshStartup, REFRESH_INTERVAL);
    setInterval(refreshBlock, REFRESH_INTERVAL);
    setInterval(refreshPageload, REFRESH_INTERVAL);
});

var REFRESH_INTERVAL = 2000;

function refreshStartup() {
    $.ajax({
        url: "startup",
        success: function (result) {
            result = JSON.parse(result);
            var startupResult;
            if (result.code === 1) {
                startupResult = "启动类型: " + result.data.startupType + " , 耗时：" + result.data.startupTime + " ms"
            } else {
                startupResult = "***"
            }
            $("#startup").text(startupResult);
        }
    });
}


var fpsTickTimerId;

function startFps() {
    sendStartRefreshFrame();
    fpsTickTimerId = setInterval(refreshFps, REFRESH_INTERVAL);
}

function stopFps() {
    sendStopRefreshFrame();
    clearInterval(fpsTickTimerId);
}

function refreshFps() {
    $.ajax({
        url: "fps",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                fpsUtil.refreshFps(result.data[0], result.data[1]);
            } else {
                fpsUtil.refreshFps("*", "*");
            }
        },
        error: function () {
            fpsUtil.refreshFps("*", "*");
        }
    });
}

function refreshBlock() {
    $.ajax({
        url: "block",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                blockUtil.refreshBlock(result.data);
            }
            // else {
            //     blockUtil.refreshBlock([{blockTime: 0}]);
            // }
        },
        error: function () {

        }
    });
}

function refreshPageload() {
    $.ajax({
        url: "pageLoad",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                pageloadUtil.refreshPageload(result.data);
            }
        },
        error: function () {
        }
    });
}

function setSeesionClickLisener() {
    $("#StartSessionBtn").click(function () {
        startSeesion();
    });
    $("#StopSessionBtn").click(function () {
        stopSeesion();
    });
}

var sessionTickTimerId;

function startSeesion() {
    sendStartSession();
    sessionTickTimerId = setInterval(sendTickSession, REFRESH_INTERVAL);
}

function stopSeesion() {
    clearInterval(sessionTickTimerId);
    sendStopSession();
}


function sendStartSession() {
    sendCommand("StartSession", function () {
        $("#sessionStatus").text("session started");
    }, function () {
        $("#sessionStatus").text("session start fail!!!");
    });
}

function sendTickSession() {
    sendCommand("TickSession", function () {
        $("#sessionStatus").text("session ticking...");
    }, function () {
        $("#sessionStatus").text("session tick fail!!!");
    });
}

function sendStopSession() {
    sendCommand("StopSession", function () {
        $("#sessionStatus").text("session stopped");
    }, function () {
        $("#sessionStatus").text("session stop fail!!!");
    });
}

function setFpsClickLisener() {
    $("#StartRefreshFrameBtn").click(function () {
        startFps()
    });
    $("#StopRefreshFrameBtn").click(function () {
        stopFps();
    });
}

function sendStartRefreshFrame() {
    sendCommand("StartRefreshFrame", function () {
        $("#fpsStatus").text("fps refreshing...");
    }, function () {
        $("#fpsStatus").text("fps start failed!!!");
    });
}

function sendStopRefreshFrame() {
    sendCommand("StopRefreshFrame", function () {
        $("#fpsStatus").text("fps stopped");
    }, function () {
        $("#fpsStatus").text("fps stop failed!!!");
    });
}


function sendCommand(command, callbackSuccess, callbackFail) {
    $.ajax({
        url: "command?its=" + command,
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                callbackSuccess();
            } else {
                callbackFail();
            }
        },
        error: function (e) {
            callbackFail();
        }
    });
}


function refreshSessionTick() {
    getSessionTickData(function (tickDatas) {
        console.log("get session tick data success");
        console.log(tickDatas);

        var cpuInfos = [];
        var trafficInfos = [];
        var smResults = [];
        var heapInfos = [];
        var pssInfos = [];
        var ramInfos = [];

        for (var i = 0; i < tickDatas.length; i++) {
            var tickData = tickDatas[i];
            if (tickData.cpuInfo) {
                cpuInfos.push(tickData.cpuInfo);
            }
            trafficInfos.push(tickData.trafficInfo);
            smResults.push(tickData.smResult);

            if (tickData.memoryDetailInfo) {
                if (tickData.memoryDetailInfo.dalvikHeapMem) {
                    heapInfos.push(tickData.memoryDetailInfo.dalvikHeapMem)
                }
                if (tickData.memoryDetailInfo.pssInfo) {
                    pssInfos.push(tickData.memoryDetailInfo.pssInfo)
                }
                if (tickData.memoryDetailInfo.ramMemoryInfo) {
                    ramInfos.push(tickData.memoryDetailInfo.ramMemoryInfo)
                }
            }
        }
        if (cpuInfos.length > 0) {
            cpuUtil.refreshChart(cpuInfos);
        }
        if (heapInfos.length > 0) {
            heapUtil.refreshChart(heapInfos);
        }
        if (pssInfos.length > 0) {
            pssUtil.refreshChart(pssInfos);
        }
        if (ramInfos.length > 0) {
            ramUtil.refreshRam(ramInfos[ramInfos.length - 1]);
        }
        if (trafficInfos.length > 0) {
            trafficUtil.refreshTraffic(trafficInfos);
        }
        if (smResults.length > 0) {
            sessionSmUtil.refreshSessionSm(smResults[smResults.length - 1]);
        }
    }, function () {
        console.log("get session tick data fail");
    });
}


function getSessionTickData(callbackSuccess, callbackFail) {
    $.ajax({
        url: "sessionTick",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                callbackSuccess(result.data);
            } else {
                callbackFail();
            }
        },
        error: function (e) {
            callbackFail();
        }
    });
}


function refreshSessionEnd() {
    getSessionEndData(function (endData) {
          $("#sessionend").html(jsonFormat.syntaxHighlight(endData));
    }, function () {
          $("#sessionend").html(" ** ");
    });
}


function getSessionEndData(callbackSuccess, callbackFail) {
    $.ajax({
        url: "sessionEnd",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 1) {
                callbackSuccess(result.data);
            } else {
                callbackFail();
            }
        },
        error: function (e) {
            callbackFail();
        }
    });
}