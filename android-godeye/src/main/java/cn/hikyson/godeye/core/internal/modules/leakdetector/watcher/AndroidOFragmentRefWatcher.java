/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hikyson.godeye.core.internal.modules.leakdetector.watcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.FragmentRefWatcher;

import cn.hikyson.godeye.core.internal.modules.leakdetector.release.LeakRefNameProvider;

@RequiresApi(Build.VERSION_CODES.O)
public class AndroidOFragmentRefWatcher implements FragmentRefWatcher {

    private final RefWatcher refWatcher;
    private final LeakRefNameProvider referenceNameConverter;

    public AndroidOFragmentRefWatcher(RefWatcher refWatcher, LeakRefNameProvider referenceNameConverter) {
        this.refWatcher = refWatcher;
        this.referenceNameConverter = referenceNameConverter;
    }

    private final FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks =
            new FragmentManager.FragmentLifecycleCallbacks() {

                @Override
                public void onFragmentViewDestroyed(FragmentManager fm, Fragment fragment) {
                    View view = fragment.getView();
                    if (view != null) {
                        refWatcher.watch(view, referenceNameConverter.convertFragment(fragment));
                    }
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment fragment) {
                    refWatcher.watch(fragment, referenceNameConverter.convertFragment(fragment));
                }
            };

    @Override
    public void watchFragments(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true);
    }
}
