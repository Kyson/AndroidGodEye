package cn.hikyson.godeye.core.helper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Test2NotV4FragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(new Test1NotV4Fragment(), Test1NotV4Fragment.class.getName()).commit();
    }

    public Test1NotV4Fragment getTest1NotV4Fragment() {
        return (Test1NotV4Fragment) getFragmentManager().findFragmentByTag(Test1NotV4Fragment.class.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
