package cn.hikyson.android.godeye.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


public class LeakActivityV4 extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_v4);

        findViewById(R.id.btn_fragment_v4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeakFragmentV4 fragmentV4 = new LeakFragmentV4();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragmentV4).commit();
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
                ((TextView) LeakActivityV4.this.findViewById(R.id.activity_leak_test)).setText("Yes, i am leaking...");
            }
        }).start();
    }
}
