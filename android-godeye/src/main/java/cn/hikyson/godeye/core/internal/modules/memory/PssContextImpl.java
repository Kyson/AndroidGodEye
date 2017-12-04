package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class PssContextImpl implements PssContext {
    private Context mContext;

    public PssContextImpl(Context context) {
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
