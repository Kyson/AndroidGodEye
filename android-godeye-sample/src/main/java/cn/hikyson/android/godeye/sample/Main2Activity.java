package cn.hikyson.android.godeye.sample;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Objects;

import cn.hikyson.godeye.core.utils.L;

public class Main2Activity extends AppCompatActivity implements InstallFragment.OnInstallModuleChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LogView logView = findViewById(R.id.activity_main2_logview);
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                logView.log("DEBUG: " + msg);
            }

            @Override
            public void e(String msg) {
                logView.log("!ERROR: " + msg);
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                logView.log("!!EXCEPTION: " + e.getLocalizedMessage());
            }
        });
        TabLayout tabLayout = findViewById(R.id.activity_main2_tab);
        // 添加 tab item
        tabLayout.addTab(tabLayout.newTab().setText("Step1:Install"));
        tabLayout.addTab(tabLayout.newTab().setText("Step2:Consume"));
        tabLayout.addTab(tabLayout.newTab().setText("Tools"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.activity_main2_tabcontent, new InstallFragment(), InstallFragment.class.getSimpleName())
                .add(R.id.activity_main2_tabcontent, new ConsumeFragment(), ConsumeFragment.class.getSimpleName())
                .add(R.id.activity_main2_tabcontent, new ToolsFragment(), ToolsFragment.class.getSimpleName()).commitNow();
        showFragment(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            fragmentTransaction.hide(fragments.get(i));
        }
        String fragmentName = "";
        if (index == 0) {
            fragmentName = InstallFragment.class.getSimpleName();
        } else if (index == 1) {
            fragmentName = ConsumeFragment.class.getSimpleName();
        } else if (index == 2) {
            fragmentName = ToolsFragment.class.getSimpleName();
        }
        fragmentTransaction.show(Objects.requireNonNull(fragmentManager.findFragmentByTag(fragmentName))).commit();
    }

    @Override
    public void onInstallModuleChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ((ConsumeFragment) Objects.requireNonNull(fragmentManager.findFragmentByTag(ConsumeFragment.class.getSimpleName()))).onInstallModuleChanged();
    }
}
