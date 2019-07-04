package cn.hikyson.godeye.core.internal.modules.sm.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public final class HandlerThreadFactory {

    /**
     * dump线程
     */
    private static HandlerThreadWrapper sDoDumpThread = new HandlerThreadWrapper("godeye-sm-do-dump");
    /**
     * 获取dump数据线程
     */
    private static HandlerThreadWrapper sObtainDumpThread = new HandlerThreadWrapper("godeye-sm-obtain-dump");

    private HandlerThreadFactory() {
        throw new InstantiationError("can not init this class");
    }

    public static Handler getDoDumpThreadHandler() {
        return sDoDumpThread.getHandler();
    }

    public static Handler getObtainDumpThreadHandler() {
        return sObtainDumpThread.getHandler();
    }

    private static class HandlerThreadWrapper {
        private Handler handler = null;

        public HandlerThreadWrapper(String threadName) {
            HandlerThread handlerThread = new HandlerThread(threadName, Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        public Handler getHandler() {
            return handler;
        }
    }
}
