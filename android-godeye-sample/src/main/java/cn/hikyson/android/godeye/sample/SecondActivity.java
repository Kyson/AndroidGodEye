package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.textView)).setText("I am SecondActivity!");
                GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).onPageLoaded(SecondActivity.this);
            }
        }, 2000);
    }

    public void JumpToThirdActivity(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}
