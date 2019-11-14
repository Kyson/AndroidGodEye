package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public enum PageType implements Serializable {
    ACTIVITY,
    FRAGMENT,
    UNKNOWN
}
