import { message } from 'antd'

class Notification {

    static handleMessage(moduleName, payload) {
        if ("AndroidGodEyeNotification" === moduleName) {
            message.error(payload.message);
        }
    }
}

export default Notification;
