package cn.hikyson.android.godeye.sample;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Objects;

import cn.hikyson.android.godeye.sample.fragmentlifecycle.BlankFragment1;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LogView logView = findViewById(R.id.activity_main2_logview);
        TabLayout tabLayout = findViewById(R.id.activity_main2_tab);
        FrameLayout tabContent = findViewById(R.id.activity_main2_tabcontent);

        // 添加 tab item
        tabLayout.addTab(tabLayout.newTab().setText("1. Install"));
        tabLayout.addTab(tabLayout.newTab().setText("2. Consume"));
        tabLayout.addTab(tabLayout.newTab().setText("3. Tools"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.activity_main2_tabcontent, new InstallFragment(), InstallFragment.class.getSimpleName())
                .add(R.id.activity_main2_tabcontent, new ConsumeFragment(), ConsumeFragment.class.getSimpleName())
                .add(R.id.activity_main2_tabcontent, new ToolsFragment(), ToolsFragment.class.getSimpleName()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<Fragment> fragments = fragmentManager.getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    fragmentManager.beginTransaction().hide(fragments.get(i)).commit();
                }
                String fragmentName = "";
                if (tab.getPosition() == 0) {
                    fragmentName = InstallFragment.class.getSimpleName();
                } else if (tab.getPosition() == 1) {
                    fragmentName = ConsumeFragment.class.getSimpleName();
                } else if (tab.getPosition() == 2) {
                    fragmentName = ToolsFragment.class.getSimpleName();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag(fragmentName))).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
