package cn.hikyson.godeye.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import cn.hikyson.android.godeye.sample.R;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class InstallFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnInstallModuleChangeListener mListener;

    public InstallFragment() {
        // Required empty public constructor
    }

    public static InstallFragment newInstance(String param1, String param2) {
        InstallFragment fragment = new InstallFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install, container, false);
        view.findViewById(R.id.fragment_install_default).setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GodEye.instance().install(GodEyeConfig.defaultConfig());
                    mListener.onInstallModuleChanged();
                }
            }).start();
        });
        view.findViewById(R.id.fragment_install_local_stream).setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GodEye.instance().install(GodEyeConfig.fromAssets("android-godeye-config/install.config"));
                    mListener.onInstallModuleChanged();
                }
            }).start();
        });
        view.findViewById(R.id.fragment_install_remote_stream).setOnClickListener(v -> {
            ((TextView) v).setText("Loading...");
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url("https://raw.githubusercontent.com/Kyson/AndroidGodEye/master/android-godeye-sample/src/main/assets/android-godeye-config/install.config")
                    .get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e("Fail to load config content to install:" + e);
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        ((TextView) v).setText("Install(InputStream Config)");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String contentStr = response.body().string();
                    L.d("--------------Config START--------------");
                    L.d(contentStr);
                    L.d("--------------Config END--------------");
                    GodEye.instance().install(GodEyeConfig.fromInputStream(new ByteArrayInputStream(contentStr.getBytes(Charset.forName("utf-8")))));
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        ((TextView) v).setText("Install(InputStream Config)");
                        mListener.onInstallModuleChanged();
                    });
                }
            });
        });
        view.findViewById(R.id.fragment_install_uninstall).setOnClickListener(v -> {
            GodEye.instance().uninstall();
            mListener.onInstallModuleChanged();
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInstallModuleChangeListener) {
            mListener = (OnInstallModuleChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInstallModuleChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInstallModuleChangeListener {
        void onInstallModuleChanged();
    }
}
