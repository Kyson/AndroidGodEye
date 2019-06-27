package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.app.Application;

public interface MethodCanaryContext {
    /**
     * 超过阈值则存储到文件
     *
     * @return
     */
    int methodEventCountThreshold();

    /**
     * 低于阈值认为是短耗时方法，短耗时方法会丢掉
     *
     * @return <=0 for all method,单位ms
     */
    long lowCostMethodThreshold();

    Application app();
}
