package cn.hikyson.godeye.core.internal.modules.fps;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface FpsContext {
    Context context();

    long intervalMillis();

    long sampleMillis();
}
