package cn.hikyson.android.godeye.sample;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LogView logView = findViewById(R.id.activity_main2_logview);
        TabLayout tabLayout = findViewById(R.id.activity_main2_tab);
        FrameLayout tabContent = findViewById(R.id.activity_main2_tabcontent);
    }
}
