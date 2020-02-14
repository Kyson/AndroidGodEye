package cn.hikyson.godeye.core.helper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class TestViewCanaryActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(new TestViewCanaryFragment(), TestViewCanaryFragment.class.getName()).commit();
    }

    public TestViewCanaryFragment getTestViewCanaryFragment() {
        return (TestViewCanaryFragment) getSupportFragmentManager().findFragmentByTag(TestViewCanaryFragment.class.getName());
    }

    public void layoutChange() {
        getTestViewCanaryFragment().layoutChange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
