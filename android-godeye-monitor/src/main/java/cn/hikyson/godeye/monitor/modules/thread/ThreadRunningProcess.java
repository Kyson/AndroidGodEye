package cn.hikyson.godeye.monitor.modules.thread;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public enum ThreadRunningProcess implements Serializable {
    UNKNOWN, APP, SYSTEM
}
