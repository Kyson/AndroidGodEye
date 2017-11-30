package cn.hikyson.godeye.monitor.modules;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class AppInfoModule extends BaseModule<AppInfoModule.AppInfo> {

    @Override
    AppInfo popData() {
        if (AppInfo.sAppInfoProxy == null || AppInfo.sAppInfoProxy.getAppInfo() == null) {
            return null;
        }
        return AppInfo.sAppInfoProxy.getAppInfo();
    }

    public static class AppInfo {
        public String appName;
        public List<String> extentions;

        public AppInfo(Context context, Map<String, Object> extentions) {
            PackageManager pm = context.getPackageManager();
            this.appName = context.getApplicationInfo().loadLabel(pm).toString();
            this.extentions = new ArrayList<>();
            for (Map.Entry<String, Object> entry : extentions.entrySet()) {
                this.extentions.add(entry.getKey() + " : " + entry.getValue());
            }
        }

        public interface AppInfoProxy {
            AppInfo getAppInfo();
        }

        private static AppInfoProxy sAppInfoProxy;

        /**
         * 注入获取app info的代理，代理实现必须不依赖任何页面，否则内存泄漏
         *
         * @param appInfoProxy
         */
        public static void injectAppInfoProxy(AppInfoProxy appInfoProxy) {
            sAppInfoProxy = appInfoProxy;
        }


//            LinkedHashMap<String, String> debugInfos = new LinkedHashMap<>();
//            debugInfos.put("VersionName", ContextHolder.sVersionName);
//            debugInfos.put("VersionCode", String.valueOf(ContextHolder.sVersionCode));
//            debugInfos.put("BuildType", ContextHolder.sBuildType);
//            debugInfos.put("Debuggable", String.valueOf(ContextHolder.sDebug));
//            debugInfos.put("Channel", ChannelUtil.getChannel(ContextHolder.sContext));
//            debugInfos.put("Uid", AccountManager.get().getUid());
//            debugInfos.put("Email", AccountManager.get().getUserEmail());
//            debugInfos.put("ClientId", CtripSDKConfig.getClientID());
//            debugInfos.put("DeviceId", CtripSDKManager.getInstance().getDeviceID());
//        }
    }
}
