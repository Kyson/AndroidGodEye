package cn.hikyson.godeye.core.internal.notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.LinkedList;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.Notifier;
import cn.hikyson.godeye.core.utils.L;

public class LocalNotificationListenerService extends Service {
    private static final String ACTION_START = "START_FOREGROUND_ACTION";
    private static final String ACTION_STOP = "STOP_FOREGROUND_ACTION";
    private int mNotificationId;
    private LinkedList<String> mLatestMessages;
    private int mCount;

    public static void start(String message, boolean isStartup) {
        Intent intent = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.putExtra("message", message);
        intent.putExtra("isStartup", isStartup);
        intent.setAction(ACTION_START);
        ContextCompat.startForegroundService(GodEye.instance().getApplication(), intent);
    }

    public static void stop() {
        Intent intent = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.setAction(ACTION_STOP);
        GodEye.instance().getApplication().startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationId = Notifier.createNoticeId();
        mLatestMessages = new LinkedList<>();
        mCount = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            L.w("LocalNotificationListenerService onStartCommand intent == null");
            return START_REDELIVER_INTENT;
        }
        if (ACTION_START.equals(intent.getAction())) {
            startForeground(mNotificationId, updateNotification(intent));
        } else if (ACTION_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }

    private Notification updateNotification(Intent intent) {
        String message = intent.getStringExtra("message");
        boolean isStartup = intent.getBooleanExtra("isStartup", false);
        if (isStartup) {
            return Notifier.create(this, new Notifier.Config("AndroidGodEye", message));
        }
        mCount = mCount + 1;
        mLatestMessages.addFirst(message);
        if (mLatestMessages.size() > 5) {
            mLatestMessages.removeLast();
        }
        StringBuilder sb = new StringBuilder();
        if (mLatestMessages.size() == 1) {
            sb.append(mLatestMessages.get(0));
        } else {
            for (int i = 0; i < mLatestMessages.size(); i++) {
                sb.append(i + 1).append(". ").append(mLatestMessages.get(i));
                if (i < (mLatestMessages.size() - 1)) {
                    sb.append("\n");
                }
            }
        }
        String title = String.format("Found [%s] issue(s), latest %s:", mCount, mLatestMessages.size());
        return Notifier.create(this, new Notifier.Config(title, sb.toString()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
