package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class RamModule extends BaseModule<RamInfo> {

    @Override
    RamInfo popData() {
        return Pipe.instance().popRamInfo();
    }
}
