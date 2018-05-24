package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ActivityStackSubject {
    private Subject<WeakReference<Activity>> mSubject;

    public ActivityStackSubject(Application application) {
        mSubject = BehaviorSubject.create();
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mSubject.onNext(new WeakReference<>(activity));
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public Observable<WeakReference<Activity>> topActivityObservable() {
        return mSubject;
    }
}
