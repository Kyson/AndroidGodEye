package cn.hikyson.godeye.internal.modules.leakdetector;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.CanaryLog;
import com.squareup.leakcanary.DefaultLeakDirectoryProvider;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.LeakDirectoryProvider;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.utils.FileUtil;
import cn.hikyson.godeye.utils.L;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class LeakDetector implements Install<Application>, Consumer<LeakQueue.LeakMemoryInfo> {

    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return InstanceHolder.sINSTANCE;
    }

    @Override
    public void install(Context context, Application config) {
        mLeakDirectoryProvider = new DefaultLeakDirectoryProvider(config);
        if (LeakCanary.isInAnalyzerProcess(config)) {
            return;
        }
        try {
            clearLeaks();
        } catch (FileUtil.FileException e) {
            L.e(e.getLocalizedMessage());
        }
        CanaryLog.setLogger(new CanaryLog.Logger() {
            @Override
            public void d(String s, Object... objects) {
                L.d(String.format(s, objects));
            }

            @Override
            public void d(Throwable throwable, String s, Object... objects) {
                L.e(String.format(s, objects) + "\n" + String.valueOf(throwable));
            }
        });
        LeakCanary.install(config);
    }

    @Override
    public void uninstall(Context context) throws Exception {
        throw new UnsupportedOperationException("leak detector can not uninstall");
    }

    private LeakDirectoryProvider mLeakDirectoryProvider;

    public static LeakDirectoryProvider getLeakDirectoryProvider() {
        return instance().mLeakDirectoryProvider;
    }

    public void clearLeaks() throws FileUtil.FileException {
        List<File> leakFiles = mLeakDirectoryProvider.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        });
        for (File f : leakFiles) {
            FileUtil.deleteIfExists(f);
        }
    }

    private ObservableEmitter<LeakQueue.LeakMemoryInfo> mLeakMemoryInfoObservableEmitter;

    @Override
    public Observable<LeakQueue.LeakMemoryInfo> consume() {
        return Observable.create(new ObservableOnSubscribe<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void subscribe(ObservableEmitter<LeakQueue.LeakMemoryInfo> e) throws Exception {
                mLeakMemoryInfoObservableEmitter = e;
            }
        });
    }

    public static void emitLeak(LeakQueue.LeakMemoryInfo leakMemoryInfo) {
        final ObservableEmitter<LeakQueue.LeakMemoryInfo> emitter = instance().mLeakMemoryInfoObservableEmitter;
        if (emitter != null && !emitter.isDisposed()) {
            emitter.onNext(leakMemoryInfo);
        }
    }
}
