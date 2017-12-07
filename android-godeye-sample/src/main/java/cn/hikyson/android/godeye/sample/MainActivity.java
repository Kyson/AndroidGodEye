package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import cn.hikyson.android.godeye.toolbox.StartupTracer;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.godeye.monitor.modules.AppInfoModule;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends Activity implements Loggable {
    private static final String TAG = "AndroidGodEye";
    private TextView mLogTv;
    private ScrollView mLogScrollView;

    private static class AppInfoProxyImpl implements AppInfoModule.AppInfo.AppInfoProxy {
        private Context mContext;

        public AppInfoProxyImpl(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public AppInfoModule.AppInfo getAppInfo() {
            Map<String, Object> map = new ArrayMap<>();
            map.put("versionName", BuildConfig.VERSION_NAME);
            map.put("versionCode", BuildConfig.VERSION_CODE);
            map.put("buildType", BuildConfig.BUILD_TYPE);
            map.put("debuggable", BuildConfig.DEBUG);
            map.put("channel", "Channel_XX");
            map.put("clientid", "ClientId");
            map.put("deviceid", "DeviceId");
            map.put("uid", "0x0001");
            map.put("email", "kysonchao@gmail.com");
            return new AppInfoModule.AppInfo(mContext, map);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartupTracer.get().onHomeCreate();
        mLogTv = findViewById(R.id.activity_main_log_tv);
        mLogScrollView = findViewById(R.id.activity_main_log_sc);
        GodEye.instance().installAll(getApplication());
        GodEyeMonitor.injectAppInfoProxy(new AppInfoProxyImpl(this));
        GodEyeMonitor.work(MainActivity.this);
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                log("DEBUG: " + msg);
            }

            @Override
            public void e(String msg) {
                log("!ERROR: " + msg);
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                log("!!EXCEPTION: " + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void log(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogTv.append(msg + "\n");
                mLogScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLogScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 300);
            }
        });
    }

    public void installAll(View view) {
        GodEye.instance().installAll(getApplication());
    }

    public void uninstallAll(View view) {
        GodEye.instance().uninstallAll();
    }

    public void uninstallCpu(View view) {
        GodEye.instance().cpu().uninstall();
    }

    public void uninstallBattery(View view) {
        GodEye.instance().battery().uninstall();
    }

    public void uninstallSm(View view) {
        GodEye.instance().sm().uninstall();
    }

    public void uninstallLeak(View view) {
        GodEye.instance().leakDetector().uninstall();
    }

    public void uninstallFps(View view) {
        GodEye.instance().fps().uninstall();
    }

    public void uninstallTraffic(View view) {
        GodEye.instance().traffic().uninstall();
    }

    public void testTmp(View view) {
        final String url = String.valueOf(((EditText) this.findViewById(R.id.activity_main_test_temp_et)).getText());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    public static class GodEyeDisposableObserver<T> extends DisposableObserver<T> {
        private String mName;
        private Loggable mLoggable;

        public GodEyeDisposableObserver(String name, Loggable loggable) {
            this.mName = name;
            this.mLoggable = loggable;
        }

        @Override
        public void onNext(T t) {
            mLoggable.log("DEBUG: " + mName + " , " + String.valueOf(t));
        }

        @Override
        public void onError(Throwable e) {
            mLoggable.log("!ERROR: " + mName + " , " + String.valueOf(e));
        }

        @Override
        public void onComplete() {
            mLoggable.log("DEBUG: " + mName + " , onComplete.");
        }
    }

    public void testCpu(View view) {
        GodEye.instance().cpu().consume().subscribe(new GodEyeDisposableObserver<CpuInfo>("cpu", this));
    }

    public void testBattery(View view) {
        GodEye.instance().battery().consume().subscribe(new GodEyeDisposableObserver<BatteryInfo>("battery", this));
    }

    public void testSM(View view) {
        GodEye.instance().sm().consume().subscribe(new GodEyeDisposableObserver<BlockInfo>("sm", this));
    }

    public void block(View view) {
        EditText editText = findViewById(R.id.activity_main_block_et);
        final int blockTime = Integer.parseInt(String.valueOf(editText.getText()));
//        final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < blockTime; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep((long) (Math.random() * 3000 + 500));
                            } catch (Throwable e) {
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

    public void testLeak(View view) {
        GodEye.instance().leakDetector().consume().subscribe(new GodEyeDisposableObserver<LeakQueue.LeakMemoryInfo>("leak", this));
    }

    public void testNetwork(View view) {
        GodEye.instance().network().consume().subscribe(new GodEyeDisposableObserver<RequestBaseInfo>("network", this));
    }

    public void jumpToLeak(View view) {
        Intent intent = new Intent(MainActivity.this, LeakActivity.class);
        startActivity(intent);
    }

    public void testFps(View view) {
        GodEye.instance().fps().consume().subscribe(new GodEyeDisposableObserver<FpsInfo>("fps", this));
    }

    public void testTraffic(View view) {
        GodEye.instance().traffic().consume().subscribe(new GodEyeDisposableObserver<TrafficInfo>("traffic", this));
    }

    public void testRequest(View view) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    request();
                }
            });
        }
    }

    private void request() {
        try {
            long startTimeMillis = System.currentTimeMillis();
            URL url = new URL("https://www.trip.com/");
            //打开连接
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                String result = baos.toString("utf-8");
                long endTimeMillis = System.currentTimeMillis();
                GodEye.instance().network().produce(new RequestBaseInfo(startTimeMillis, endTimeMillis, result.getBytes().length, String.valueOf(url)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
