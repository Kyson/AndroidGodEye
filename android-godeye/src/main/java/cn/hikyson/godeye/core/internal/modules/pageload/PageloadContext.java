package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Application;
import androidx.annotation.NonNull;

/**
 * Created by kysonchao on 2018/1/25.
 */
public interface PageloadContext {
    Application application();
    @NonNull
    // PageInfoProvider
    String pageInfoProvider();
}
