package cn.hikyson.godeye.core;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Objects;

public class GodEyeInitContentProvider extends ContentProvider {

    public GodEyeInitContentProvider() {
    }

    private static final String DEFAULT_ASSETS_INSTALL_CONFIG = "android-godeye-config/install.config";

    @Override
    public boolean onCreate() {
        Application application = (Application) Objects.requireNonNull(this.getContext()).getApplicationContext();
        GodEye.instance().internalInit(application);
        if (!application.getResources().getBoolean(R.bool.android_god_eye_manual_install)) {
            GodEye.instance().install(GodEyeConfig.fromAssets(DEFAULT_ASSETS_INSTALL_CONFIG));
            GodEyeHelper.startMonitor(application.getResources().getInteger(R.integer.android_god_eye_monitor_port));
        }
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
