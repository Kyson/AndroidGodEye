package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public enum FragmentLifecycleEvent implements LifecycleEvent, Serializable {
    ON_ATTACH(true),
    ON_CREATE(true),
    ON_VIEW_CREATE(true),
    ON_START(true),
    ON_RESUME(true),
    ON_DRAW(false),
    ON_LOAD(false),
    ON_SHOW(false),
    ON_HIDE(false),
    ON_PAUSE(true),
    ON_STOP(true),
    ON_VIEW_DESTROY(true),
    ON_DESTROY(true),
    ON_DETACH(true);

    public boolean isSystemLifecycle;

    FragmentLifecycleEvent(boolean isSystemLifecycle) {
        this.isSystemLifecycle = isSystemLifecycle;
    }

    @Override
    public boolean isSystemLifecycle() {
        return isSystemLifecycle;
    }
}
