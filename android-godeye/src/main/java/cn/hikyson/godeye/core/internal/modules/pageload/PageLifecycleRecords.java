package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.utils.L;

public class PageLifecycleRecords {
    // page with lifecycle event and time index list
    private Map<PageInfo, List<Integer>> mPageLifecycleEventIndexing = new HashMap<>();
    // all pages lifecycle events and times
    private List<PageLifecycleEventWithTime> mLifecycleEvents = new ArrayList<>();
    // each page with a top lifecycle event and time
    private Map<PageInfo, LifecycleMethodEventWithTime> mTopLifecycleMethodEventForPages = new HashMap<>();

    /**
     * exist such LifecycleEvent for page
     *
     * @param pageInfo
     * @param lifecycleEvent
     * @return
     */
    synchronized boolean isExistEvent(PageInfo pageInfo, LifecycleEvent lifecycleEvent) {
        List<PageLifecycleEventWithTime> pageLifecycleEventWithTimes = getLifecycleEventsByPageInfo(pageInfo);
        if (pageLifecycleEventWithTimes != null && !pageLifecycleEventWithTimes.isEmpty()) {
            for (PageLifecycleEventWithTime pageLifecycleEventWithTime : pageLifecycleEventWithTimes) {
                if (pageLifecycleEventWithTime.lifecycleEvent == lifecycleEvent) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSystemLifecycleEvent(LifecycleEvent lifecycleEvent) {
        return !ActivityLifecycleEvent.ON_DRAW.equals(lifecycleEvent)
                && !ActivityLifecycleEvent.ON_LOAD.equals(lifecycleEvent)
                && !FragmentLifecycleEvent.ON_DRAW.equals(lifecycleEvent)
                && !FragmentLifecycleEvent.ON_LOAD.equals(lifecycleEvent)
                && !FragmentLifecycleEvent.ON_HIDE.equals(lifecycleEvent)
                && !FragmentLifecycleEvent.ON_SHOW.equals(lifecycleEvent);
    }

    synchronized @Nullable
    <T> PageLifecycleEventWithTime<T> addLifecycleEvent(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long time) {
        if (!isSystemLifecycleEvent(lifecycleEvent)) {
            return addEvent(pageInfo, new PageLifecycleEventWithTime<>(pageInfo, lifecycleEvent, time, time));
        }
        LifecycleMethodEventWithTime currentTop = mTopLifecycleMethodEventForPages.get(pageInfo);
        PageLifecycleEventWithTime<T> tmp = null;
        if (currentTop == null) {
            tmp = new PageLifecycleEventWithTime<>(pageInfo, lifecycleEvent, time, time);
        } else if (!PageLifecycleMethodEventTypes.isMatch(lifecycleEvent, currentTop.lifecycleEvent)) {
            tmp = new PageLifecycleEventWithTime<>(pageInfo, lifecycleEvent, time, time);
        } else if (currentTop.methodStartTime > 0 && currentTop.methodEndTime > 0) {
            tmp = new PageLifecycleEventWithTime<>(pageInfo, lifecycleEvent, currentTop.methodStartTime, time);
        }
        if (tmp != null) {
            mTopLifecycleMethodEventForPages.remove(pageInfo);
            return addEvent(pageInfo, tmp);
        } else {
            currentTop.lifecycleTime = time;
            return null;
        }
    }

    synchronized @Nullable
    <T> PageLifecycleEventWithTime<T> addMethodEndEvent(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long time) {
        LifecycleMethodEventWithTime currentTop = mTopLifecycleMethodEventForPages.get(pageInfo);
        if (currentTop == null) {
            L.w(String.format("PageLifecycleRecords addMethodEndEvent currentTop == null: %s, %s", pageInfo, lifecycleEvent));
            return null;
        }
        if (!PageLifecycleMethodEventTypes.isMatch(currentTop.lifecycleEvent, lifecycleEvent)) {
            L.w(String.format("PageLifecycleRecords addMethodEndEvent !PageLifecycleMethodEventTypes.isMatch(currentTop.lifecycleEvent, lifecycleEvent): %s, %s", pageInfo, lifecycleEvent));
            mTopLifecycleMethodEventForPages.remove(pageInfo);
            return null;
        }
        if (currentTop.methodStartTime > 0 && currentTop.lifecycleTime > 0) {
            mTopLifecycleMethodEventForPages.remove(pageInfo);
            return addEvent(pageInfo, new PageLifecycleEventWithTime<>(pageInfo, currentTop.lifecycleEvent, currentTop.methodStartTime, time));
        } else {
            currentTop.methodEndTime = time;
            return null;
        }
    }

    synchronized <T> void addMethodStartEvent(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long time) {
        mTopLifecycleMethodEventForPages.put(pageInfo, new LifecycleMethodEventWithTime(lifecycleEvent, time, 0, 0));
    }

    private <T> PageLifecycleEventWithTime<T> addEvent(PageInfo<T> pageInfo, PageLifecycleEventWithTime<T> pageLifecycleEventLine) {
        mLifecycleEvents.add(pageLifecycleEventLine);
        List<Integer> pageEventIndexingList = mPageLifecycleEventIndexing.get(pageInfo);
        if (pageEventIndexingList == null) {
            pageEventIndexingList = new ArrayList<>();
            mPageLifecycleEventIndexing.put(pageInfo, pageEventIndexingList);
        }
        pageEventIndexingList.add(mLifecycleEvents.size() - 1);
        return pageLifecycleEventLine;
    }

    synchronized List<PageLifecycleEventWithTime> getLifecycleEventsByPageInfo(PageInfo pageInfo) {
        List<Integer> pageEventIndexingList = mPageLifecycleEventIndexing.get(pageInfo);
        if (pageEventIndexingList == null) {
            return new ArrayList<>();
        }
        List<PageLifecycleEventWithTime> pageLifecycleEventWithTimes = new ArrayList<>(pageEventIndexingList.size());
        for (int index : pageEventIndexingList) {
            pageLifecycleEventWithTimes.add(mLifecycleEvents.get(index));
        }
        return pageLifecycleEventWithTimes;
    }

    @Keep
    static class LifecycleMethodEventWithTime implements Serializable {
        LifecycleEvent lifecycleEvent;
        long methodStartTime;
        long methodEndTime;
        long lifecycleTime;

        LifecycleMethodEventWithTime(LifecycleEvent lifecycleEvent, long methodStartTime, long methodEndTime, long lifecycleTime) {
            this.lifecycleEvent = lifecycleEvent;
            this.methodStartTime = methodStartTime;
            this.methodEndTime = methodEndTime;
            this.lifecycleTime = lifecycleTime;
        }
    }
}
