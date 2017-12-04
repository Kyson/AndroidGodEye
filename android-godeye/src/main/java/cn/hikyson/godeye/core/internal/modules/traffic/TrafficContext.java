package cn.hikyson.godeye.core.internal.modules.traffic;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface TrafficContext {
    long intervalMillis();

    long sampleMillis();
}
