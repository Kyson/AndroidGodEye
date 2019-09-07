package cn.hikyson.godeye.monitor.modules.appinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hikyson.godeye.monitor.GodEyeMonitor;
import github.nisrulz.easydeviceinfo.base.EasyAppMod;
import github.nisrulz.easydeviceinfo.base.EasyCpuMod;
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.base.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import github.nisrulz.easydeviceinfo.base.EasyNetworkMod;
import github.nisrulz.easydeviceinfo.base.EasySimMod;

public class AppInfo {
    private static GodEyeMonitor.AppInfoConext sAppInfoConext;

    public static void injectAppInfoConext(GodEyeMonitor.AppInfoConext appInfoConext) {
        sAppInfoConext = appInfoConext;
    }

    private static List<AppInfoLabel> sInternalLabels;
    private static String sAppName;

    public List<AppInfoLabel> labels;
    public String appName;

    public static class Factory {

        public static AppInfo create() {
            AppInfo appInfo = new AppInfo();
            Context context = GodEyeMonitor.getContext();
            if (context == null) {
                return appInfo;
            }
            synchronized (AppInfo.class) {
                if (TextUtils.isEmpty(sAppName)) {
                    PackageManager pm = context.getPackageManager();
                    sAppName = context.getApplicationInfo().loadLabel(pm).toString();
                }
                if (sInternalLabels == null) {
                    sInternalLabels = new ArrayList<>();

                    EasyAppMod easyAppMod = new EasyAppMod(context);
                    sInternalLabels.add(new AppInfoLabel("AppName:" + easyAppMod.getAppName(), null));
                    sInternalLabels.add(new AppInfoLabel("PackageName:" + easyAppMod.getPackageName(), null));
                    sInternalLabels.add(new AppInfoLabel("AppVersionCode:" + easyAppMod.getAppVersionCode(), null));
                    sInternalLabels.add(new AppInfoLabel("AppVersion:" + easyAppMod.getAppVersion(), null));
                    sInternalLabels.add(new AppInfoLabel("Install From:" + easyAppMod.getStore(), null));

                    EasyCpuMod easyCpuMod = new EasyCpuMod();
                    sInternalLabels.add(new AppInfoLabel("SupportedABIS:" + Arrays.toString(easyCpuMod.getSupportedABIS()), null));
                    sInternalLabels.add(new AppInfoLabel("Supported32bitABIS:" + Arrays.toString(easyCpuMod.getSupported32bitABIS()), null));
                    sInternalLabels.add(new AppInfoLabel("Supported64bitABIS:" + Arrays.toString(easyCpuMod.getSupported64bitABIS()), null));

                    EasyDeviceMod easyDeviceMod = new EasyDeviceMod(context);
                    sInternalLabels.add(new AppInfoLabel("Board:" + easyDeviceMod.getBoard(), null));
                    sInternalLabels.add(new AppInfoLabel("Bootloader:" + easyDeviceMod.getBootloader(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildBrand:" + easyDeviceMod.getBuildBrand(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildHost:" + easyDeviceMod.getBuildHost(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildID:" + easyDeviceMod.getBuildID(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildTags:" + easyDeviceMod.getBuildTags(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildUser:" + easyDeviceMod.getBuildUser(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildVersionCodename:" + easyDeviceMod.getBuildVersionCodename(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildVersionIncremental:" + easyDeviceMod.getBuildVersionIncremental(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildVersionRelease:" + easyDeviceMod.getBuildVersionRelease(), null));
                    sInternalLabels.add(new AppInfoLabel("Device:" + easyDeviceMod.getDevice(), null));
                    sInternalLabels.add(new AppInfoLabel("DisplayVersion:" + easyDeviceMod.getDisplayVersion(), null));
                    sInternalLabels.add(new AppInfoLabel("Fingerprint:" + easyDeviceMod.getFingerprint(), null));
                    sInternalLabels.add(new AppInfoLabel("Hardware:" + easyDeviceMod.getHardware(), null));
                    sInternalLabels.add(new AppInfoLabel("Language:" + easyDeviceMod.getLanguage(), null));
                    sInternalLabels.add(new AppInfoLabel("Manufacturer:" + easyDeviceMod.getManufacturer(), null));
                    sInternalLabels.add(new AppInfoLabel("Model:" + easyDeviceMod.getModel(), null));
                    sInternalLabels.add(new AppInfoLabel("OSCodename:" + easyDeviceMod.getOSCodename(), null));
                    sInternalLabels.add(new AppInfoLabel("OSVersion:" + easyDeviceMod.getOSVersion(), null));
                    sInternalLabels.add(new AppInfoLabel("PhoneNo:" + easyDeviceMod.getPhoneNo(), null));
                    sInternalLabels.add(new AppInfoLabel("Product:" + easyDeviceMod.getProduct(), null));
                    sInternalLabels.add(new AppInfoLabel("RadioVer:" + easyDeviceMod.getRadioVer(), null));
                    sInternalLabels.add(new AppInfoLabel("ScreenDisplayID:" + easyDeviceMod.getScreenDisplayID(), null));
                    sInternalLabels.add(new AppInfoLabel("Serial:" + easyDeviceMod.getSerial(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildTime:" + easyDeviceMod.getBuildTime(), null));
                    sInternalLabels.add(new AppInfoLabel("BuildVersionSDK:" + easyDeviceMod.getBuildVersionSDK(), null));
                    sInternalLabels.add(new AppInfoLabel("PhoneType:" + easyDeviceMod.getPhoneType(), null));

                    EasyDisplayMod easyDisplayMod = new EasyDisplayMod(context);
                    sInternalLabels.add(new AppInfoLabel("Density" + easyDisplayMod.getDensity(), null));
                    sInternalLabels.add(new AppInfoLabel("Resolution" + easyDisplayMod.getResolution(), null));
                    sInternalLabels.add(new AppInfoLabel("PhysicalSize" + easyDisplayMod.getPhysicalSize(), null));
                    sInternalLabels.add(new AppInfoLabel("RefreshRate" + easyDisplayMod.getRefreshRate(), null));

                    EasySimMod easySimMod = new EasySimMod(context);
                    sInternalLabels.add(new AppInfoLabel("MultiSim:" + easySimMod.isMultiSim(), null));
                    sInternalLabels.add(new AppInfoLabel("SimNetworkLocked:" + easySimMod.isSimNetworkLocked(), null));
                    sInternalLabels.add(new AppInfoLabel("Carrier:" + easySimMod.getCarrier(), null));
                    sInternalLabels.add(new AppInfoLabel("Country:" + easySimMod.getCountry(), null));
                    sInternalLabels.add(new AppInfoLabel("IMSI:" + easySimMod.getIMSI(), null));
                    sInternalLabels.add(new AppInfoLabel("SIMSerial:" + easySimMod.getSIMSerial(), null));
                    sInternalLabels.add(new AppInfoLabel("ActiveMultiSimInfo:" + easySimMod.getActiveMultiSimInfo(), null));
                    sInternalLabels.add(new AppInfoLabel("NumberOfActiveSim:" + easySimMod.getNumberOfActiveSim(), null));

                    if (PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                            || PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
                        EasyLocationMod easyLocationMod = new EasyLocationMod(context);
                        double[] l = easyLocationMod.getLatLong();
                        String lat = String.valueOf(l[0]);
                        String lon = String.valueOf(l[1]);
                        sInternalLabels.add(new AppInfoLabel("Location lat:" + lat, null));
                        sInternalLabels.add(new AppInfoLabel("Location lon:" + lon, null));
                    } else {
                        sInternalLabels.add(new AppInfoLabel("LocationInfo need permission ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION", null));
                    }
                    if (PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PermissionChecker.PERMISSION_GRANTED) {
                        EasyNetworkMod easyNetworkMod = new EasyNetworkMod(context);
                        sInternalLabels.add(new AppInfoLabel("IPv4Address:" + easyNetworkMod.getIPv4Address(), null));
                        sInternalLabels.add(new AppInfoLabel("IPv6Address:" + easyNetworkMod.getIPv6Address(), null));
                        sInternalLabels.add(new AppInfoLabel("WifiBSSID:" + easyNetworkMod.getWifiBSSID(), null));
                        sInternalLabels.add(new AppInfoLabel("WifiLinkSpeed:" + easyNetworkMod.getWifiLinkSpeed(), null));
                        sInternalLabels.add(new AppInfoLabel("WifiMAC:" + easyNetworkMod.getWifiMAC(), null));
                        sInternalLabels.add(new AppInfoLabel("WifiSSID:" + easyNetworkMod.getWifiSSID(), null));
                        sInternalLabels.add(new AppInfoLabel("NetworkType:" + easyNetworkMod.getNetworkType(), null));
                    } else {
                        sInternalLabels.add(new AppInfoLabel("NetworkInfo need permission ACCESS_NETWORK_STATE", null));
                    }
                }
            }
            List<AppInfoLabel> appInfoLabels = new ArrayList<>(sInternalLabels);
            if (sAppInfoConext != null && sAppInfoConext.getAppInfo() != null) {
                appInfoLabels.addAll(sAppInfoConext.getAppInfo());
            }
            appInfo.appName = sAppName;
            appInfo.labels = appInfoLabels;
            return appInfo;
        }
    }
}
