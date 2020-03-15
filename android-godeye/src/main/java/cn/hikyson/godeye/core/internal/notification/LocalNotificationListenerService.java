package cn.hikyson.godeye.core.internal.notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.Notifier;

public class LocalNotificationListenerService extends Service {
    private int mNotificationId;
    private int mCount;

    public static void start(String message) {
        Intent intent = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.putExtra("message", message);
        intent.setAction("START_FOREGROUND_ACTION");
        ContextCompat.startForegroundService(GodEye.instance().getApplication(), intent);
    }

    public static void stop() {
        Intent intent = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.setAction("STOP_FOREGROUND_ACTION");
        GodEye.instance().getApplication().startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationId = Notifier.createNoticeId();
        mCount = -1;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("START_FOREGROUND_ACTION".equals(intent.getAction())) {
            startForeground(mNotificationId, updateNotification(intent));
        } else if ("STOP_FOREGROUND_ACTION".equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private Notification updateNotification(Intent intent) {
        String title = intent.getStringExtra("message");
        mCount = mCount + 1;
        String message = String.format("[%s] issue(s) have been found by AndroidGodEye.", mCount);
        return Notifier.create(this, new Notifier.Config(title, message));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
