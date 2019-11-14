package cn.hikyson.godeye.core.internal.modules.memory;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by kysonchao on 2017/11/22.
 */
@Keep
public class HeapInfo implements Serializable {
    public long freeMemKb;
    public long maxMemKb;
    public long allocatedKb;

    @Override
    public String toString() {
        return "HeapInfo{" +
                "freeMemKb=" + freeMemKb +
                ", maxMemKb=" + maxMemKb +
                ", allocatedKb=" + allocatedKb +
                '}';
    }
}
