package cn.hikyson.godeye.core.internal.modules.thread;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ThreadConfig implements Serializable {
    public long intervalMillis;
    //ThreadFilter
    public String threadFilter;

    public ThreadConfig(long intervalMillis, String threadFilter) {
        this.intervalMillis = intervalMillis;
        this.threadFilter = threadFilter;
    }

    public ThreadConfig() {
        this.intervalMillis = 2000;
        this.threadFilter = ExcludeSystemThreadFilter.class.getName();
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    public String threadFilter() {
        return threadFilter;
    }

    @Override
    public String toString() {
        return "ThreadConfig{" +
                "intervalMillis=" + intervalMillis +
                ", threadFilter=" + threadFilter +
                '}';
    }
}
