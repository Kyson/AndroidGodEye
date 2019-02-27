package cn.hikyson.godeye.core.internal.modules.crash;

import android.support.annotation.Keep;

import java.util.List;

/**
 * Created by kysonchao on 2017/12/18.
 */
@Keep
public interface CrashProvider {
    void storeCrash(CrashInfo crashInfo) throws Throwable;

    List<CrashInfo> restoreCrash() throws Throwable;
}
