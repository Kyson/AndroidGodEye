package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

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
