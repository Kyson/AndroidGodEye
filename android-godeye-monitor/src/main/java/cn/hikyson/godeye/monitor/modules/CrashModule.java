package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class CrashModule extends BaseModule<CrashInfo> {

    @Override
    CrashInfo popData() {
        return Pipe.instance().popCrashInfo();
    }
}
