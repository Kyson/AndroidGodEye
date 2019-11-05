package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        new Handler().postDelayed(() -> {
            ((TextView)findViewById(R.id.textView)).setText("I am SecondActivity111!");
            try {
                GodEyeHelper.onPageLoaded(SecondActivity.this);
            } catch (UninstallException e) {
                e.printStackTrace();
            }
        }, 2000);

        new Handler().postDelayed(() -> {
            ((TextView)findViewById(R.id.textView)).setText("I am SecondActivity222!");
            try {
                GodEyeHelper.onPageLoaded(SecondActivity.this);
            } catch (UninstallException e) {
                e.printStackTrace();
            }
        }, 3000);
    }

    public void JumpToThirdActivity(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}
