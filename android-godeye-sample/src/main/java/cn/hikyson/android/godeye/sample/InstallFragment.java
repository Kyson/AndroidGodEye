package cn.hikyson.android.godeye.sample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

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

    private OnFragmentInteractionListener mListener;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install, container, false);
        TextView contentTv = view.findViewById(R.id.fragment_install_stream_content);

        view.findViewById(R.id.fragment_install_default).setOnClickListener(v -> {
            GodEye.instance().install(GodEyeConfig.defaultConfig());
        });
        view.findViewById(R.id.fragment_install_stream).setOnClickListener(v -> {
            contentTv.setText("Loading...");
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url("https://raw.githubusercontent.com/Kyson/AndroidGodEye/feature-refactor/android-godeye-sample/src/main/assets/android-godeye-config/install.config")
                    .get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e(String.valueOf(e));
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        contentTv.setText("Fail to load config content to install!");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String contentStr = response.body().string();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        contentTv.setText(contentStr);
                    });
                    GodEye.instance().install(GodEyeConfig.fromInputStream(new ByteArrayInputStream(contentStr.getBytes(Charset.forName("utf-8")))));
                }
            });
        });
        view.findViewById(R.id.fragment_install_uninstall).setOnClickListener(v -> {
            GodEye.instance().uninstall();
        });
        return view;
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
