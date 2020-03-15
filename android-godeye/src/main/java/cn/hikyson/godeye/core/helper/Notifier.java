package cn.hikyson.godeye.core.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Objects;

import cn.hikyson.godeye.core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notifier {

    @Keep
    public static class Config implements Serializable {
        private String mTitle;
        private String mMessage;

        public Config(String title, String message) {
            mTitle = title;
            mMessage = message;
        }

        public Config() {
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }
    }

    public static Notification create(Context context, Config config) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    Objects.requireNonNull(notificationManager).getNotificationChannel("AndroidGodEye");
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel("AndroidGodEye", "AndroidGodEye", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("AndroidGodEye");
                notificationManager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(context, "AndroidGodEye");
        } else {
            builder = new Notification.Builder(context);
        }
        return builder.setSmallIcon(R.drawable.androidgodeye_ic_remove_red_eye)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.androidgodeye_ic_launcher))
                .setContentTitle(config.getTitle())
                .setContentText(config.getMessage())
                .setStyle(new Notification.BigTextStyle().bigText(config.getMessage()))
                .build();
    }

    public static int createNoticeId() {
        return (int) SystemClock.uptimeMillis();
    }

    public static int notice(Context context, Config config) {
        return notice(context, createNoticeId(), create(context, config));
    }

    public static int notice(Context context, int id, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
        return id;
    }

    public static void cancelNotice(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
