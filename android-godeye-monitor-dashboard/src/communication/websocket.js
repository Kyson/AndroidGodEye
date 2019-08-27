
class GlobalWs {

    constructor() {
        this._receiveMessage = this._receiveMessage.bind(this);
        this.wsCallbacks = new Set();
    }

    registerCallback(callback) {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            callback()
        }
        this.wsCallbacks.add(callback)
    }

    unregisterCallback(callback) {
        this.wsCallbacks.delete(callback)
    }

    start() {
        this.ws = new WebSocket("ws://" + window.location.host + "/refresh");
        this.ws.addEventListener('open', () => {
            if (this.wsCallbacks) {
                this.wsCallbacks.forEach((element) => {
                    element()
                })
            }
        });
        this.ws.addEventListener('message', this._receiveMessage);
    }

    sendMessage(message) {
        this.ws.send(message);
    }

    _receiveMessage(e) {
        let message = JSON.parse(e.data);
        if (this.receiveMessageCallback) {
            if (message.code === 1) {
                this.receiveMessageCallback(message.data.moduleName, message.data.payload);
            } else {
                console.log('Error for _receiveMessage:', message)
            }
        }
    }

    setReceiveMessageCallback(callback) {
        this.receiveMessageCallback = callback;
    }
}

const globalWs = new GlobalWs();

export default globalWs;