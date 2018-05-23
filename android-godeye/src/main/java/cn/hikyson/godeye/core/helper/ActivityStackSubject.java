package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class ActivityStackSubject {
    private Subject<Activity> mSubject;

    public ActivityStackSubject(Application application) {
        mSubject = BehaviorSubject.create();
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mSubject.onNext(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Observable<Activity> topActivityObservable() {
        return mSubject;
    }
}
