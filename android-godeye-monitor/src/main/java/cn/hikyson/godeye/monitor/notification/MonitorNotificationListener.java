package cn.hikyson.godeye.monitor.notification;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.notification.NotificationContent;
import cn.hikyson.godeye.core.internal.notification.NotificationListener;
import cn.hikyson.godeye.monitor.server.Messager;
import cn.hikyson.godeye.monitor.server.ServerMessage;

public class MonitorNotificationListener implements NotificationListener {
    private Messager mMessager;

    public MonitorNotificationListener(Messager messager) {
        this.mMessager = messager;
    }

    @Override
    public void onInstalled() {
        if (this.mMessager != null) {
            this.mMessager.sendMessage(new ServerMessage("AndroidGodEyeNotification", new MonitorNotificationContent(timeMillis, notificationContent)).toString());
        }
    }

    @Override
    public void onUninstalled() {
        if (this.mMessager != null) {
            this.mMessager.sendMessage(new ServerMessage("AndroidGodEyeNotification", new MonitorNotificationContent(timeMillis, notificationContent)).toString());
        }
    }

    @Override
    public void onNotificationReceive(long timeMillis, NotificationContent notificationContent) {
        if (this.mMessager != null) {
            this.mMessager.sendMessage(new ServerMessage("AndroidGodEyeNotification", new MonitorNotificationContent(timeMillis, notificationContent)).toString());
        }
    }

    @Keep
    private static class MonitorNotificationContent extends NotificationContent implements Serializable {
        long timeMillis;

        MonitorNotificationContent(long timeMillis, NotificationContent notificationContent) {
            super(notificationContent.message, notificationContent.extras);
            this.timeMillis = timeMillis;
        }

        @Override
        public String toString() {
            return "MonitorNotificationContent{" +
                    "timeMillis=" + timeMillis +
                    ", message='" + message + '\'' +
                    ", extras=" + extras +
                    '}';
        }
    }

}
