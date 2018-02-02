package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.Manifest;
import android.app.Application;

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
import io.reactivex.functions.Consumer;

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

    public synchronized void install(Application application) {
        install(new LeakContextImpl(application));
    }

    @Override
    public synchronized void install(final LeakContext config) {
        final Application application = config.application();
        if (LeakCanary.isInAnalyzerProcess(application)) {
            throw new IllegalStateException("can not call install leak");
        }
        config.permissionNeed(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    throw new IllegalStateException("install leak need permission:" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                uninstall();
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
        });
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
