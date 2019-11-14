package cn.hikyson.android.godeye.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.PermissionChecker;
import android.widget.Toast;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartupTracer.get().onSplashCreate();
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            Toast.makeText(this, "grant " + Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "permission " + Manifest.permission.WRITE_EXTERNAL_STORAGE + " need!!!", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }
}
