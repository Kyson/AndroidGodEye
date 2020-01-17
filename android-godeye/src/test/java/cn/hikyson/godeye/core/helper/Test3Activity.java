package cn.hikyson.godeye.core.helper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Test3Activity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(new Test2Fragment(), Test2Fragment.class.getName()).commit();
    }

    public Test2Fragment getTest2Fragment() {
        return (Test2Fragment) getFragmentManager().findFragmentByTag(Test2Fragment.class.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
