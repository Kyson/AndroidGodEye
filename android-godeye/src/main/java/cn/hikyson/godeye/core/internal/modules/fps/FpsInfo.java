package cn.hikyson.godeye.core.internal.modules.fps;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class FpsInfo {
    public float currentFps;
    public float systemFps;

    public FpsInfo(float currentFps, float systemFps) {
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
