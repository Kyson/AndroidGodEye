package cn.hikyson.android.godeye.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;

import cn.hikyson.android.godeye.toolbox.network.GodEyePluginOkNetwork;
import cn.hikyson.godeye.core.utils.L;
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
        view.findViewById(R.id.fragment_tools_crash_bt).setOnClickListener(v -> {
            throw new IllegalStateException("This is a crash made by AndroidGodEye.");
        });
        view.findViewById(R.id.fragment_tools_pageload_bt).setOnClickListener(v -> {
            Intent intent = new Intent(ToolsFragment.this.getActivity(), SecondActivity.class);
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
        for (int i = 0; i < 100000; i++) {
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
