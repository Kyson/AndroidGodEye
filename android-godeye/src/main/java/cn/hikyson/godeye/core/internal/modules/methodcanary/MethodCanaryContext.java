package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.app.Application;

public interface MethodCanaryContext {
    int methodEventCountThreshold();
    Application app();
}
