package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class StartUpModule extends BaseModule<StartupInfo> {

    @Override
    StartupInfo popData() {
        return Pipe.instance().popStartupInfo();
    }
}
