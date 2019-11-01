package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;



public class LeakActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);



        findViewById(R.id.btn_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LeakFragment fragment = new LeakFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }
            }
        });
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
