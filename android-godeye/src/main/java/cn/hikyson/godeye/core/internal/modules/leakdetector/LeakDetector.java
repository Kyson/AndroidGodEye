package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.Manifest;
import android.app.Application;
import android.support.v4.content.PermissionChecker;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableConsumer;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.CanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.DefaultLeakDirectoryProvider;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.LeakCanary;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.LeakDirectoryProvider;
import cn.hikyson.godeye.core.utils.FileUtil;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class LeakDetector extends ProduceableConsumer<LeakQueue.LeakMemoryInfo> implements Install<LeakContext> {

    private boolean mInstalled;

    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return InstanceHolder.sINSTANCE;
    }

    public synchronized void install(Application application) {
        install(new LeakContextImpl(application));
    }

    @Override
    public synchronized void install(LeakContext config) {
        Application application = config.application();
        if (LeakCanary.isInAnalyzerProcess(application)) {
            throw new IllegalStateException("can not call install leak");
        }
        if (PermissionChecker.checkSelfPermission(config.application(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            throw new IllegalStateException("install leak need permission:" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (mInstalled) {
            return;
        }
        mInstalled = true;
        mLeakDirectoryProvider = new DefaultLeakDirectoryProvider(application);
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
        LeakCanary.install(application);
        L.d("LeakCanary installed");
    }

    @Override
    public synchronized void uninstall() {
        throw new UnsupportedOperationException("leak detector can not uninstall!");
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
