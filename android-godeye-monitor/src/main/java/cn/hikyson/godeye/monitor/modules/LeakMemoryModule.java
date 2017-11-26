package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;

import java.util.List;

/**
 * Created by kysonchao on 2017/9/29.
 */
public class LeakMemoryModule implements Module {

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        List<StotageQueue.LeakMemoryItem> leakMemoryItems = StotageQueue.getLeakMemoryItems();
        return new ResultWrapper<>(leakMemoryItems).toBytes();
    }
}
