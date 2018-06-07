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

public class ActivityStackSubject {

    private List<Activity> mActivities = new ArrayList<>();
    private Application mApplication;

    @UiThread
    public ActivityStackSubject(final Application application) {
        ThreadUtil.ensureMainThread();
        mApplication = application;
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            @UiThread
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ThreadUtil.ensureMainThread();
                mActivities.add(activity);
            }

            @Override
            @UiThread
            public void onActivityDestroyed(Activity activity) {
                ThreadUtil.ensureMainThread();
                mActivities.remove(activity);
            }
        });
    }

    @UiThread
    public Observable<Activity> topActivityObservable() {
        ThreadUtil.ensureMainThread();
        if (mActivities.isEmpty()) {
            return Observable.create(new ObservableOnSubscribe<Activity>() {
                @Override
                public void subscribe(final ObservableEmitter<Activity> e) throws Exception {
                    ThreadUtil.ensureMainThread();
                    mApplication.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
                        @Override
                        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                            ThreadUtil.ensureMainThread();
                            e.onNext(activity);
                            e.onComplete();
                            mApplication.unregisterActivityLifecycleCallbacks(this);
                        }
                    });
                }
            }).subscribeOn(AndroidSchedulers.mainThread());
        } else {
            return Observable.just(mActivities.get(0));
        }
    }
}
