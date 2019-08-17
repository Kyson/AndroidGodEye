package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryContext;

public class MethodCanaryStatus {
    public long lowCostMethodThresholdMillis;
    public int maxMethodCountSingleThreadByCost;
    public boolean isMonitoring;
    public boolean isInstalled;

    public MethodCanaryStatus(MethodCanaryContext methodCanaryContext, boolean isInstalled, boolean isMonitoring) {
        if (methodCanaryContext == null) {
            this.lowCostMethodThresholdMillis = -1;
            this.maxMethodCountSingleThreadByCost = -1;
            this.isInstalled = isInstalled;
            this.isMonitoring = isMonitoring;
        } else {
            this.lowCostMethodThresholdMillis = methodCanaryContext.lowCostMethodThresholdMillis();
            this.maxMethodCountSingleThreadByCost = methodCanaryContext.maxMethodCountSingleThreadByCost();
            this.isInstalled = isInstalled;
            this.isMonitoring = isMonitoring;
        }
    }
}
