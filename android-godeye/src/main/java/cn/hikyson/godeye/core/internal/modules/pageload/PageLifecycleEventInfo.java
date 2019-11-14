package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class PageLifecycleEventInfo<T> implements Serializable {
    public PageInfo<T> pageInfo;
    public PageLifecycleEventWithTime<T> currentEvent;
    public List<PageLifecycleEventWithTime> allEvents;

    public PageLifecycleEventInfo(PageInfo<T> pageInfo, PageLifecycleEventWithTime<T> currentEvent, List<PageLifecycleEventWithTime> allEvents) {
        this.pageInfo = pageInfo;
        this.currentEvent = currentEvent;
        this.allEvents = allEvents;
    }

    @Override
    public String toString() {
        return "PageLifecycleEventInfo{" +
                "pageInfo=" + pageInfo +
                ", currentEvent=" + currentEvent +
                ", allEvents=" + allEvents +
                '}';
    }
}
