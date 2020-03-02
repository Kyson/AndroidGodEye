package cn.hikyson.godeye.core.internal.modules.pageload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageLifecycleRecords {
    private Map<PageInfo, List<Integer>> mPageLifecycleEventIndexing = new HashMap<>();
    private List<PageLifecycleEventWithTime> mLifecycleEvents = new ArrayList<>();

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

    public synchronized <T> PageLifecycleEventWithTime<T> addEvent(PageInfo<T> pageInfo, LifecycleEvent lifecycleEvent, long time) {
        PageLifecycleEventWithTime<T> pageLifecycleEventLine = new PageLifecycleEventWithTime<>(pageInfo, lifecycleEvent, time);
        mLifecycleEvents.add(pageLifecycleEventLine);
        List<Integer> pageEventIndexingList = mPageLifecycleEventIndexing.get(pageInfo);
        if (pageEventIndexingList == null) {
            if (lifecycleEvent != ActivityLifecycleEvent.ON_CREATE
                    && lifecycleEvent != FragmentLifecycleEvent.ON_ATTACH) {
                throw new IllegalStateException(String.format("Page [%s] Lifecycle [%s] must start with ActivityLifecycleEvent.ON_CREATE or FragmentLifecycleEvent.ON_ATTACH", pageInfo, lifecycleEvent));
            }
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
}
