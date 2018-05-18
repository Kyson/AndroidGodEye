/**
 * Created by kysonchao on 2017/11/27.
 */
'use strict';
var requestUtil = function () {

    function getData(path, callbackSuccess, callbackFail) {
        $.ajax({
            url: path,
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

    function openWebSocket() {
        if ("WebSocket" in window) {
            // 打开一个 web socket
            require(['EventBus'], function (EventBus) {
                var ws = new WebSocket("ws://localhost:5390/live");
                ws.onopen = function () {
                    ws.send("发送数据");
                };
                ws.onmessage = function (evt) {
                    processMessage(EventBus, evt.data);
                };
                ws.onclose = function () {
                    alert("连接已关闭...");
                };
            });
        }
        else {
            // 浏览器不支持 WebSocket
            alert("您的浏览器不支持 WebSocket!");
        }
    }

    function processMessage(EventBus, message) {
        var messageModel = JSON.parse(message);
        if (result.code === 1) {
            var moduleName = messageModel.data.moduleName;
            var payload = messageModel.data.payload;
            EventBus.dispatch(moduleName, this, payload);
        } else {
        }
    }

    return {
        getData: getData,
        openWebSocket: openWebSocket
    }
}();
