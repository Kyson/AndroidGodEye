package cn.hikyson.godeye.core.internal.notification;


import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class NotificationObserverManager {
    private static NotificationConfig sGodEyeNotificationConfig;
    private static Map<String, NotificationObserver> sNotificationObservers = new HashMap<>();

    public synchronized static void installNotification(NotificationConfig godEyeNotificationConfig) {
        sGodEyeNotificationConfig = godEyeNotificationConfig;
        installNotificationListener("LOCAL", new LocalNotificationListener());
    }

    public synchronized static void uninstallNotification() {
        sGodEyeNotificationConfig = null;
        ensureNotificationObserversUninstalled();
    }

    public synchronized static void installNotificationListener(String name, NotificationListener notificationListener) {
        uninstallNotificationListener(name);
        NotificationObserver notificationObserver = new NotificationObserver(notificationListener);
        sNotificationObservers.put(name, notificationObserver);
        if (sGodEyeNotificationConfig != null) {
            ensureNotificationObserversInstalled(sGodEyeNotificationConfig);
        }
    }

    public synchronized static void uninstallNotificationListener(String name) {
        NotificationObserver exist = sNotificationObservers.remove(name);
        if (exist != null) {
            exist.uninstall();
        }
    }

    private static void ensureNotificationObserversInstalled(@NonNull NotificationConfig godEyeNotificationConfig) {
        for (Map.Entry<String, NotificationObserver> entry : sNotificationObservers.entrySet()) {
            if (!entry.getValue().isInstalled()) {
                entry.getValue().install(godEyeNotificationConfig);
            }
        }
    }

    private static void ensureNotificationObserversUninstalled() {
        for (Map.Entry<String, NotificationObserver> entry : sNotificationObservers.entrySet()) {
            if (entry.getValue().isInstalled()) {
                entry.getValue().uninstall();
            }
        }
        sNotificationObservers.clear();
    }
}
