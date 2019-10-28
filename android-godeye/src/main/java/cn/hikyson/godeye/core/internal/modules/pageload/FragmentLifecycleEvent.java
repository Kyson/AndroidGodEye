package cn.hikyson.godeye.core.internal.modules.pageload;

import android.support.annotation.Keep;

import java.io.Serializable;
@Keep
public enum FragmentLifecycleEvent implements LifecycleEvent, Serializable {
    ON_ATTACH,
    ON_CREATE,
    ON_VIEW_CREATE,
    ON_START,
    ON_RESUME,
    ON_DRAW,
    ON_LOAD,
    ON_SHOW,
    ON_HIDE,
    ON_PAUSE,
    ON_STOP,
    ON_VIEW_DESTROY,
    ON_DESTROY,
    ON_DETACH
}
