package cn.hikyson.android.godeye.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;

public class SecondActivity extends AppCompatActivity implements InstallFragment.OnInstallModuleChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        new Handler().postDelayed(() -> {
            ((TextView) findViewById(R.id.textView)).setText("I am SecondActivity111!");
            try {
                GodEyeHelper.onPageLoaded(SecondActivity.this);
            } catch (UninstallException e) {
                e.printStackTrace();
            }
        }, 2000);

        new Handler().postDelayed(() -> {
            ((TextView) findViewById(R.id.textView)).setText("I am SecondActivity222!");
            try {
                GodEyeHelper.onPageLoaded(SecondActivity.this);
            } catch (UninstallException e) {
                e.printStackTrace();
            }
        }, 3000);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.activity_second_container, new InstallFragment(), InstallFragment.class.getSimpleName()).commit();

        new Handler().postDelayed(() -> {
            fragmentManager.beginTransaction().replace(R.id.activity_second_container, new ConsumeFragment(), ConsumeFragment.class.getSimpleName()).commit();
        }, 2000);
    }

    public void JumpToThirdActivity(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInstallModuleChanged() {
        // do nothing
    }
}
