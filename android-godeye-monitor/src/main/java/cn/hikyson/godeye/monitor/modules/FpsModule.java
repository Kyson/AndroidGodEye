package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import cn.hikyson.godeye.internal.modules.fps.FpsInfo;
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
