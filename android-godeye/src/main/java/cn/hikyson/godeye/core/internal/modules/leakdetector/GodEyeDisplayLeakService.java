package cn.hikyson.godeye.core.internal.modules.leakdetector;



import android.content.Intent;
import android.support.annotation.Nullable;

import com.squareup.leakcanary.AbstractAnalysisResultService;

public class GodEyeDisplayLeakService extends AbstractAnalysisResultService {

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //不实现
    }

    @Override
    protected void showForegroundNotification(int max, int progress, boolean indeterminate, String contentText) {
        //不实现
    }
}
