package cn.hikyson.godeye.monitor.modules.leak;

import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakInfo;
import io.reactivex.functions.Function;

public class LeakConverter {

    public static Function<LeakInfo, LeakSimpleInfo> leakConverter() {
        return new Function<LeakInfo, LeakSimpleInfo>() {
            @Override
            public LeakSimpleInfo apply(LeakInfo leak) throws Exception {
                return new LeakSimpleInfo(leak);
            }
        };
    }

}
