package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.CanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.DefaultLeakDirectoryProvider;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.LeakCanary;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.LeakDirectoryProvider;
import cn.hikyson.godeye.core.utils.FileUtil;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class LeakDetector extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakContext> {

    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return InstanceHolder.sINSTANCE;
    }

    @SuppressLint("CheckResult")
    @Override
    public synchronized void install(final LeakContext config) {
        final Application application = config.application();
        if (LeakCanary.isInAnalyzerProcess(application)) {
            throw new IllegalStateException("can not call install leak");
        }
        LeakCanary.uninstall();
        mLeakDirectoryProvider = new DefaultLeakDirectoryProvider(application);
        try {
            clearLeaks();
        } catch (FileUtil.FileException e) {
            L.e(e.getLocalizedMessage());
        }
        LeakCanary.install(application);
        L.d("LeakCanary installed");
    }

    @Override
    public synchronized void uninstall() {
        mLeakDirectoryProvider = null;
        LeakCanary.uninstall();
        L.d("LeakCanary uninstalled");
    }

    private LeakDirectoryProvider mLeakDirectoryProvider;

    static LeakDirectoryProvider getLeakDirectoryProvider() {
        return instance().mLeakDirectoryProvider;
    }

    private void clearLeaks() throws FileUtil.FileException {
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
}
