package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/5.
 */
public class FpsModule extends BaseModule<FpsInfo> {

    @Override
    FpsInfo popData() {
        return  Pipe.instance().popFpsInfo();
    }
}
