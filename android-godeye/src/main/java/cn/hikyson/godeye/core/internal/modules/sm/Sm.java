package cn.hikyson.godeye.core.internal.modules.sm;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.sm.core.BlockListener;
import cn.hikyson.godeye.core.internal.modules.sm.core.LongBlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.core.ShortBlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.core.SmCore;
import cn.hikyson.godeye.core.utils.JsonUtil;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * 卡顿模块
 * 安装卸载可以在任意线程
 * 发射数据在子线程
 */
public final class Sm extends ProduceableSubject<BlockInfo> implements Install<SmConfig> {
    private SmCore mBlockCore;
    private SmConfig mSmRealConfig;
    private SmConfig mSmConfig;
    private boolean mInstalled = false;

    @Override
    public synchronized void install(SmConfig config) {
        if (mInstalled) {
            L.d("Sm already installed, ignore.");
            return;
        }
        this.mInstalled = true;
        this.mSmConfig = config;
        this.mSmRealConfig = wrapRealConfig(config);
        this.mBlockCore = new SmCore(GodEye.instance().getApplication(), this.mSmRealConfig.debugNotification(),
                this.mSmRealConfig.longBlockThreshold(), this.mSmRealConfig.shortBlockThreshold(), this.mSmRealConfig.dumpInterval());
        this.mBlockCore.setBlockListener(new BlockListener() {
            @Override
            public void onStart(Context context) {
            }

            @Override
            public void onStop(Context context) {
            }

            @WorkerThread
            @Override
            public void onShortBlock(Context context, ShortBlockInfo shortBlockInfo) {
                ThreadUtil.ensureWorkThread("Sm onShortBlock");
                produce(new BlockInfo(shortBlockInfo));
            }

            @WorkerThread
            @Override
            public void onLongBlock(Context context, LongBlockInfo blockInfo) {
                ThreadUtil.ensureWorkThread("Sm onLongBlock");
                produce(new BlockInfo(blockInfo));
            }
        });
        mBlockCore.install();
        L.d("Sm installed");
    }

    private SmConfig wrapRealConfig(SmConfig installSmConfig) {
        SmConfig cacheSmConfig = getValidSmConfigCache();
        SmConfig realSmConfig = new SmConfig();
        realSmConfig.debugNotification = installSmConfig.debugNotification();
        realSmConfig.dumpIntervalMillis = installSmConfig.dumpInterval();
        realSmConfig.longBlockThresholdMillis = (cacheSmConfig == null || cacheSmConfig.longBlockThreshold() <= 0) ? installSmConfig.longBlockThreshold() : cacheSmConfig.longBlockThreshold();
        realSmConfig.shortBlockThresholdMillis = (cacheSmConfig == null || cacheSmConfig.shortBlockThreshold() <= 0) ? installSmConfig.shortBlockThreshold() : cacheSmConfig.shortBlockThreshold();
        return realSmConfig;
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("Sm already uninstalled, ignore.");
            return;
        }
        mInstalled = false;
        this.mSmRealConfig = null;
        this.mSmConfig = null;
        mBlockCore.uninstall();
        L.d("Sm uninstalled");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public SmConfig config() {
        return mSmRealConfig;
    }

    public SmConfig installConfig() {
        return mSmConfig;
    }

    @Override
    protected Subject<BlockInfo> createSubject() {
        return ReplaySubject.create();
    }

    public SmCore getBlockCore() {
        return mBlockCore;
    }

    public void setSmConfigCache(SmConfig smConfigCache) {
        if (smConfigCache == null || !smConfigCache.isValid()) {
            return;
        }
        SharedPreferences sharedPreferences = GodEye.instance().getApplication().getSharedPreferences("AndroidGodEye", 0);
        sharedPreferences.edit().putString("SmConfig", JsonUtil.toJson(smConfigCache)).apply();
    }

    public void clearSmConfigCache() {
        SharedPreferences sharedPreferences = GodEye.instance().getApplication().getSharedPreferences("AndroidGodEye", 0);
        sharedPreferences.edit().remove("SmConfig").apply();
    }

    public @Nullable
    SmConfig getValidSmConfigCache() {
        SharedPreferences sharedPreferences = GodEye.instance().getApplication().getSharedPreferences("AndroidGodEye", 0);
        String cachedSmContextStr = sharedPreferences.getString("SmConfig", null);
        if (TextUtils.isEmpty(cachedSmContextStr)) {
            return null;
        }
        SmConfig smConfigCache = JsonUtil.sGson.fromJson(cachedSmContextStr, SmConfig.class);
        if (smConfigCache == null) {
            return null;
        }
        if (!smConfigCache.isValid()) {
            sharedPreferences.edit().remove("SmConfig").apply();
            return null;
        }
        return smConfigCache;
    }
}
