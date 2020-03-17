package cn.hikyson.godeye.core.internal.notification;

public interface NotificationListener {
    void onInstalled();
    void onUninstalled();
    void onNotificationReceive(long timeMillis, NotificationContent notificationContent);
}
