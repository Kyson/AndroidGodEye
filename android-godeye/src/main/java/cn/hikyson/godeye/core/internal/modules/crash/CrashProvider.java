package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;

/**
 * Created by kysonchao on 2017/12/18.
 */
public interface CrashProvider {
    public void storeCrash(CrashInfo crashInfo) throws Throwable;

    public List<CrashInfo> restoreCrash() throws Throwable;
}
