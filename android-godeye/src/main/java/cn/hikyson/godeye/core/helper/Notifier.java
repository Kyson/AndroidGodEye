package cn.hikyson.godeye.core.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class Notifier {

    public static class Config{
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

    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("AndroidGodEye", "AndroidGodEye", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("AndroidGodEye");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notice(Config config) {
        if (Config.Type.NOTIFICATION.equals(config.getType())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                new Notification.Builder(sContext,"AndroidGodEye")
                        .setContentTitle(config.getTitle())
                        .setContentText(config.getMessage())
                        .setStyle(Notification.Style)
            }else{

            }
        }
    }

}
