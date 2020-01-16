package cn.hikyson.godeye.core.helper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Test2Activity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(new Test1Fragment(), Test1Fragment.class.getName()).commit();
    }

    public Test1Fragment getTest1Fragment() {
        return (Test1Fragment) getSupportFragmentManager().findFragmentByTag(Test1Fragment.class.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
