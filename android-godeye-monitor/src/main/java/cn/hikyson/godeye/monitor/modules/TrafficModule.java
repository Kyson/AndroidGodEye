package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class TrafficModule extends BaseListModule<TrafficInfo> {

    @Override
    Collection<TrafficInfo> popData() {
        return Pipe.instance().popTrafficInfo();
    }
}
