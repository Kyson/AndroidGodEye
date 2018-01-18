package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/29.
 */
public class ThreadModule extends BaseListModule<ThreadInfo> {

    @Override
    Collection<ThreadInfo> popData() {
        return Pipe.instance().popThreadInfo();
    }
}
