package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.util.Log;

public final class GodEyeCanaryLog {

    private static volatile Logger logger = new DefaultLogger();

    public interface Logger {
        void d(String message, Object... args);

        void d(Throwable throwable, String message, Object... args);
    }

    private static class DefaultLogger implements Logger {
        DefaultLogger() {
        }

        @Override
        public void d(String message, Object... args) {
            String formatted = String.format(message, args);
            largeLog(Log.DEBUG, "AndroidGodEye", formatted);
        }

        private static final int LOG_MAX_LENGTH = 3000;

        private static void largeLog(int level, String tag, String content) {
            if (content.length() > LOG_MAX_LENGTH) {
                logOrigin(level, tag, content.substring(0, LOG_MAX_LENGTH));
                largeLog(level, tag, content.substring(LOG_MAX_LENGTH));
            } else {
                logOrigin(level, tag, content);
            }
        }

        private static void logOrigin(int level, String tag, String msg) {
            Log.println(level, tag, msg);
        }

        @Override
        public void d(Throwable throwable, String message, Object... args) {
            d(String.format(message, args) + '\n' + Log.getStackTraceString(throwable));
        }
    }

    public static void setLogger(Logger logger) {
        GodEyeCanaryLog.logger = logger;
    }

    public static void d(String message, Object... args) {
        // Local variable to prevent the ref from becoming null after the null check.
        Logger logger = GodEyeCanaryLog.logger;
        if (logger == null) {
            return;
        }
        logger.d(message, args);
    }

    public static void d(Throwable throwable, String message, Object... args) {
        // Local variable to prevent the ref from becoming null after the null check.
        Logger logger = GodEyeCanaryLog.logger;
        if (logger == null) {
            return;
        }
        logger.d(throwable, message, args);
    }

    private GodEyeCanaryLog() {
        throw new AssertionError();
    }
}
