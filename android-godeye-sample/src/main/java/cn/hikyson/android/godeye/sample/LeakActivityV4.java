package cn.hikyson.android.godeye.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class LeakActivityV4 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_v4);
        LeakFragmentV4 fragmentV4 = new LeakFragmentV4();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragmentV4).commit();
        AndroidSchedulers.mainThread().scheduleDirect(() -> getSupportFragmentManager().beginTransaction().remove(fragmentV4).commit(), 2, TimeUnit.SECONDS);
    }
}
