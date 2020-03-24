package cn.hikyson.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.hikyson.android.godeye.sample.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartupTracer.get().onSplashCreate();
        Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }
}
