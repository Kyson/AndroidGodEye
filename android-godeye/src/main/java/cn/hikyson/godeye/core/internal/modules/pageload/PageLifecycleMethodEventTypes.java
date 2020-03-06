package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.hikyson.methodcanary.lib.MethodEvent;

public class PageLifecycleMethodEventTypes {

    @Keep
    public static class MethodInfoForLifecycle implements Serializable {
        PageType pageType;
        String methodName;
        String methodDesc;

        public MethodInfoForLifecycle(PageType pageType, String methodName, String methodDesc) {
            this.pageType = pageType;
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        public MethodInfoForLifecycle(PageType pageType, MethodEvent methodEvent) {
            this.pageType = pageType;
            this.methodName = methodEvent.methodName;
            this.methodDesc = methodEvent.methodDesc;
        }

        @Override
        public String toString() {
            return "MethodInfoForLifecycle{" +
                    "pageType=" + pageType +
                    ", methodName='" + methodName + '\'' +
                    ", methodDesc='" + methodDesc + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodInfoForLifecycle that = (MethodInfoForLifecycle) o;

            if (pageType != that.pageType) return false;
            if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null)
                return false;
            return methodDesc != null ? methodDesc.equals(that.methodDesc) : that.methodDesc == null;
        }

        @Override
        public int hashCode() {
            int result = pageType != null ? pageType.hashCode() : 0;
            result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
            result = 31 * result + (methodDesc != null ? methodDesc.hashCode() : 0);
            return result;
        }
    }


    static MethodInfoForLifecycle ON_ACTIVITY_CREATE = new MethodInfoForLifecycle(PageType.ACTIVITY, "onCreate", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_ACTIVITY_START = new MethodInfoForLifecycle(PageType.ACTIVITY, "onStart", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_RESUME = new MethodInfoForLifecycle(PageType.ACTIVITY, "onResume", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_PAUSE = new MethodInfoForLifecycle(PageType.ACTIVITY, "onPause", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_STOP = new MethodInfoForLifecycle(PageType.ACTIVITY, "onStop", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_SAVE_INSTANCE_STATE = new MethodInfoForLifecycle(PageType.ACTIVITY, "onSaveInstanceState", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_ACTIVITY_DESTORY = new MethodInfoForLifecycle(PageType.ACTIVITY, "onDestroy", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_ATTACH = new MethodInfoForLifecycle(PageType.FRAGMENT, "onAttach", "(Landroid/app/Activity;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_CREATE = new MethodInfoForLifecycle(PageType.FRAGMENT, "onCreate", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_VIEW_CREATE = new MethodInfoForLifecycle(PageType.FRAGMENT, "onCreateView", "(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;");
    static MethodInfoForLifecycle ON_FRAGMENT_START = new MethodInfoForLifecycle(PageType.FRAGMENT, "onStart", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_RESUME = new MethodInfoForLifecycle(PageType.FRAGMENT, "onResume", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_PAUSE = new MethodInfoForLifecycle(PageType.FRAGMENT, "onPause", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_STOP = new MethodInfoForLifecycle(PageType.FRAGMENT, "onStop", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_SAVE_INSTANCE_STATE = new MethodInfoForLifecycle(PageType.FRAGMENT, "onSaveInstanceState", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_VIEW_DESTROY = new MethodInfoForLifecycle(PageType.FRAGMENT, "onDestroyView", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_DESTROY = new MethodInfoForLifecycle(PageType.FRAGMENT, "onDestory", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_DETACH = new MethodInfoForLifecycle(PageType.FRAGMENT, "onDetach", "()V");

    static Map<MethodInfoForLifecycle, LifecycleEvent> sPairsOfLifecycleAndMethods;

    static {
        sPairsOfLifecycleAndMethods = new HashMap<>();
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_CREATE, ActivityLifecycleEvent.ON_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_START, ActivityLifecycleEvent.ON_START);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_RESUME, ActivityLifecycleEvent.ON_RESUME);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_PAUSE, ActivityLifecycleEvent.ON_PAUSE);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_STOP, ActivityLifecycleEvent.ON_STOP);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_DESTORY, ActivityLifecycleEvent.ON_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_ATTACH, FragmentLifecycleEvent.ON_ATTACH);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_CREATE, FragmentLifecycleEvent.ON_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_VIEW_CREATE, FragmentLifecycleEvent.ON_VIEW_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_START, FragmentLifecycleEvent.ON_START);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_RESUME, FragmentLifecycleEvent.ON_RESUME);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_PAUSE, FragmentLifecycleEvent.ON_PAUSE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_STOP, FragmentLifecycleEvent.ON_STOP);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_VIEW_DESTROY, FragmentLifecycleEvent.ON_VIEW_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_DESTROY, FragmentLifecycleEvent.ON_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_DETACH, FragmentLifecycleEvent.ON_DETACH);
    }

    static boolean isMatch(LifecycleEvent lifecycleEvent0, LifecycleEvent lifecycleEvent1) {
        return lifecycleEvent0.equals(lifecycleEvent1);
    }

    static LifecycleEvent convert(PageType pageType, MethodEvent lifecycleMethodEvent) {
        return sPairsOfLifecycleAndMethods.get(new MethodInfoForLifecycle(pageType, lifecycleMethodEvent));
    }
}
