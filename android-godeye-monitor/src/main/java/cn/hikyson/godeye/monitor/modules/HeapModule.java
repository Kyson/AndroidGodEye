package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class HeapModule extends BaseListModule<HeapInfo> {

    @Override
    Collection<HeapInfo> popData() {
        return Pipe.instance().popHeapInfo();
    }
}
