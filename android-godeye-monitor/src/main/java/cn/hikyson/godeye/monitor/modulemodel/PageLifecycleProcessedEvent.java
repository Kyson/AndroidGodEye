package cn.hikyson.godeye.monitor.modulemodel;

import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.pageload.LifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageType;

public class PageLifecycleProcessedEvent<T> {
    public PageType pageType;
    public String pageClassName;
    public int pageHashCode;
    public LifecycleEvent lifecycleEvent;
    public long eventTimeMillis;
    public Map<String, Object> processedInfo;
}
