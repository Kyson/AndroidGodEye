package cn.hikyson.godeye.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import cn.hikyson.godeye.core.exceptions.UnexpectException;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.monitor.AppInfoConext;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ReflectUtil;

/**
 * core extension helper
 */
public class GodEyeHelper {
    /**
     * call this method when activity or fragment's content loaded
     *
     * @param page
     * @throws UninstallException
     */
    public static void onPageLoaded(Object page) throws UninstallException {
        if (page instanceof Fragment) {
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentV4Load((Fragment) page);
        } else if (page instanceof android.app.Fragment) {
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentLoad((android.app.Fragment) page);
        } else if (page instanceof Activity) {
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onActivityLoad((Activity) page);
        } else {
            throw new UnexpectException("GodEyeHelper.onPageLoaded's page must be instance of Fragment or Activity.");
        }
        L.d("GodEyeHelper onPageLoaded: " + page.getClass().getSimpleName());
    }

    /**
     * call this method when fragment visibility changed
     *
     * @param fragmentPage
     * @param visible
     * @throws UninstallException
     */
    public static void onFragmentPageVisibilityChange(Object fragmentPage, boolean visible) throws UninstallException {
        if (fragmentPage instanceof Fragment) {
            if (visible) {
                GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentV4Show((Fragment) fragmentPage);
            } else {
                GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentV4Hide((Fragment) fragmentPage);
            }
        } else if (fragmentPage instanceof android.app.Fragment) {
            if (visible) {
                GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentShow((android.app.Fragment) fragmentPage);
            } else {
                GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onFragmentHide((android.app.Fragment) fragmentPage);
            }
        } else {
            throw new UnexpectException("GodEyeHelper.onFragmentPageVisibilityChange's fragmentPage must be instance of Fragment.");
        }
        L.d("GodEyeHelper onFragmentPageVisibilityChange: " + fragmentPage.getClass().getSimpleName() + ", visible:" + visible);
    }

    /**
     * call this method when app startup end
     *
     * @param startupInfo
     * @throws UninstallException
     */
    public static void onAppStartEnd(StartupInfo startupInfo) throws UninstallException {
        GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).produce(startupInfo);
        L.d("GodEyeHelper onAppStartEnd: " + startupInfo);
    }

    /**
     * call this method when network request end
     *
     * @param networkInfo
     * @throws UninstallException
     */
    public static void onNetworkEnd(NetworkInfo networkInfo) throws UninstallException {
        GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).produce(networkInfo);
        L.d("GodEyeHelper onNetworkEnd: %s", networkInfo == null ? "null" : networkInfo.toSummaryString());
    }

    /**
     * inspect view of top activity
     *
     * @throws UninstallException
     * @deprecated you do not need to call this function,viewCanary will auto detect view issues
     */
    @Deprecated
    public static void inspectView() throws UninstallException {
    }

    /**
     * method canary start recording
     *
     * @param tag
     * @throws UninstallException
     */
    public static void startMethodCanaryRecording(String tag) throws UninstallException {
        final MethodCanary methodCanary = GodEye.instance().getModule(GodEye.ModuleName.METHOD_CANARY);
        methodCanary.startMonitor(tag);
    }

    /**
     * method canary stop recording
     *
     * @param tag
     * @throws UninstallException
     */
    public static void stopMethodCanaryRecording(String tag) throws UninstallException {
        final MethodCanary methodCanary = GodEye.instance().getModule(GodEye.ModuleName.METHOD_CANARY);
        methodCanary.stopMonitor(tag);
    }

    /**
     * method canary is recording
     *
     * @param tag
     * @return
     * @throws UninstallException
     */
    public static boolean isMethodCanaryRecording(String tag) throws UninstallException {
        final MethodCanary methodCanary = GodEye.instance().getModule(GodEye.ModuleName.METHOD_CANARY);
        return methodCanary.isRunning(tag);
    }

    /**
     * set appinfo show in monitor dashboard
     * please run this in main process
     *
     * @param appInfoConext
     */
    public static void setMonitorAppInfoConext(AppInfoConext appInfoConext) {
        ReflectUtil.invokeStaticMethod("cn.hikyson.godeye.monitor.modules.appinfo.AppInfo", "injectAppInfoConext", new Class[]{AppInfoConext.class}, new Object[]{appInfoConext});
    }

    /**
     * start monitor
     * please run this in main process
     *
     * @param port
     */
    public static void startMonitor(int port) {
        long startTime = System.currentTimeMillis();
        try {
            ReflectUtil.invokeStaticMethodUnSafe("cn.hikyson.godeye.monitor.GodEyeMonitor", "work", new Class[]{Context.class, int.class}, new Object[]{GodEye.instance().getApplication(), port});
        } catch (Exception e) {
            Log.w(L.DEFAULT_TAG, "You should add dependency 'implementation project(':android-godeye-monitor')' if you want to start GodEyeMonitor.");
        }
        Log.d(L.DEFAULT_TAG, String.format("GodEye start monitor cost %sms.", (System.currentTimeMillis() - startTime)));
    }

    private static final int DEFAULT_PORT = 5390;

    /**
     * please run this in main process
     */
    public static void startMonitor() {
        startMonitor(DEFAULT_PORT);
    }

    /**
     * shutdown monitor
     * please run this in main process
     */
    public static void shutdownMonitor() {
        long startTime = System.currentTimeMillis();
        try {
            ReflectUtil.invokeStaticMethodUnSafe("cn.hikyson.godeye.monitor.GodEyeMonitor", "shutDown", new Class[]{}, new Object[]{});
        } catch (Exception e) {
            Log.w(L.DEFAULT_TAG, "You should add dependency 'implementation project(':android-godeye-monitor')' if you want to shutdown GodEyeMonitor.");
        }
        Log.d(L.DEFAULT_TAG, String.format("GodEye shutdown monitor cost %sms.", (System.currentTimeMillis() - startTime)));
    }
}
