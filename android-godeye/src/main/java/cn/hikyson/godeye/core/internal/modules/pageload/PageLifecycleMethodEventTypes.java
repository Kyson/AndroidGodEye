package cn.hikyson.godeye.core.internal.modules.pageload;

import java.util.HashMap;
import java.util.Map;

import cn.hikyson.methodcanary.lib.MethodEvent;

public class PageLifecycleMethodEventTypes {

    public static class MethodInfoForLifecycle {
        String name;
        String desc;

        public MethodInfoForLifecycle(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public MethodInfoForLifecycle(MethodEvent methodEvent) {
            this.name = methodEvent.className;
            this.desc = methodEvent.methodDesc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodInfoForLifecycle that = (MethodInfoForLifecycle) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return desc != null ? desc.equals(that.desc) : that.desc == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            return result;
        }
    }

    static MethodInfoForLifecycle ON_ACTIVITY_CREATE = new MethodInfoForLifecycle("onCreate", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_ACTIVITY_START = new MethodInfoForLifecycle("onStart", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_RESUME = new MethodInfoForLifecycle("onResume", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_PAUSE = new MethodInfoForLifecycle("onPause", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_STOP = new MethodInfoForLifecycle("onStop", "()V");
    static MethodInfoForLifecycle ON_ACTIVITY_SAVE_INSTANCE_STATE = new MethodInfoForLifecycle("onSaveInstanceState", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_ACTIVITY_DESTORY = new MethodInfoForLifecycle("onDestroy", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_ATTACH = new MethodInfoForLifecycle("onAttach", "(Landroid/app/Activity;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_CREATE = new MethodInfoForLifecycle("onCreate", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_VIEW_CREATE = new MethodInfoForLifecycle("onCreateView", "(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;");
    static MethodInfoForLifecycle ON_FRAGMENT_START = new MethodInfoForLifecycle("onStart", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_RESUME = new MethodInfoForLifecycle("onResume", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_PAUSE = new MethodInfoForLifecycle("onPause", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_STOP = new MethodInfoForLifecycle("onStop", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_SAVE_INSTANCE_STATE = new MethodInfoForLifecycle("onSaveInstanceState", "(Landroid/os/Bundle;)V");
    static MethodInfoForLifecycle ON_FRAGMENT_VIEW_DESTROY = new MethodInfoForLifecycle("onDestroyView", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_DESTROY = new MethodInfoForLifecycle("onDestory", "()V");
    static MethodInfoForLifecycle ON_FRAGMENT_DETACH = new MethodInfoForLifecycle("onDetach", "()V");

    static Map<MethodInfoForLifecycle, LifecycleEvent> sPairsOfLifecycleAndMethods;

    static {
        sPairsOfLifecycleAndMethods = new HashMap<>();
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_CREATE, ActivityLifecycleEvent.ON_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_START, ActivityLifecycleEvent.ON_START);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_RESUME, ActivityLifecycleEvent.ON_RESUME);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_PAUSE, ActivityLifecycleEvent.ON_PAUSE);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_STOP, ActivityLifecycleEvent.ON_STOP);
        // TODO KYSON IMPL
//        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_SAVE_INSTANCE_STATE,ActivityLifecycleEvent.sa);
        sPairsOfLifecycleAndMethods.put(ON_ACTIVITY_DESTORY, ActivityLifecycleEvent.ON_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_ATTACH, FragmentLifecycleEvent.ON_ATTACH);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_CREATE, FragmentLifecycleEvent.ON_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_VIEW_CREATE, FragmentLifecycleEvent.ON_VIEW_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_START, FragmentLifecycleEvent.ON_START);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_RESUME, FragmentLifecycleEvent.ON_RESUME);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_PAUSE, FragmentLifecycleEvent.ON_PAUSE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_STOP, FragmentLifecycleEvent.ON_STOP);
        // TODO KYSON IMPL
//        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_SAVE_INSTANCE_STATE,FragmentLifecycleEvent.ON_CREATE);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_VIEW_DESTROY, FragmentLifecycleEvent.ON_VIEW_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_DESTROY, FragmentLifecycleEvent.ON_DESTROY);
        sPairsOfLifecycleAndMethods.put(ON_FRAGMENT_DETACH, FragmentLifecycleEvent.ON_DETACH);
    }

    static boolean isMatch(LifecycleEvent lifecycleEvent, MethodEvent lifecycleMethodEvent) {
        return lifecycleEvent.equals(sPairsOfLifecycleAndMethods.get(new MethodInfoForLifecycle(lifecycleMethodEvent)));
    }

    static boolean isMatch(MethodEvent lifecycleMethodEvent0, MethodEvent lifecycleMethodEvent1) {
        return new MethodInfoForLifecycle(lifecycleMethodEvent0).equals(new MethodInfoForLifecycle(lifecycleMethodEvent1));
    }

    static boolean isMatch(LifecycleEvent lifecycleEvent0, LifecycleEvent lifecycleEvent1) {
        return lifecycleEvent0.equals(lifecycleEvent1);
    }

    static LifecycleEvent convert(MethodEvent lifecycleMethodEvent) {
        return sPairsOfLifecycleAndMethods.get(new MethodInfoForLifecycle(lifecycleMethodEvent));
    }
}
