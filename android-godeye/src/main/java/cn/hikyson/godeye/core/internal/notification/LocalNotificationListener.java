package cn.hikyson.godeye.core.internal.notification;

public class LocalNotificationListener implements NotificationListener {

    @Override
    public void onInstalled() {
        LocalNotificationListenerService.start("monitoring...", true);
    }

    @Override
    public void onUninstalled() {
        LocalNotificationListenerService.stop();
    }

    @Override
    public void onNotificationReceive(long timeMillis, NotificationContent notificationContent) {
        LocalNotificationListenerService.start(notificationContent.message, false);
    }
}
