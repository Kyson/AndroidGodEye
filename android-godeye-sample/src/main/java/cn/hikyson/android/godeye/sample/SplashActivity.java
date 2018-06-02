package cn.hikyson.android.godeye.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import cn.hikyson.android.godeye.toolbox.StartupTracer;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
