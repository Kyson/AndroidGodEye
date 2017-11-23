package cn.hikyson.godeye.internal.modules.sm.core;


import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/6/1.
 */
public class SmConfig {
    private static final int LONG_BLOCK_TIME = 2000;
    private static final int SHORT_BLOCK_TIME = 500;
    //800ms dump一次
    private static final int DUMP_INTERVAL = 800;

    //长卡顿阀值
    public int longBlockThreshold = LONG_BLOCK_TIME;
    //短卡顿阀值
    public int shortBlockThreshold = SHORT_BLOCK_TIME;
    //dump信息的间隔
    public int dumpInterval = DUMP_INTERVAL;

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
