package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.app.Application;

public interface ViewCanaryContext {
    Application application();
    int maxDepth();
}
