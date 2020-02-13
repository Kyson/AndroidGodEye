package cn.hikyson.godeye.core.helper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.utils.ThreadUtil;

public class TestLeak0Activity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreadUtil.sMainScheduler.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = TestLeak0Activity.this.getActionBar();
            }
        }, 8, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
