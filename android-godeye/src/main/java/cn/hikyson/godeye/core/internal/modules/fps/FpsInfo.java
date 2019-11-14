package cn.hikyson.godeye.core.internal.modules.fps;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by kysonchao on 2017/11/23.
 */
@Keep
public class FpsInfo implements Serializable {
    public int currentFps;
    public int systemFps;

    public FpsInfo(int currentFps, int systemFps) {
        this.currentFps = currentFps;
        this.systemFps = systemFps;
    }

    @Override
    public String toString() {
        return "FpsInfo{" +
                "currentFps=" + currentFps +
                ", systemFps=" + systemFps +
                '}';
    }
}
