package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class StartUpModule implements Module {

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        StotageQueue.StartupItem startupItem = StotageQueue.getStartupItem();
        if (startupItem == null) {
            return new ResultWrapper<StotageQueue.StartupItem>(ResultWrapper.DEFAULT_FAIL, "no startup data found", null).toBytes();
        }
        return new ResultWrapper<>(startupItem).toBytes();
    }
}
