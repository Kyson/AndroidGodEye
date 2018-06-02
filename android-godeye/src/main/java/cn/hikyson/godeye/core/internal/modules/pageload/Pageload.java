package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Application;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class Pageload extends ProduceableSubject<PageloadInfo> implements Install<PageloadContext> {
    private PageloadEngine mPageloadEngine;

    public void install(Application application) {
        install(new PageloadContextImpl(application));
    }

    @Override
    public void install(PageloadContext config) {
        if (mPageloadEngine != null) {
            L.d("pageload already installed, ignore.");
            return;
        }
        mPageloadEngine = new PageloadEngine(this, config);
        mPageloadEngine.work();
        L.d("pageload installed.");
    }

    @Override
    public void uninstall() {
        if (mPageloadEngine == null) {
            L.d("pageload already uninstalled, ignore.");
            return;
        }
        mPageloadEngine.shutdown();
        mPageloadEngine = null;
        L.d("pageload uninstalled.");
    }
}
