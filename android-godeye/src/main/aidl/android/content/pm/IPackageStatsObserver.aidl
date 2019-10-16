// IPackageStatsObserver.aidl
package android.content.pm;

// 包名必须在android.content.pm
// https://www.baidufe.com/item/8786bc2e95a042320bef.html
import android.content.pm.PackageStats;
interface IPackageStatsObserver {
    oneway void onGetStatsCompleted(in PackageStats pStats, boolean succeeded);
}
