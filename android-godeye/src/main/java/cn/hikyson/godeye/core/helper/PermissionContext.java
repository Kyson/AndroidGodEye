package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by kysonchao on 2018/1/29.
 */
public class PermissionContext {
    public interface OnAttachActivityCallback {
        void onAttachActivity(Activity activity);
    }

    public static void getAttachedActivity(final Application application, final OnAttachActivityCallback onAttachActivityCallback) {
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (onAttachActivityCallback != null) {
                    onAttachActivityCallback.onAttachActivity(activity);
                }
                application.unregisterActivityLifecycleCallbacks(this);
            }
        });
    }
}
