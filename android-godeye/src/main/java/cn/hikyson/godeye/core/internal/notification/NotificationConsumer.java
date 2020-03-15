package cn.hikyson.godeye.core.internal.notification;

import java.util.List;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

class NotificationConsumer implements Consumer<NotificationContent> {
    private List<NotificationListener> mNotifications;

    NotificationConsumer(List<NotificationListener> notifications) {
        mNotifications = notifications;
    }

    @Override
    public void accept(NotificationContent notificationContent) throws Exception {
        ThreadUtil.ensureWorkThread("NotificationConsumer");
        if (mNotifications == null) {
            return;
        }
        Object[] notifications = mNotifications.toArray();
        for (Object o : notifications) {
            ((NotificationListener) o).onNotificationReceive(notificationContent);
        }
    }
}
