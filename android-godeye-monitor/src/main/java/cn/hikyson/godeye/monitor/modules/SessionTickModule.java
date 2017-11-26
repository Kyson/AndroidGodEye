package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;
import com.ctrip.ibu.performance.internal.PerformanceData;

import java.util.List;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class SessionTickModule implements Module {
    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        List<PerformanceData.TickData> tickDatas = StotageQueue.popTickDatas();
        if (tickDatas == null || tickDatas.isEmpty()) {
            return new ResultWrapper("tick data can not be found").toBytes();
        }
        return new ResultWrapper<>(tickDatas).toBytes();
    }
}
