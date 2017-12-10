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
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

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
    @BindView(R.id.activity_main_consumer_cancel_watch)
    Button mActivityMainCancelWatch;

    CheckBox[] installableCbs;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);
        mCompositeDisposable = new CompositeDisposable();
        installableCbs = new CheckBox[9];
        installableCbs[0] = mActivityMainCpu;
        installableCbs[1] = mActivityMainBattery;
        installableCbs[2] = mActivityMainFps;
        installableCbs[3] = mActivityMainLeak;
        installableCbs[4] = mActivityMainHeap;
        installableCbs[5] = mActivityMainPss;
        installableCbs[6] = mActivityMainRam;
        installableCbs[7] = mActivityMainSm;
        installableCbs[8] = mActivityMainTraffic;
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
        GodEyeMonitor.injectAppInfoConext(new AppInfoProxyImpl(this));
    }

    @OnClick({R.id.activity_main_all, R.id.activity_main_cancel_all, R.id.activity_main_install, R.id.activity_main_uninstall, R.id.activity_main_monitor_work, R.id.activity_main_monitor_shutdown, R.id.activity_main_consumer_cpu, R.id.activity_main_consumer_battery, R.id.activity_main_consumer_fps, R.id.activity_main_consumer_leak, R.id.activity_main_consumer_heap, R.id.activity_main_consumer_pss, R.id.activity_main_consumer_ram, R.id.activity_main_consumer_network, R.id.activity_main_consumer_sm, R.id.activity_main_consumer_startup, R.id.activity_main_consumer_traffic, R.id.activity_main_make_block, R.id.activity_main_make_request, R.id.activity_main_make_leak, R.id.activity_main_consumer_cancel_watch})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_all:
                checkAllInstall();
                break;
            case R.id.activity_main_cancel_all:
                cancelCheckAllInstall();
                break;
            case R.id.activity_main_install:
                onClickInstall();
                break;
            case R.id.activity_main_uninstall:
                onClickUninstall();
                break;
            case R.id.activity_main_monitor_work:
                GodEyeMonitor.work(MainActivity.this);
                break;
            case R.id.activity_main_monitor_shutdown:
                GodEyeMonitor.shutDown();
                break;
            case R.id.activity_main_consumer_cpu:
                mCompositeDisposable.add(GodEye.instance().cpu().consume().subscribe(new LogObserver<>("cpu", this)));
                break;
            case R.id.activity_main_consumer_battery:
                mCompositeDisposable.add(GodEye.instance().battery().consume().subscribe(new LogObserver<BatteryInfo>("battery", this)));
                break;
            case R.id.activity_main_consumer_fps:
                mCompositeDisposable.add(GodEye.instance().fps().consume().subscribe(new LogObserver<FpsInfo>("fps", this)));
                break;
            case R.id.activity_main_consumer_leak:
                mCompositeDisposable.add(GodEye.instance().leakDetector().consume().subscribe(new LogObserver<LeakQueue.LeakMemoryInfo>("leak", this)));
                break;
            case R.id.activity_main_consumer_heap:
                mCompositeDisposable.add(GodEye.instance().heap().consume().subscribe(new LogObserver<HeapInfo>("heap", this)));
                break;
            case R.id.activity_main_consumer_pss:
                mCompositeDisposable.add(GodEye.instance().pss().consume().subscribe(new LogObserver<PssInfo>("pss", this)));
                break;
            case R.id.activity_main_consumer_ram:
                mCompositeDisposable.add(GodEye.instance().ram().consume().subscribe(new LogObserver<RamInfo>("ram", this)));
                break;
            case R.id.activity_main_consumer_network:
                mCompositeDisposable.add(GodEye.instance().network().consume().subscribe(new LogObserver<RequestBaseInfo>("network", this)));
                break;
            case R.id.activity_main_consumer_sm:
                mCompositeDisposable.add(GodEye.instance().sm().consume().subscribe(new LogObserver<BlockInfo>("sm", this)));
                break;
            case R.id.activity_main_consumer_startup:
                mCompositeDisposable.add(GodEye.instance().startup().consume().subscribe(new LogObserver<StartupInfo>("startup", this)));
                break;
            case R.id.activity_main_consumer_traffic:
                mCompositeDisposable.add(GodEye.instance().traffic().consume().subscribe(new LogObserver<TrafficInfo>("traffic", this)));
                break;
            case R.id.activity_main_make_block:
                block();
                break;
            case R.id.activity_main_make_request:
                request();
                break;
            case R.id.activity_main_make_leak:
                jumpToLeak();
                break;
            case R.id.activity_main_consumer_cancel_watch:
                mCompositeDisposable.clear();
                break;
            default:
                break;
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

    private void block() {
        EditText editText = findViewById(R.id.activity_main_block_et);
        final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(blockTime);
                } catch (Throwable e) {
                }
            }
        });
    }


    private void jumpToLeak() {
        Intent intent = new Intent(MainActivity.this, LeakActivity.class);
        startActivity(intent);
    }

    private void checkAllInstall() {
        for (CheckBox cb : installableCbs) {
            cb.setChecked(true);
        }
    }

    private void cancelCheckAllInstall() {
        for (CheckBox cb : installableCbs) {
            cb.setChecked(false);
        }
    }

    private void onClickInstall() {
        if (mActivityMainCpu.isChecked()) {
            GodEye.instance().cpu().install();
        }
        if (mActivityMainBattery.isChecked()) {
            GodEye.instance().battery().install(this);
        }
        if (mActivityMainFps.isChecked()) {
            GodEye.instance().fps().install(this);
        }
        if (mActivityMainLeak.isChecked()) {
            GodEye.instance().leakDetector().install(getApplication());
        }
        if (mActivityMainHeap.isChecked()) {
            GodEye.instance().heap().install();
        }
        if (mActivityMainPss.isChecked()) {
            GodEye.instance().pss().install(this);
        }
        if (mActivityMainRam.isChecked()) {
            GodEye.instance().ram().install(this);
        }
        if (mActivityMainSm.isChecked()) {
            GodEye.instance().sm().install(this);
        }
        if (mActivityMainTraffic.isChecked()) {
            GodEye.instance().traffic().install();
        }
    }

    private void onClickUninstall() {
        if (mActivityMainCpu.isChecked()) {
            GodEye.instance().cpu().uninstall();
        }
        if (mActivityMainBattery.isChecked()) {
            GodEye.instance().battery().uninstall();
        }
        if (mActivityMainFps.isChecked()) {
            GodEye.instance().fps().uninstall();
        }
        if (mActivityMainLeak.isChecked()) {
            GodEye.instance().leakDetector().uninstall();
        }
        if (mActivityMainHeap.isChecked()) {
            GodEye.instance().heap().uninstall();
        }
        if (mActivityMainPss.isChecked()) {
            GodEye.instance().pss().uninstall();
        }
        if (mActivityMainRam.isChecked()) {
            GodEye.instance().ram().uninstall();
        }
        if (mActivityMainSm.isChecked()) {
            GodEye.instance().sm().uninstall();
        }
        if (mActivityMainTraffic.isChecked()) {
            GodEye.instance().traffic().uninstall();
        }
    }

    @Override
    public void log(String msg) {
        mActivityMainLogview.log(msg);
    }

}
