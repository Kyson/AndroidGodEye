package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.app.Application;

public interface MethodCanaryContext {
    /**
     * 低于阈值认为是短耗时方法，短耗时方法会丢弃
     *
     * @return <=0 for all methods,单位ms
     */
    long lowCostMethodThresholdMillis();

    /**
     * 单个线程最大的方法数，如果超出会根据end-start小的方法进行丢弃
     *
     * @return <=0 for all methods
     */
    int maxMethodCountSingleThreadByCost();

    Application app();
}
