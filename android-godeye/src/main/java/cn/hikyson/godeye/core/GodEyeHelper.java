package cn.hikyson.godeye.core;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import cn.hikyson.godeye.core.exceptions.UnexpectException;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.utils.L;

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
}
