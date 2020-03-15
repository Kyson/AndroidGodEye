package cn.hikyson.godeye.monitor.notification;

import cn.hikyson.godeye.core.internal.notification.NotificationContent;
import cn.hikyson.godeye.core.internal.notification.NotificationListener;
import cn.hikyson.godeye.monitor.server.Messager;
import cn.hikyson.godeye.monitor.server.ServerMessage;

public class MonitorNotificationListener implements NotificationListener {
    private Messager mMessager;

    public MonitorNotificationListener(Messager mMessager) {
        this.mMessager = mMessager;
    }

    @Override
    public void onNotificationReceive(NotificationContent notificationContent) {
        if (this.mMessager != null) {
            this.mMessager.sendMessage(new ServerMessage("AndroidGodEyeNotification", notificationContent).toString());
        }
    }
}
