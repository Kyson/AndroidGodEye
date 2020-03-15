package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public enum ActivityLifecycleEvent implements LifecycleEvent, Serializable {
    ON_CREATE(true),
    ON_START(true),
    ON_RESUME(true),
    ON_DRAW(false),
    ON_LOAD(false),
    ON_PAUSE(true),
    ON_STOP(true),
    ON_DESTROY(true);

    public boolean isSystemLifecycle;

    ActivityLifecycleEvent(boolean isSystemLifecycle) {
        this.isSystemLifecycle = isSystemLifecycle;
    }

    @Override
    public boolean isSystemLifecycle() {
        return isSystemLifecycle;
    }
}
