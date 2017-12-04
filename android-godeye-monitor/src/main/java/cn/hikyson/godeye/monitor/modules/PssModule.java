package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class PssModule extends BaseModule<PssInfo> {

    @Override
    PssInfo popData() {
        return Pipe.instance().popPssInfo();
    }
}
