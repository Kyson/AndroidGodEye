package cn.hikyson.godeye.monitor.modulemodel;

import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.pageload.PageInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventWithTime;

public class PageLifecycleProcessedEvent<T> {
    public PageInfo<T> pageInfo;
    public PageLifecycleEventWithTime<T> pageLifecycleEventWithTime;
    public Map<String, Object> processedInfo;
}
