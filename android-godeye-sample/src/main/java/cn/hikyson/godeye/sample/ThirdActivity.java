package cn.hikyson.godeye.sample;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import cn.hikyson.android.godeye.sample.R;
import cn.hikyson.godeye.sample.fragmentlifecycle.BlankFragment1;
import cn.hikyson.godeye.sample.fragmentlifecycle.BlankFragment2;

public class ThirdActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content, new BlankFragment1(), "1").commit();

        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("1")).add(R.id.content, new BlankFragment2(), "2").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("2")).show(fragmentManager.findFragmentByTag("1")).commit();
                        } catch (Throwable ignore) {
                        }
                    }
                }, 2000);
            }
        });
    }
}
