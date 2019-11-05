package cn.hikyson.godeye.monitor.modules.thread;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public enum ThreadRunningProcess implements Serializable {
    UNKNOWN, APP, SYSTEM
}
