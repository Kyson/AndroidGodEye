package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.app.Application;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

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

    public Application app() {
        return GodEye.instance().getApplication();
    }

    @Override
    public String toString() {
        return "MethodCanaryConfig{" +
                "maxMethodCountSingleThreadByCost=" + maxMethodCountSingleThreadByCost +
                ", lowCostMethodThresholdMillis=" + lowCostMethodThresholdMillis +
                '}';
    }
}
