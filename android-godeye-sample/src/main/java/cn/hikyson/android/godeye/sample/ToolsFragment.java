package cn.hikyson.android.godeye.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import cn.hikyson.android.godeye.okhttp.GodEyePluginOkNetwork;
import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ReflectUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ToolsFragment extends Fragment {

    public ToolsFragment() {
    }

    private OkHttpClient mZygote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GodEyePluginOkNetwork godEyePluginOkNetwork = new GodEyePluginOkNetwork();
        mZygote = new OkHttpClient.Builder().eventListenerFactory(godEyePluginOkNetwork).addNetworkInterceptor(godEyePluginOkNetwork).build();
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        view.findViewById(R.id.fragment_tools_block_bt).setOnClickListener(v -> {
            block();
        });
        view.findViewById(R.id.fragment_tools_request_bt).setOnClickListener(v -> {
            request();
        });
        view.findViewById(R.id.fragment_tools_leak_activity_bt).setOnClickListener(v -> {
            leakActivity();
        });
        view.findViewById(R.id.fragment_tools_leak_fragment_bt).setOnClickListener(v -> {
            leakFragment();
        });
        view.findViewById(R.id.fragment_tools_call_functions_bt).setOnClickListener(v -> {
            makeInvocations();
        });
        view.findViewById(R.id.fragment_tools_java_crash_bt).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("AndroidGodEye", Context.MODE_PRIVATE);
            int index = sharedPreferences.getInt("CrashIndex", 0);
            sharedPreferences.edit().putInt("CrashIndex", index + 1).commit();
            throw new IllegalStateException("This is a crash made by AndroidGodEye " + index + ".");
        });
        view.findViewById(R.id.fragment_tools_native_crash_bt).setOnClickListener(v -> {
            ReflectUtil.invokeStaticMethod("xcrash.XCrash", "testNativeCrash",
                    new Class<?>[]{boolean.class}, new Object[]{false});
        });
        view.findViewById(R.id.fragment_tools_pageload_bt).setOnClickListener(v -> {
            Intent intent = new Intent(ToolsFragment.this.getActivity(), SecondActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.fragment_tools_startup_bt).setOnClickListener(v -> {
            try {
                GodEyeHelper.onAppStartEnd(new StartupInfo(StartupInfo.StartUpType.COLD, new Random().nextInt(1000) + 1000));
            } catch (UninstallException e) {
                e.printStackTrace();
            }
        });
        view.findViewById(R.id.fragment_tools_image_bt).setOnClickListener(v -> {
            Intent intent = new Intent(ToolsFragment.this.getActivity(), ImageActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void block() {
        EditText editText = getView().findViewById(R.id.fragment_tools_block_et);
        try {
            final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                try {
                    Thread.sleep(blockTime);
                } catch (Throwable e) {
                }
            });
        } catch (Throwable e) {
            L.e("ToolsFragment Input valid time for block(jank)!");
        }
    }

    private void request() {
        new Thread(() -> {
            try {
                OkHttpClient client = mZygote;
                Request request = new Request.Builder()
                        .url("https://tech.hikyson.cn/")
                        .build();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                L.d("ToolsFragment request result:" + response.code());
            } catch (IOException e) {
                e.printStackTrace();
                L.d("ToolsFragment request result:" + e);
            }
        }).start();
    }

    private void leakActivity() {
        Intent intent = new Intent(ToolsFragment.this.getActivity(), LeakActivity.class);
        startActivity(intent);
    }

    private void leakFragment() {
        Intent intent = new Intent(ToolsFragment.this.getActivity(), LeakActivityV4.class);
        startActivity(intent);
    }


    private void makeInvocations() {
        L.d("ToolsFragment makeInvocations start...");
        MethodCanaryTest methodCanaryTest = new MethodCanaryTest();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            L.d("ToolsFragment makeInvocations thread[" + Thread.currentThread().getName() + "] end.");
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    methodCanaryTest.methodA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            L.d("ToolsFragment makeInvocations thread[" + Thread.currentThread().getName() + "] end.");
        }).start();
        for (int i = 0; i < 1000; i++) {
            methodCanaryTest.methodC();
            methodCanaryTest.methodD();
        }
        L.d("ToolsFragment makeInvocations main thread end.");
    }

    public static class MethodCanaryTest {
        private void methodA() throws InterruptedException {
            methodB();
            methodB();
            methodB();
            methodC();
            methodD();
            int j = 0;
            String m = "m" + j;
        }

        private void methodB() throws InterruptedException {
            Thread.sleep(200);
            int i = 0;
        }

        private int[] methodC() {
            int[] i = new int[1000];
            for (int m = 0; m < i.length; m++) {
                i[m] = m;
            }
            return i;
        }

        private void methodD() {
            String[] i = new String[1000];
            for (int m = 0; m < i.length; m++) {
                i[m] = m + "this is a string";
            }
        }

        private void methodE() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
