package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;

/**
 * Created by kysonchao on 2017/9/5.
 */
public class FpsModule implements Module {

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        float[] refreshRates = StotageQueue.getRefreshFrameRate();
        if (refreshRates == null) {
            return new ResultWrapper("refresh rate can not be found").toBytes();
        }
        return new ResultWrapper<>(refreshRates).toBytes();
    }
}
