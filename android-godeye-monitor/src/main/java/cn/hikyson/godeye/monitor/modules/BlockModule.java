package cn.hikyson.godeye.monitor.modules;

import java.util.Collection;

import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class BlockModule extends BaseListModule<BlockInfo> {

    @Override
    Collection<BlockInfo> popData() {
        return Pipe.instance().popBlockInfos();
    }
}
