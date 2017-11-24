package cn.hikyson.godeye.internal.modules.memory;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class RamContextImpl implements RamContext {
    private Context mContext;

    public RamContextImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Context context() {
        return mContext;
    }

    @Override
    public long intervalMillis() {
        return 1000;
    }
}
