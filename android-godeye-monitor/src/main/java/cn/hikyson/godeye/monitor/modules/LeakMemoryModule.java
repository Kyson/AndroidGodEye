package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/29.
 */
public class LeakMemoryModule extends BaseListModule<LeakQueue.LeakMemoryInfo> {

    @Override
    Collection<LeakQueue.LeakMemoryInfo> popData() {
        return Pipe.instance().popLeakMemoryInfos();
    }
}
