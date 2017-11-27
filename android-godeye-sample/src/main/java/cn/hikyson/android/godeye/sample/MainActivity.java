package cn.hikyson.android.godeye.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import cn.hikyson.android.godeye.R;
import cn.hikyson.godeye.GodEye;
import cn.hikyson.godeye.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.godeye.utils.L;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends Activity implements Loggable {
    private static final String TAG = "AndroidGodEye";
    private GodEye mGodEye;
    private TextView mLogTv;
    private ScrollView mLogScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogTv = findViewById(R.id.activity_main_log_tv);
        mLogScrollView = findViewById(R.id.activity_main_log_sc);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        mGodEye = GodEye.instance();
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
        installAll(null);
        GodEyeMonitor.work(MainActivity.this);
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
                }, 500);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            Toast.makeText(this, "grant " + Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void installAll(View view) {
        mGodEye.installAll(getApplication());
    }

    public void uninstallAll(View view) {
        mGodEye.uninstallAll();
    }

    public void uninstallCpu(View view) {
        mGodEye.cpu().uninstall();
    }

    public void uninstallBattery(View view) {
        mGodEye.battery().uninstall();
    }

    public void uninstallSm(View view) {
        mGodEye.sm().uninstall();
    }

    public void uninstallLeak(View view) {
        mGodEye.leakDetector().uninstall();
    }

    public void uninstallFps(View view) {
        mGodEye.fps().uninstall();
    }

    public void uninstallTraffic(View view) {
        mGodEye.traffic().uninstall();
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
        mGodEye.cpu().consume().subscribe(new GodEyeDisposableObserver<CpuInfo>("cpu", this));
    }

    public void testBattery(View view) {
        mGodEye.battery().consume().subscribe(new GodEyeDisposableObserver<BatteryInfo>("battery", this));
    }

    public void testSM(View view) {
        mGodEye.sm().consume().subscribe(new GodEyeDisposableObserver<BlockInfo>("sm", this));
    }

    public void block(View view) {
        try {
            EditText editText = findViewById(R.id.activity_main_block_et);
            final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
            Thread.sleep(blockTime);
        } catch (Throwable e) {
        }
    }

    public void testLeak(View view) {
        mGodEye.leakDetector().consume().subscribe(new GodEyeDisposableObserver<LeakQueue.LeakMemoryInfo>("leak", this));
    }

    public void testNetwork(View view) {
        mGodEye.network().consume().subscribe(new GodEyeDisposableObserver<RequestBaseInfo>("network", this));
    }

    public void jumpToLeak(View view) {
        Intent intent = new Intent(MainActivity.this, LeakActivity.class);
        startActivity(intent);
    }

    public void testFps(View view) {
        mGodEye.fps().consume().subscribe(new GodEyeDisposableObserver<FpsInfo>("fps", this));
    }

    public void testTraffic(View view) {
        mGodEye.traffic().consume().subscribe(new GodEyeDisposableObserver<TrafficInfo>("traffic", this));
    }

    public void testRequest(View view) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
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
                baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
