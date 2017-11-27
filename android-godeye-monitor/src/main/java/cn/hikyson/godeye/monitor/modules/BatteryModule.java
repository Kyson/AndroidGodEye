package cn.hikyson.godeye.monitor.modules;

import cn.hikyson.godeye.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class BatteryModule extends BaseModule<BatteryInfo> {

    @Override
    BatteryInfo popData() {
        return Pipe.instance().popBatteryInfo();
    }
}
