package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class PageLifecycleEventWithTime<T> implements Serializable {
    public PageInfo<T> pageInfo;
    public LifecycleEvent lifecycleEvent;
    public long startTimeMillis;
    public long endTimeMillis;

    public PageLifecycleEventWithTime(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long startTimeMillis, long endTimeMillis) {
        this.pageInfo = pageInfo;
        this.lifecycleEvent = lifecycleEvent;
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    public String toString() {
        return "PageLifecycleEventWithTime{" +
                "pageInfo=" + pageInfo +
                ", lifecycleEvent=" + lifecycleEvent +
                ", startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                '}';
    }
}
