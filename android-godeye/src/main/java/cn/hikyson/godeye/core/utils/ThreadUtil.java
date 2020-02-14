package cn.hikyson.godeye.core.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kysonchao on 2018/1/19.
 */
public class ThreadUtil {
    private static boolean sNeedDetect = true;
    private static Scheduler sMainScheduler;
    private static Scheduler sComputationScheduler;

    public static void setNeedDetectRunningThread(boolean sNeedDetect) {
        ThreadUtil.sNeedDetect = sNeedDetect;
    }

    public static void setMainScheduler(Scheduler sMainScheduler) {
        ThreadUtil.sMainScheduler = sMainScheduler;
    }

    public static void setComputationScheduler(Scheduler sComputationScheduler) {
        ThreadUtil.sComputationScheduler = sComputationScheduler;
    }

    public static Scheduler mainScheduler() {
        if (sMainScheduler != null) {
            return sMainScheduler;
        }
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler computationScheduler() {
        if (sComputationScheduler != null) {
            return sComputationScheduler;
        }
        return Schedulers.computation();
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void ensureMainThread(String tag) {
        if (sNeedDetect && !isMainThread()) {
            throw new IllegalStateException(tag + " operation must execute on main thread!");
        }
    }

    public static void ensureMainThread() {
        ensureMainThread("this");
    }

    public static void ensureWorkThread(String tag) {
        if (sNeedDetect && isMainThread()) {
            throw new IllegalStateException(tag + " operation must execute on work thread!");
        }
    }

    public static void ensureWorkThread() {
        ensureWorkThread("this");
    }

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static Executor sMain = new Executor() {
        @Override
        public void execute(Runnable command) {
            sHandler.post(command);
        }
    };

    private static final Object sLockForHandlerManager = new Object();

    private static Map<String, Handler> sHandlerMap = new HashMap<>();

    public static @NonNull
    Handler createIfNotExistHandler(String tag) {
        synchronized (sLockForHandlerManager) {
            Handler tmp = sHandlerMap.get(tag);
            if (tmp != null) {
                return tmp;
            }
            HandlerThread handlerThread = new HandlerThread(tag, Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            sHandlerMap.put(tag, handler);
            return handler;
        }
    }

    public static @Nullable
    Handler obtainHandler(String tag) {
        synchronized (sLockForHandlerManager) {
            return sHandlerMap.get(tag);
        }
    }

    public static void destoryHandler(String tag) {
        synchronized (sLockForHandlerManager) {
            Handler handler = sHandlerMap.remove(tag);
            if (handler != null) {
                handler.getLooper().quit();
            }
        }
    }
}
