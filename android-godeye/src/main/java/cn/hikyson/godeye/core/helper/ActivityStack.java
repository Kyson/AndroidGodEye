package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.utils.ThreadUtil;

public class ActivityStack {

    private List<Activity> mActivities = new ArrayList<>();

    @UiThread
    public ActivityStack(final Application application) {
        ThreadUtil.ensureMainThread();
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            @UiThread
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivities.add(activity);
            }

            @Override
            @UiThread
            public void onActivityDestroyed(Activity activity) {
                mActivities.remove(activity);
            }
        });
    }
}
