package cn.hikyson.godeye.internal.modules.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

import java.util.concurrent.Callable;

import cn.hikyson.godeye.utils.ProcessUtils;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Pss implements Snapshotable<PssInfo> {
    private Context mContext;

    public Pss(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Observable<PssInfo> snapshot() {
        return Observable.fromCallable(new Callable<PssInfo>() {
            @Override
            public PssInfo call() throws Exception {
                final int pid = ProcessUtils.getCurrentPid();
                return getAppPssInfo(mContext, pid);
            }
        });
    }

    /**
     * 获取应用实际占用RAM
     *
     * @param context
     * @param pid
     * @return 应用pss信息KB
     */
    private static PssInfo getAppPssInfo(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{pid})[0];
        PssInfo pssInfo = new PssInfo();
        pssInfo.totalPssKb = memoryInfo.getTotalPss();
        pssInfo.dalvikPssKb = memoryInfo.dalvikPss;
        pssInfo.nativePssKb = memoryInfo.nativePss;
        pssInfo.otherPssKb = memoryInfo.otherPss;
        return pssInfo;
    }

}
