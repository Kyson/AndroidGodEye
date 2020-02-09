package cn.hikyson.godeye.core.internal.modules.viewcanary;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ViewCanaryConfig implements Serializable {

    public int maxDepth;

    public ViewCanaryConfig() {
        this.maxDepth = 10;
    }

    public ViewCanaryConfig(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int maxDepth() {
        return maxDepth;
    }

    @Override
    public String toString() {
        return "ViewCanaryConfig{" +
                "maxDepth=" + maxDepth +
                '}';
    }
}