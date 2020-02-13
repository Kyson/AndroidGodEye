package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

@Keep
public class DefaultThreadTagger implements ThreadTagger {
    @Override
    public String tag(Thread thread, ThreadInfo threadInfo) {
        return null;
    }
}
