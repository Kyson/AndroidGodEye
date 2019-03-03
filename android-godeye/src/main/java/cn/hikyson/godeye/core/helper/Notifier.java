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
        public @interface Type {
            public static final String NOTIFICATION = "NOTIFICATION";
            public static final String TOAST = "TOAST";
            public static final String DIALOG = "DIALOG";
        }

        private String mTitle;
        private String mMessage;
        private String mDetail;
        private @Type
        String mType;

        public Config(String title, String message, String detail, String type) {
            mTitle = title;
            mMessage = message;
            mDetail = detail;
            mType = type;
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

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }
    }

    public synchronized static void notice(Context context, Config config) {
        if (Config.Type.NOTIFICATION.equals(config.getType())) {
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
            builder.setSmallIcon(R.drawable.ic_remove_red_eye)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(config.getTitle())
                    .setContentText(config.getMessage())
                    .setStyle(new Notification.BigTextStyle().bigText(config.getDetail()))
                    .build();
            notificationManager.notify(getId(), builder.build());
        }
    }

    public static int getId() {
        return (int) SystemClock.uptimeMillis();
    }
}
