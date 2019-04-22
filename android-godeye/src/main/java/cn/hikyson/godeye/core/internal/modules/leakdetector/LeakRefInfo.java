package cn.hikyson.godeye.core.internal.modules.leakdetector;

public class LeakRefInfo {
    private final String extraInfo;
    private final boolean excludeRef;

    public LeakRefInfo(boolean excludeRef, String extraInfo) {
        this.extraInfo = extraInfo;
        this.excludeRef = excludeRef;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public boolean isExcludeRef() {
        return excludeRef;
    }
}
