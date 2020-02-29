package cn.hikyson.godeye.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.hikyson.android.godeye.sample.R;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class LeakActivity extends Activity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        mTv = this.findViewById(R.id.activity_leak_test);
        findViewById(R.id.btn_fragment).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LeakFragment fragment = new LeakFragment();
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                AndroidSchedulers.mainThread().scheduleDirect(() -> getFragmentManager().beginTransaction().remove(fragment).commit(), 2, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(() -> {
            try {
                Thread.sleep(200000);
                mTv.setText("Leak");
            } catch (InterruptedException e) {
            }
        }).start();
    }
}
