package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

public class LeakRefInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Nullable
    private final String pageId;
    private final boolean excludeRef;
    @Nullable
    private final Map<String, String> extraInfo;

    public LeakRefInfo(boolean excludeRef, @Nullable String pageId, @Nullable Map<String, String> extraInfo) {
        this.pageId = pageId;
        this.excludeRef = excludeRef;
        this.extraInfo = extraInfo;
    }

    @Nullable
    public String getPageId() {
        return pageId;
    }

    @Nullable
    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public boolean isExcludeRef() {
        return excludeRef;
    }

    @Override
    public String toString() {
        return "LeakRefInfo{" +
                "pageId='" + pageId + '\'' +
                ", excludeRef=" + excludeRef +
                ", extraInfo=" + extraInfo +
                '}';
    }
}
