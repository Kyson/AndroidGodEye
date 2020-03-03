package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public enum ActivityLifecycleEvent implements LifecycleEvent, Serializable {
    ON_CREATE,
    ON_START,
    ON_RESUME,
    ON_DRAW,
    ON_LOAD,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY
}
