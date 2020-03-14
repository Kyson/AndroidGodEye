package cn.hikyson.godeye.core.internal.notification;

import java.util.List;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

public class NotificationConsumer implements Consumer<NotificationContent> {
    private List<Notification> mNotifications;

    public NotificationConsumer(List<Notification> notifications) {
        mNotifications = notifications;
    }

    @Override
    public void accept(NotificationContent notificationContent) throws Exception {
        // TODO KYSON IMPL Name
        ThreadUtil.ensureWorkThread("");
        notify(notificationContent);
    }

    private void notify(NotificationContent notificationContent) {
        if (mNotifications == null) {
            return;
        }
        Object[] notifications = mNotifications.toArray();
        for (Object o : notifications) {
            ((Notification) o).notify(notificationContent);
        }
    }
}
