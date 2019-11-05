package cn.hikyson.android.godeye.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import io.reactivex.disposables.CompositeDisposable;


public class ConsumeFragment extends Fragment {
    private CompositeDisposable mCompositeDisposable;

    public ConsumeFragment() {
    }

    private LinearLayout mCbGroup;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consume, container, false);
        mCbGroup = view.findViewById(R.id.fragment_consume_cb_group);
        view.findViewById(R.id.fragment_consume_start_debug_monitor).setOnClickListener(v -> {
            GodEyeMonitor.injectAppInfoConext(new AppInfoProxyImpl());
            GodEyeMonitor.setClassPrefixOfAppProcess(Collections.singletonList("cn.hikyson.android.godeye.sample"));
            // CN_HIKYSON_ANDROID_GODEYE_MONITOR_PORT is define at rootDir/gradle.properties as ANDROID_GODEYE_MONITOR_PORT
            GodEyeMonitor.work(ConsumeFragment.this.getActivity(), getResources().getInteger(R.integer.CN_HIKYSON_ANDROID_GODEYE_MONITOR_PORT));
        });
        view.findViewById(R.id.fragment_consume_stop_debug_monitor).setOnClickListener(v -> {
            GodEyeMonitor.shutDown();
        });
        Switch switchView = view.findViewById(R.id.fragment_consume_select_all);
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> toggleAllModule(isChecked));
        view.findViewById(R.id.fragment_consume_start_log_consumer).setOnClickListener(v -> {
            if (mCompositeDisposable != null) {
                mCompositeDisposable.dispose();
            }
            mCompositeDisposable = new CompositeDisposable();
            Set<String> modules = getModulesSelected();
            StringBuilder sb = new StringBuilder();
            for (@GodEye.ModuleName String module : modules) {
                try {
                    mCompositeDisposable.add(GodEye.instance().observeModule(module, new LogObserver<>(module, msg -> {
                        L.d(msg);
                    })));
                    sb.append(module).append(", ");
                } catch (UninstallException e) {
                    L.e(String.valueOf(e));
                }
            }
            L.d("Current Log Consumers:" + sb);
        });
        view.findViewById(R.id.fragment_consume_stop_log_consumer).setOnClickListener(v -> {
            if (mCompositeDisposable != null) {
                mCompositeDisposable.dispose();
            }
            L.d("Current No Log Consumers");
        });
        return view;
    }

    private Set<String> getModulesSelected() {
        Set<String> modules = new HashSet<>();
        for (int i = 0; i < mCbGroup.getChildCount(); i++) {
            AppCompatCheckBox cb = (AppCompatCheckBox) mCbGroup.getChildAt(i);
            if (cb.isChecked()) {
                modules.add(String.valueOf(cb.getText()));
            }
        }
        return modules;
    }

    private void toggleAllModule(boolean check) {
        for (int i = 0; i < mCbGroup.getChildCount(); i++) {
            AppCompatCheckBox cb = (AppCompatCheckBox) mCbGroup.getChildAt(i);
            cb.setChecked(check);
        }
    }

    public void onInstallModuleChanged() {
        mCbGroup.removeAllViews();
        Set<String> modules = GodEye.instance().getInstalledModuleNames();
        for (@GodEye.ModuleName String module : modules) {
            AppCompatCheckBox appCompatCheckBox = new AppCompatCheckBox(Objects.requireNonNull(ConsumeFragment.this.getActivity()));
            appCompatCheckBox.setText(module);
            mCbGroup.addView(appCompatCheckBox);
        }
    }
}
