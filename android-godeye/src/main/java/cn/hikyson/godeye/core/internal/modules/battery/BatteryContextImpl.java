package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */

public class BatteryContextImpl implements BatteryContext {

    private Context mContext;

    public BatteryContextImpl(Context context) {
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
