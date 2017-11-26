package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;
import com.ctrip.ibu.performance.internal.PerformanceData;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class SessionEndModule implements Module {

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        PerformanceData.StopData stopData = StotageQueue.getStopData();
        if (stopData == null) {
            return new ResultWrapper("session stop data can not be found").toBytes();
        }
        return new ResultWrapper<>(stopData).toBytes();
    }
}
