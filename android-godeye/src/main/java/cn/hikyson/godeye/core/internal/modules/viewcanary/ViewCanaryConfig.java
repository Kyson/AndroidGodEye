package cn.hikyson.godeye.core.internal.modules.viewcanary;


import android.app.Application;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class ViewCanaryConfig implements Serializable {

    public int maxDepth;

    public ViewCanaryConfig() {
        this.maxDepth = 10;
    }

    public ViewCanaryConfig(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Application application() {
        return GodEye.instance().getApplication();
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