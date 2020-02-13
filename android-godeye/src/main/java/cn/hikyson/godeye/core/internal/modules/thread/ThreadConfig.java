package cn.hikyson.godeye.core.internal.modules.thread;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ThreadConfig implements Serializable {
    public long intervalMillis;
    // ThreadFilter class path
    public String threadFilter;
    // ThreadTagger class path
    public String threadTagger;

    public ThreadConfig(long intervalMillis, String threadFilter, String threadTagger) {
        this.intervalMillis = intervalMillis;
        this.threadFilter = threadFilter;
        this.threadTagger = threadTagger;
    }

    public ThreadConfig() {
        this.intervalMillis = 2000;
        this.threadFilter = ExcludeSystemThreadFilter.class.getName();
        this.threadTagger = ExcludeSystemThreadFilter.class.getName();
    }

    @Override
    public String toString() {
        return "ThreadConfig{" +
                "intervalMillis=" + intervalMillis +
                ", threadFilter='" + threadFilter + '\'' +
                ", threadTagger='" + threadTagger + '\'' +
                '}';
    }
}
