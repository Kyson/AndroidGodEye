package cn.hikyson.godeye.internal.modules.traffic;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface TrafficContext {
    long intervalMillis();

    long sampleMillis();
}
