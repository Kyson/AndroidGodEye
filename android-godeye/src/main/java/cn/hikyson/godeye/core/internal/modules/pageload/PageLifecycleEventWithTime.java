package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class PageLifecycleEventWithTime<T> implements Serializable {
    public PageInfo<T> pageInfo;
    public LifecycleEvent lifecycleEvent;
    public long startTimeMillis;
    public long endTimeMillis;

    public PageLifecycleEventWithTime(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long eventTimeMillis) {
        this.pageInfo = pageInfo;
        this.lifecycleEvent = lifecycleEvent;
        this.eventTimeMillis = eventTimeMillis;
    }

    @Override
    public String toString() {
        return "PageLifecycleEventWithTime{" +
                "pageInfo=" + pageInfo +
                ", lifecycleEvent=" + lifecycleEvent +
                ", eventTimeMillis=" + eventTimeMillis +
                '}';
    }
}
