package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class NetworkModule extends BaseListModule<RequestBaseInfo> {

    @Override
    Collection<RequestBaseInfo> popData() {
        return Pipe.instance().popRequestBaseInfos();
    }
}
