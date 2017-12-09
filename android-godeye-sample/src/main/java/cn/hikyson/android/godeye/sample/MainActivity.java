package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class MainActivity extends Activity implements Loggable {
    private static final String TAG = "AndroidGodEye";
    @BindView(R.id.activity_main_logview)
    LogView mActivityMainLogview;
    @BindView(R.id.activity_main_cpu)
    CheckBox mActivityMainCpu;
    @BindView(R.id.activity_main_battery)
    CheckBox mActivityMainBattery;
    @BindView(R.id.activity_main_fps)
    CheckBox mActivityMainFps;
    @BindView(R.id.activity_main_leak)
    CheckBox mActivityMainLeak;
    @BindView(R.id.activity_main_heap)
    CheckBox mActivityMainHeap;
    @BindView(R.id.activity_main_pss)
    CheckBox mActivityMainPss;
    @BindView(R.id.activity_main_ram)
    CheckBox mActivityMainRam;
    @BindView(R.id.activity_main_sm)
    CheckBox mActivityMainSm;
    @BindView(R.id.activity_main_traffic)
    CheckBox mActivityMainTraffic;
    @BindView(R.id.activity_main_all)
    Button mActivityMainAll;
    @BindView(R.id.activity_main_cancel_all)
    Button mActivityMainCancelAll;
    @BindView(R.id.activity_main_install)
    Button mActivityMainInstall;
    @BindView(R.id.activity_main_uninstall)
    Button mActivityMainUninstall;
    @BindView(R.id.activity_main_monitor_work)
    Button mActivityMainMonitorWork;
    @BindView(R.id.activity_main_monitor_shutdown)
    Button mActivityMainMonitorShutdown;
    @BindView(R.id.activity_main_consumer_cpu)
    Button mActivityMainConsumerCpu;
    @BindView(R.id.activity_main_consumer_battery)
    Button mActivityMainConsumerBattery;
    @BindView(R.id.activity_main_consumer_fps)
    Button mActivityMainConsumerFps;
    @BindView(R.id.activity_main_consumer_leak)
    Button mActivityMainConsumerLeak;
    @BindView(R.id.activity_main_consumer_heap)
    Button mActivityMainConsumerHeap;
    @BindView(R.id.activity_main_consumer_pss)
    Button mActivityMainConsumerPss;
    @BindView(R.id.activity_main_consumer_ram)
    Button mActivityMainConsumerRam;
    @BindView(R.id.activity_main_consumer_network)
    Button mActivityMainConsumerNetwork;
    @BindView(R.id.activity_main_consumer_sm)
    Button mActivityMainConsumerSm;
    @BindView(R.id.activity_main_consumer_startup)
    Button mActivityMainConsumerStartup;
    @BindView(R.id.activity_main_consumer_traffic)
    Button mActivityMainConsumerTraffic;
    @BindView(R.id.activity_main_block_et)
    EditText mActivityMainBlockEt;
    @BindView(R.id.activity_main_make_block)
    Button mActivityMainMakeBlock;
    @BindView(R.id.activity_main_make_request)
    Button mActivityMainMakeRequest;
    @BindView(R.id.activity_main_make_leak)
    Button mActivityMainMakeLeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);
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
        StartupTracer.get().onHomeCreate();

        GodEyeMonitor.work(this);
        GodEyeMonitor.injectAppInfoConext(new AppInfoProxyImpl(this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GodEye.instance().uninstallAll();
        GodEyeMonitor.shutDown();
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

    @OnClick({R.id.activity_main_all, R.id.activity_main_cancel_all, R.id.activity_main_install, R.id.activity_main_uninstall, R.id.activity_main_monitor_work, R.id.activity_main_monitor_shutdown, R.id.activity_main_consumer_cpu, R.id.activity_main_consumer_battery, R.id.activity_main_consumer_fps, R.id.activity_main_consumer_leak, R.id.activity_main_consumer_heap, R.id.activity_main_consumer_pss, R.id.activity_main_consumer_ram, R.id.activity_main_consumer_network, R.id.activity_main_consumer_sm, R.id.activity_main_consumer_startup, R.id.activity_main_consumer_traffic, R.id.activity_main_block_et, R.id.activity_main_make_block, R.id.activity_main_make_request, R.id.activity_main_make_leak})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_all:
                if(mActivityMainCpu.isChecked()){
                    GodEye.instance().cpu().install();
                }
                if(mActivityMainBattery.isChecked()){
                    GodEye.instance().battery().install(this);
                }
                if(mActivityMainFps.isChecked()){
                    GodEye.instance().fps().install(this);
                }
                if(mActivityMainLeak.isChecked()){
                    GodEye.instance().leakDetector().install(getApplication());
                }

                    <CheckBox
                android:id="@+id/activity_main_leak"


                    <CheckBox
                android:id="@+id/activity_main_heap"


                    <CheckBox
                android:id="@+id/activity_main_pss"


                    <CheckBox
                android:id="@+id/activity_main_ram"


                    <CheckBox
                android:id="@+id/activity_main_sm"


                    <CheckBox
                android:id="@+id/activity_main_traffic"




                GodEye.instance().installAll(getApplication());
                break;
            case R.id.activity_main_cancel_all:
                break;
            case R.id.activity_main_install:
                break;
            case R.id.activity_main_uninstall:
                break;
            case R.id.activity_main_monitor_work:
                break;
            case R.id.activity_main_monitor_shutdown:
                break;
            case R.id.activity_main_consumer_cpu:
                break;
            case R.id.activity_main_consumer_battery:
                break;
            case R.id.activity_main_consumer_fps:
                break;
            case R.id.activity_main_consumer_leak:
                break;
            case R.id.activity_main_consumer_heap:
                break;
            case R.id.activity_main_consumer_pss:
                break;
            case R.id.activity_main_consumer_ram:
                break;
            case R.id.activity_main_consumer_network:
                break;
            case R.id.activity_main_consumer_sm:
                break;
            case R.id.activity_main_consumer_startup:
                break;
            case R.id.activity_main_consumer_traffic:
                break;
            case R.id.activity_main_block_et:
                break;
            case R.id.activity_main_make_block:
                break;
            case R.id.activity_main_make_request:
                break;
            case R.id.activity_main_make_leak:
                break;
            default:
                break;
        }
    }


    @Override
    public void log(String msg) {
        mActivityMainLogview.log(msg);
    }

}
