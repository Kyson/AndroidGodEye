package cn.hikyson.android.godeye.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import io.reactivex.disposables.CompositeDisposable;


public class ConsumeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConsumeFragment() {
        // Required empty public constructor
    }

    public static ConsumeFragment newInstance(String param1, String param2) {
        ConsumeFragment fragment = new ConsumeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        ((TextView) view.findViewById(R.id.fragment_consume_observe)).setText("Log Consumers:");
        view.findViewById(R.id.fragment_consume_start_log_consumer).setOnClickListener(v -> {
            mCompositeDisposable.dispose();
            Set<String> modules = getModulesSelected();
            StringBuilder sb = new StringBuilder("Log Consumers:\n");
            for (@GodEye.ModuleName String module : modules) {
                try {
                    mCompositeDisposable.add(GodEye.instance().observeModule(module, new LogObserver<>(module, L::d)));
                    sb.append(module).append(", ");
                } catch (UninstallException e) {
                    L.e(String.valueOf(e));
                }
            }
            ((TextView) view.findViewById(R.id.fragment_consume_observe)).setText(String.valueOf(sb));
        });
        view.findViewById(R.id.fragment_consume_stop_log_consumer).setOnClickListener(v -> {
            mCompositeDisposable.dispose();
            ((TextView) view.findViewById(R.id.fragment_consume_observe)).setText("Log Consumers:");
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCbGroup.removeAllViews();
        Set<String> modules = GodEye.instance().getInstalledModuleNames();
        for (@GodEye.ModuleName String module : modules) {
            AppCompatCheckBox appCompatCheckBox = new AppCompatCheckBox(Objects.requireNonNull(ConsumeFragment.this.getActivity()));
            appCompatCheckBox.setText(module);
            mCbGroup.addView(appCompatCheckBox);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
