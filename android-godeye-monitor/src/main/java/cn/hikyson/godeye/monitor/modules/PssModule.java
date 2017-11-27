package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import cn.hikyson.godeye.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class PssModule implements Module {
    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        RamWithPssInfo ramWithPssInfo = Pipe.instance().popRamWithPssInfo();
        if (ramWithPssInfo == null) {
            return new ResultWrapper("no ramWithPssInfo").toBytes();
        }
        return new ResultWrapper<>(ramWithPssInfo).toBytes();
    }

    public static class RamWithPssInfo {
        public RamInfo ramInfo;
        public PssInfo pssInfo;

        public RamWithPssInfo(RamInfo ramInfo, PssInfo pssInfo) {
            this.ramInfo = ramInfo;
            this.pssInfo = pssInfo;
        }
    }
}
