package cn.hikyson.godeye.core.internal.modules.pageload;

import java.util.List;

public class PageloadUtil {
    public static long parsePageDrawTimeMillis(List<PageLifecycleEventWithTime> allEvents) {
        long startTime = 0;
        long drawTime = 0;
        for (PageLifecycleEventWithTime pageLifecycleEventWithTime : allEvents) {
            if (pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_CREATE
                    || pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_ATTACH) {
                startTime = pageLifecycleEventWithTime.eventTimeMillis;
            }
            if (pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                    || pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW) {
                drawTime = pageLifecycleEventWithTime.eventTimeMillis;
            }
        }
        if (startTime > 0 && drawTime > 0 && drawTime > startTime) {
            return drawTime - startTime;
        }
        return 0;
    }

    public static long parsePageloadTimeMillis(List<PageLifecycleEventWithTime> allEvents) {
        long startTime = 0;
        long loadTime = 0;
        for (PageLifecycleEventWithTime pageLifecycleEventWithTime : allEvents) {
            if (pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_CREATE
                    || pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_ATTACH) {
                startTime = pageLifecycleEventWithTime.eventTimeMillis;
            }
            if (pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                    || pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD) {
                loadTime = pageLifecycleEventWithTime.eventTimeMillis;
            }
        }
        if (startTime > 0 && loadTime > 0 && loadTime > startTime) {
            return loadTime - startTime;
        }
        return 0;
    }

}
