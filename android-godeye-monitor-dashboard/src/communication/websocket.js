'use strict';

class GlobalWs {

    constructor() {
        this._receiveMessage = this._receiveMessage.bind(this);
    }

    start(onReady) {
        this.ws = new WebSocket("ws://" + window.location.host + "/refresh");
        this.ws.addEventListener('open', onReady);
        this.ws.addEventListener('message', this._receiveMessage);
    }

    sendMessage(message) {
        this.ws.send(message);
    }

    _receiveMessage(e) {
        let message = JSON.parse(e.data);
        console.log("received message:" + message);
        if (this.receiveMessageCallback) {
            if (message.code === 1) {
                this.receiveMessageCallback(message.data.moduleName, message.data.payload);
            } else {
                console.log('Error for biz:', message)
            }
        }
    }

    setReceiveMessageCallback(callback) {
        this.receiveMessageCallback = callback;
    }
}

const globalWs = new GlobalWs();

export default globalWs;