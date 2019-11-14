package cn.hikyson.godeye.monitor.modules;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryContext;

@Keep
public class MethodCanaryStatus implements Serializable {
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
