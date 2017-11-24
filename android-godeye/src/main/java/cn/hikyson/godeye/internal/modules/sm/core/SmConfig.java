package cn.hikyson.godeye.internal.modules.sm.core;


import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/6/1.
 */
public class SmConfig {
    //长卡顿阀值
    public int longBlockThreshold;
    //短卡顿阀值
    public int shortBlockThreshold;
    //dump信息的间隔
    public int dumpInterval;

    public SmConfig(int longBlockThreshold, int shortBlockThreshold, int dumpInterval) {
        if (shortBlockThreshold <= 0 || longBlockThreshold < shortBlockThreshold || dumpInterval <= 0) {
            L.onRuntimeException(new IllegalArgumentException("performance config params invalid!!!"));
            return;
        }
        this.longBlockThreshold = longBlockThreshold;
        this.shortBlockThreshold = shortBlockThreshold;
        this.dumpInterval = dumpInterval;
    }

    public SmConfig() {
    }
}
