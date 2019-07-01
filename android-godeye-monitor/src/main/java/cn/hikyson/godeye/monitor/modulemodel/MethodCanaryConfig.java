package cn.hikyson.godeye.monitor.modulemodel;

import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryContext;

public class MethodCanaryConfig {
    public long lowCostMethodThresholdMillis;
    public int maxMethodCountSingleThreadByCost;

    public MethodCanaryConfig(MethodCanaryContext methodCanaryContext) {
        if (methodCanaryContext == null) {
            lowCostMethodThresholdMillis = -1;
            maxMethodCountSingleThreadByCost = -1;
        } else {
            lowCostMethodThresholdMillis = methodCanaryContext.lowCostMethodThresholdMillis();
            maxMethodCountSingleThreadByCost = methodCanaryContext.maxMethodCountSingleThreadByCost();
        }
    }
}
