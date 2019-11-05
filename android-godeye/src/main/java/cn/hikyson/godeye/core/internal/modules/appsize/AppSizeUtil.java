package cn.hikyson.godeye.core.internal.modules.appsize;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;


import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;


class AppSizeUtil {

    static void getAppSize(Context context, OnGetSizeListener listener) {
        if (listener == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getAppSizeAboveO(context, listener);
            } else {
                getAppSizeLowerO(context, listener);
            }
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    /**
     * 获取应用的大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void getAppSizeAboveO(Context context, @NonNull OnGetSizeListener listener) {
        StorageStatsManager storageStatsManager = (StorageStatsManager) context
                .getSystemService(Context.STORAGE_STATS_SERVICE);
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        // 获取所有应用的StorageVolume列表
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        for (StorageVolume item : storageVolumes) {
            String uuidStr = item.getUuid();
            UUID uuid;
            if (uuidStr == null) {
                uuid = StorageManager.UUID_DEFAULT;
            } else {
                uuid = UUID.fromString(uuidStr);
            }
            int uid = getUid(context, context.getPackageName());
            // 通过包名获取uid
            StorageStats storageStats;
            try {
                storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
                AppSizeInfo ctAppSizeInfo = new AppSizeInfo();
                ctAppSizeInfo.cacheSize = storageStats.getCacheBytes();
                ctAppSizeInfo.dataSize = storageStats.getDataBytes();
                ctAppSizeInfo.codeSize = storageStats.getAppBytes();
                listener.onGetSize(ctAppSizeInfo);
            } catch (IOException e) {
                listener.onError(e);
            }
        }
    }

    /**
     * 根据应用包名获取对应uid
     */
    private static int getUid(Context context, String pakName) {
        try {
            return context.getPackageManager().getApplicationInfo(pakName, PackageManager.GET_META_DATA).uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取应用大小8.0以下
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static void getAppSizeLowerO(Context context, @NonNull final OnGetSizeListener listener) {
        try {
            Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
            method.invoke(context.getPackageManager(), context.getPackageName(), new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                    AppSizeInfo ctAppSizeInfo = new AppSizeInfo();
                    ctAppSizeInfo.cacheSize = pStats.cacheSize;
                    ctAppSizeInfo.dataSize = pStats.dataSize;
                    ctAppSizeInfo.codeSize = pStats.codeSize;
                    listener.onGetSize(ctAppSizeInfo);
                }
            });
        } catch (Throwable e) {
            listener.onError(e);
        }
    }

    public interface OnGetSizeListener {
        void onGetSize(AppSizeInfo ctAppSizeInfo);
        void onError(Throwable t);
    }

    static String formatSize(long size) {
        try {
            if (size / (1024 * 1024 * 1024) > 0) {
                float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
                DecimalFormat df = new DecimalFormat("#.##");
                return "" + df.format(tmpSize) + "GB";
            } else if (size / (1024 * 1024) > 0) {
                float tmpSize = (float) (size) / (float) (1024 * 1024);
                DecimalFormat df = new DecimalFormat("#.##");
                return "" + df.format(tmpSize) + "MB";
            } else if (size / 1024 > 0) {
                return "" + (size / (1024)) + "KB";
            } else
                return "" + size + "B";
        } catch (Exception e) {
            return "";
        }
    }
}

