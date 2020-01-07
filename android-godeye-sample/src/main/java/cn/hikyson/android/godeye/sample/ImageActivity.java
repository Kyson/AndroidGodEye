package cn.hikyson.android.godeye.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.utils.ThreadUtil;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        findViewById(R.id.activity_image_change_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView) findViewById(R.id.activity_image_iv2)).setImageResource(R.drawable.image_test1);
            }
        });
        findViewById(R.id.activity_image_change_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.sMainScheduler.scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.activity_image_iv3).setVisibility(View.INVISIBLE);
                    }
                }, 0, TimeUnit.SECONDS);
                ThreadUtil.sMainScheduler.scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.activity_image_iv3).setVisibility(View.VISIBLE);
                    }
                }, 3, TimeUnit.SECONDS);
                ThreadUtil.sMainScheduler.scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.activity_image_iv3).setVisibility(View.GONE);
                    }
                }, 6, TimeUnit.SECONDS);
            }
        });
    }
}
