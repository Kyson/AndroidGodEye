package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.internal.HeapAnalyzerService;

public class LeakActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                ((TextView) LeakActivity.this.findViewById(R.id.activity_leak_test)).setText("Yes, i am leaking...");
            }
        }).start();
    }
}
