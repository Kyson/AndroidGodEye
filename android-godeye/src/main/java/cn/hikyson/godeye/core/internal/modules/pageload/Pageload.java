package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;


import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class Pageload extends ProduceableSubject<PageloadInfo> implements Install<PageloadContext> {
    private PageloadEngine mPageloadEngine;

    /**
     * 外部在需要的时候（页面全部加载结束）调用
     */
    public void onPageLoaded(Activity activity) {
        if (mPageloadEngine != null) {
            mPageloadEngine.onPageLoaded(activity);
        }
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
