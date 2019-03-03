package cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import cn.hikyson.godeye.core.R;
import cn.hikyson.godeye.core.helper.Notifier;

public class ForegroundService extends IntentService {
    private String mName;

    public ForegroundService(String name) {
        super(name);
        mName = name;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        final String channelName = "AndroidGodEye";
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(channelName);
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_MIN);
                channel.setDescription(channelName);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(ForegroundService.this, channelName);
        } else {
            builder = new Notification.Builder(ForegroundService.this);
        }
        builder.setSmallIcon(R.drawable.ic_remove_red_eye)
                .setLargeIcon(BitmapFactory.decodeResource(ForegroundService.this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(mName)
                .setContentText(channelName)
                .build();
        startForeground(Notifier.getId(), builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
