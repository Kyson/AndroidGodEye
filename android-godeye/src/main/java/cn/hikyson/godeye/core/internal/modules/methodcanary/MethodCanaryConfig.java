package cn.hikyson.godeye.core.internal.modules.methodcanary;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class MethodCanaryConfig implements Serializable {
    public int maxMethodCountSingleThreadByCost;
    public long lowCostMethodThresholdMillis;

    public MethodCanaryConfig() {
        this.maxMethodCountSingleThreadByCost = 300;
        this.lowCostMethodThresholdMillis = 10L;
    }

    public MethodCanaryConfig(int maxMethodCountSingleThreadByCost, long lowCostMethodThresholdMillis) {
        this.maxMethodCountSingleThreadByCost = maxMethodCountSingleThreadByCost;
        this.lowCostMethodThresholdMillis = lowCostMethodThresholdMillis;
    }

    public long lowCostMethodThresholdMillis() {
        return lowCostMethodThresholdMillis;
    }

    public int maxMethodCountSingleThreadByCost() {
        return maxMethodCountSingleThreadByCost;
    }

    @Override
    public String toString() {
        return "MethodCanaryConfig{" +
                "maxMethodCountSingleThreadByCost=" + maxMethodCountSingleThreadByCost +
                ", lowCostMethodThresholdMillis=" + lowCostMethodThresholdMillis +
                '}';
    }
}
