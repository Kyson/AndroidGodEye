package cn.hikyson.godeye.internal.modules.fps;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class FpsContextImpl implements FpsContext {
    private Context mContext;

    public FpsContextImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Context context() {
        return mContext;
    }

    @Override
    public long intervalMillis() {
        return 2000;
    }
}
