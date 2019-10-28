package cn.hikyson.godeye.core.internal.modules.pageload;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public enum PageType implements Serializable {
    ACTIVITY,
    FRAGMENT,
    UNKNOWN
}
