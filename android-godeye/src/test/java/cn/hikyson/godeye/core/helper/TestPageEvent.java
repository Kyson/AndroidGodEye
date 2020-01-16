package cn.hikyson.godeye.core.helper;

import cn.hikyson.godeye.core.internal.modules.pageload.LifecycleEvent;

public class TestPageEvent {
    public int pageHashCode;
    public LifecycleEvent lifecycleEvent;
    public int allEventSize;

    public TestPageEvent(int pageHashCode, LifecycleEvent lifecycleEvent, int allEventSize) {
        this.pageHashCode = pageHashCode;
        this.lifecycleEvent = lifecycleEvent;
        this.allEventSize = allEventSize;
    }
}
