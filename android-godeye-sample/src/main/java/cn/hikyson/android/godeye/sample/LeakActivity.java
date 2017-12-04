package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class LeakActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
    }

    public void leak(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

}
