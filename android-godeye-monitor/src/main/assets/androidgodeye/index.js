/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';

require.config({
    paths : {
        "EventBus" : "resources/eventbus.min"
    }
});

$(document).ready(function () {
    requestUtil.openWebSocket();
});