package cn.hikyson.godeye.core.internal.modules.sm;

import android.content.Context;

import cn.hikyson.godeye.core.internal.modules.sm.core.SmConfig;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class SmContextImpl implements SmContext {
    private static final int LONG_BLOCK_TIME = 2000;
    private static final int SHORT_BLOCK_TIME = 500;
    //800ms dump一次
    private static final int DUMP_INTERVAL = 800;
    private Context mContext;
    //长卡顿阀值
    public int mLongBlockThreshold;
    //短卡顿阀值
    public int mShortBlockThreshold;
    //dump信息的间隔
    public int mDumpInterval;

    public SmContextImpl(Context context, int longBlockThreshold, int shortBlockThreshold, int dumpInterval) {
        mContext = context.getApplicationContext();
        this.mLongBlockThreshold = longBlockThreshold <= 0 ? LONG_BLOCK_TIME : longBlockThreshold;
        this.mShortBlockThreshold = shortBlockThreshold <= 0 ? SHORT_BLOCK_TIME : shortBlockThreshold;
        this.mDumpInterval = dumpInterval <= 0 ? DUMP_INTERVAL : dumpInterval;
    }

    @Override
    public Context context() {
        return mContext;
    }

    @Override
    public SmConfig config() {
        return new SmConfig(mLongBlockThreshold, mShortBlockThreshold, mDumpInterval);
    }
}
