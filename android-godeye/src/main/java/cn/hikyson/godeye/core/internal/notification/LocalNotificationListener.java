package cn.hikyson.godeye.core.internal.notification;

public class LocalNotificationListener implements NotificationListener {

    @Override
    public void onNotificationReceive(NotificationContent notificationContent) {
        LocalNotificationListenerService.start(notificationContent.message);
    }

    void stop() {
        LocalNotificationListenerService.stop();
    }

    void start() {
        LocalNotificationListenerService.start("AndroidGodEye is running...");
    }
}
