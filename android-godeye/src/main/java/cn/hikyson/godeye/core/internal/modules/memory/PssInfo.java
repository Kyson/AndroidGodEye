package cn.hikyson.godeye.core.internal.modules.memory;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Created by kysonchao on 2017/11/22.
 */
@Keep
public class PssInfo implements Serializable {
    public int totalPssKb;
    public int dalvikPssKb;
    public int nativePssKb;
    public int otherPssKb;

    @Override
    public String toString() {
        return "PssInfo{" +
                "totalPss=" + totalPssKb +
                ", dalvikPss=" + dalvikPssKb +
                ", nativePss=" + nativePssKb +
                ", otherPss=" + otherPssKb +
                '}';
    }
}
