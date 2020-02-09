package cn.hikyson.godeye.core.internal.modules.viewcanary;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ViewCanary extends ProduceableSubject<ViewIssueInfo> implements Install<ViewCanaryConfig> {

    private ViewCanaryConfig config;
    private boolean mInstalled = false;
    private ViewCanaryInternal mViewCanaryInternal;

    @Override
    public synchronized void install(ViewCanaryConfig config) {
        if (mInstalled) {
            L.d("ViewCanary already installed, ignore.");
            return;
        }
        this.config = config;
        mViewCanaryInternal = new ViewCanaryInternal();
        mViewCanaryInternal.start(this, config());
        mInstalled = true;
        L.d("ViewCanary installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("ViewCanary already uninstalled, ignore.");
            return;
        }
        if (mViewCanaryInternal != null) {
            mViewCanaryInternal.stop(GodEye.instance().getApplication());
            mViewCanaryInternal = null;
        }
        mInstalled = false;
        L.d("ViewCanary uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public ViewCanaryConfig config() {
        return config;
    }

    @Override
    protected Subject<ViewIssueInfo> createSubject() {
        return ReplaySubject.create();
    }
}
