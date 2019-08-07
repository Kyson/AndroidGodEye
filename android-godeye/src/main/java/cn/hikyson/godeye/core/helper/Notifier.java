package cn.hikyson.godeye.core.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;

import cn.hikyson.godeye.core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notifier {

    public static class Config {
        private String mTitle;
        private String mMessage;
        private String mDetail;

        public Config(String title, String message, String detail) {
            mTitle = title;
            mMessage = message;
            mDetail = detail;
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

        public String getDetail() {
            return mDetail;
        }

        public void setDetail(String detail) {
            mDetail = detail;
        }
    }

    public static Notification create(Context context, Config config) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel("AndroidGodEye");
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
                .setStyle(new Notification.BigTextStyle().bigText(config.getDetail()))
                .build();
    }

    public static int notice(Context context, Config config) {
        int id = createNoticeId();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, create(context, config));
        return id;
    }

    public static int createNoticeId() {
        return (int) SystemClock.uptimeMillis();
    }

    public static void cancelNotice(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
