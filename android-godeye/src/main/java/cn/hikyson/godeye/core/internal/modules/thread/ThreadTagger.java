package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

@Keep
public interface ThreadTagger {

    public String tag(Thread thread, ThreadInfo threadInfo);

}
