package cn.hikyson.godeye.core.internal.notification;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

class NotificationConsumer implements Consumer<NotificationContent> {
    private NotificationListener mNotification;

    NotificationConsumer(NotificationListener notifications) {
        mNotification = notifications;
    }

    @Override
    public void accept(NotificationContent notificationContent) throws Exception {
        ThreadUtil.ensureWorkThread("NotificationConsumer");
        if (mNotification == null) {
            return;
        }
        mNotification.onNotificationReceive(System.currentTimeMillis(), notificationContent);
    }
}
