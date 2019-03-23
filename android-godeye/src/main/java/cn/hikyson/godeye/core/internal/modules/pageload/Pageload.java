package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;


import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 页面加载模块
 * 安装卸载可以任意线程
 * 发射数据在主线程
 * Created by kysonchao on 2018/1/25.
 */
public class Pageload extends ProduceableSubject<PageloadInfo> implements Install<PageloadContext> {
    private PageloadEngine mPageloadEngine;

    /**
     * 外部在需要的时候（页面全部加载结束）调用
     */
    public synchronized void onPageLoaded(Activity activity) {
        if (mPageloadEngine != null) {
            mPageloadEngine.onPageLoaded(activity);
        }
    }

    @Override
    public synchronized void install(PageloadContext config) {
        if (mPageloadEngine != null) {
            L.d("pageload already installed, ignore.");
            return;
        }
        mPageloadEngine = new PageloadEngine(this, config);
        mPageloadEngine.work();
        L.d("pageload installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mPageloadEngine == null) {
            L.d("pageload already uninstalled, ignore.");
            return;
        }
        mPageloadEngine.shutdown();
        mPageloadEngine = null;
        L.d("pageload uninstalled.");
    }
}
