package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hikyson.android.godeye.toolbox.network.OkNetworkCollectorFactory;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.internal.modules.fps.Fps;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.internal.modules.memory.Heap;
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.Pss;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.Ram;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadDump;
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends Activity implements Loggable {
    private static final String TAG = "AndroidGodEye";
    @BindView(R.id.activity_main_logview)
    LogView mActivityMainLogview;
    @BindView(R.id.activity_main_methodcanary)
    CheckBox mActivityMainMethodCanary;
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
    @BindView(R.id.activity_main_crash)
    CheckBox mActivityMainCrash;
    @BindView(R.id.activity_main_thread)
    CheckBox mActivityMainThread;
    @BindView(R.id.activity_main_deadlock)
    CheckBox mActivityMainDeadLock;
    @BindView(R.id.activity_main_pageload)
    CheckBox mActivityMainPageload;
    @BindView(R.id.activity_main_all)
    Button mActivityMainAll;
    @BindView(R.id.activity_main_cancel_all)
    Button mActivityMainCancelAll;
    @BindView(R.id.activity_main_install)
    Button mActivityMainInstall;
    @BindView(R.id.activity_main_install_with_assets)
    Button mActivityMainInstallWithAssets;
    @BindView(R.id.activity_main_uninstall)
    Button mActivityMainUninstall;
    @BindView(R.id.activity_main_monitor_work)
    Button mActivityMainMonitorWork;
    @BindView(R.id.activity_main_monitor_shutdown)
    Button mActivityMainMonitorShutdown;
    @BindView(R.id.activity_main_consumer_methodcanary)
    Button mActivityMainConsumerMethodCanary;
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
    @BindView(R.id.activity_main_consumer_crash)
    Button mActivityMainConsumerCrash;
    @BindView(R.id.activity_main_block_et)
    EditText mActivityMainBlockEt;
    @BindView(R.id.activity_main_make_block)
    Button mActivityMainMakeBlock;
    @BindView(R.id.activity_main_make_request)
    Button mActivityMainMakeRequest;
    @BindView(R.id.activity_main_make_invocations)
    Button mActivityMainMakeInvocations;
    @BindView(R.id.activity_main_make_leak)
    Button mActivityMainMakeLeak;
    @BindView(R.id.activity_main_make_leak_v4)
    Button mActivityMainMakeLeakV4;
    @BindView(R.id.activity_main_consumer_cancel_watch)
    Button mActivityMainCancelWatch;
    @BindView(R.id.activity_main_make_follow)
    CheckBox mActivityMainFollow;
    @BindView(R.id.activity_main_make_clear)
    Button mActivityMainClear;
    @BindView(R.id.activity_main_test)
    Button mActivityMainTest;

    CheckBox[] installableCbs;
    private CompositeDisposable mCompositeDisposable;

    private OkHttpClient mZygote = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GodEye.instance().install(new GodEyeConfig());
        try {
            OkNetworkCollectorFactory okNetworkCollectorFactory = new OkNetworkCollectorFactory(GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK));
            mZygote = new OkHttpClient.Builder().eventListenerFactory(okNetworkCollectorFactory).addNetworkInterceptor(okNetworkCollectorFactory).build();
        } catch (UninstallException e) {
            mZygote = new OkHttpClient.Builder().build();
        }
        ButterKnife.bind(this, this);
        mCompositeDisposable = new CompositeDisposable();
        installableCbs = new CheckBox[14];
        installableCbs[0] = mActivityMainMethodCanary;
        installableCbs[1] = mActivityMainCpu;
        installableCbs[2] = mActivityMainBattery;
        installableCbs[3] = mActivityMainFps;
        installableCbs[4] = mActivityMainLeak;
        installableCbs[5] = mActivityMainHeap;
        installableCbs[6] = mActivityMainPss;
        installableCbs[7] = mActivityMainRam;
        installableCbs[8] = mActivityMainSm;
        installableCbs[9] = mActivityMainTraffic;
        installableCbs[10] = mActivityMainCrash;
        installableCbs[11] = mActivityMainThread;
        installableCbs[12] = mActivityMainDeadLock;
        installableCbs[13] = mActivityMainPageload;
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
        mActivityMainFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mActivityMainLogview.follow(isChecked);
            }
        });
        StartupTracer.get().onHomeCreate(this);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 100);
    }

    @OnClick({R.id.activity_main_all, R.id.activity_main_cancel_all, R.id.activity_main_install, R.id.activity_main_install_with_assets,
            R.id.activity_main_uninstall, R.id.activity_main_monitor_work, R.id.activity_main_monitor_shutdown,
            R.id.activity_main_consumer_methodcanary, R.id.activity_main_consumer_cpu, R.id.activity_main_consumer_battery, R.id.activity_main_consumer_fps,
            R.id.activity_main_consumer_leak, R.id.activity_main_consumer_heap, R.id.activity_main_consumer_pss,
            R.id.activity_main_consumer_ram, R.id.activity_main_consumer_network, R.id.activity_main_consumer_sm,
            R.id.activity_main_consumer_startup, R.id.activity_main_consumer_traffic, R.id.activity_main_consumer_crash,
            R.id.activity_main_consumer_thread, R.id.activity_main_consumer_deadlock, R.id.activity_main_consumer_pageload,
            R.id.activity_main_make_block, R.id.activity_main_make_request, R.id.activity_main_make_leak, R.id.activity_main_make_leak_v4, R.id.activity_main_make_invocations,
            R.id.activity_main_make_crash, R.id.activity_main_consumer_cancel_watch, R.id.activity_main_make_clear,
            R.id.activity_main_make_deadlock, R.id.activity_main_make_pageload, R.id.activity_main_test})
    public void onClick(View v) throws UninstallException {
        switch (v.getId()) {
            case R.id.activity_main_all:
                checkAllInstall();
                break;
            case R.id.activity_main_test:
                break;
            case R.id.activity_main_cancel_all:
                cancelCheckAllInstall();
                break;
            case R.id.activity_main_install:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onClickInstall();
                    }
                }).start();
                break;
            case R.id.activity_main_install_with_assets:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GodEye.instance().install(GodEyeConfig.fromAssets("android-godeye-config/install.config"));
                    }
                }).start();
                break;
            case R.id.activity_main_uninstall:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GodEye.instance().uninstall();
                    }
                }).start();
                break;
            case R.id.activity_main_monitor_work:
                GodEyeMonitor.work(MainActivity.this, MainActivity.this.getResources().getInteger(R.integer.CN_HIKYSON_ANDROID_GODEYE_MONITOR_PORT));
                break;
            case R.id.activity_main_monitor_shutdown:
                GodEyeMonitor.shutDown();
                break;
            case R.id.activity_main_consumer_methodcanary:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.METHOD_CANARY,new LogObserver<>(GodEye.ModuleName.METHOD_CANARY, this)));
                break;
            case R.id.activity_main_consumer_cpu:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.CPU,new LogObserver<>(GodEye.ModuleName.CPU, this)));
                break;
            case R.id.activity_main_consumer_battery:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.BATTERY,new LogObserver<>(GodEye.ModuleName.BATTERY, this)));
                break;
            case R.id.activity_main_consumer_fps:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.FPS,new LogObserver<>(GodEye.ModuleName.FPS, this)));
                break;
            case R.id.activity_main_consumer_leak:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.LEAK,new LogObserver<>(GodEye.ModuleName.LEAK, this)));
                break;
            case R.id.activity_main_consumer_heap:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.HEAP,new LogObserver<>(GodEye.ModuleName.HEAP, this)));
                break;
            case R.id.activity_main_consumer_pss:
                mCompositeDisposable.add(GodEye.instance().observeModule(GodEye.ModuleName.PSS,new LogObserver<>(GodEye.ModuleName.PSS, this)));
                break;
            case R.id.activity_main_consumer_ram:
                mCompositeDisposable.add(GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).subject().subscribe(new LogObserver<RamInfo>("ram", this)));
                break;
            case R.id.activity_main_consumer_network:
                mCompositeDisposable.add(GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).subject().subscribe(new LogObserver<NetworkInfo>("network", this)));
                break;
            case R.id.activity_main_consumer_sm:
                mCompositeDisposable.add(GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).subject().subscribe(new LogObserver<BlockInfo>("sm", this)));
                break;
            case R.id.activity_main_consumer_startup:
                mCompositeDisposable.add(GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).subject().subscribe(new LogObserver<StartupInfo>("startup", this)));
                break;
            case R.id.activity_main_consumer_traffic:
                mCompositeDisposable.add(GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).subject().subscribe(new LogObserver<TrafficInfo>("traffic", this)));
                break;
            case R.id.activity_main_consumer_crash:
                mCompositeDisposable.add(GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).subject().subscribe(new LogObserver<List<CrashInfo>>("crash", this)));
                break;
            case R.id.activity_main_consumer_thread:
                mCompositeDisposable.add(GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).subject().subscribe(new LogObserver<List<Thread>>("thread", this)));
                break;
            case R.id.activity_main_consumer_pageload:
                mCompositeDisposable.add(GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).subject().subscribe(new LogObserver<>("pageload", this)));
                break;
            case R.id.activity_main_make_block:
                block();
                break;
            case R.id.activity_main_make_request:
                request();
                break;
            case R.id.activity_main_make_invocations:
                makeInvocations();
                break;
            case R.id.activity_main_make_leak:
                jumpToLeak();
                break;
            case R.id.activity_main_make_leak_v4:
                jumpToLeakV4();
                break;
            case R.id.activity_main_make_crash:
                throw new RuntimeException("this is a crash made by AndroidGodEye");
            case R.id.activity_main_make_deadlock:
                DeadLockMaker.makeBlock(this);
                DeadLockMaker.makeNormal();
                break;
            case R.id.activity_main_make_pageload:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_consumer_cancel_watch:
                mCompositeDisposable.clear();
                break;
            case R.id.activity_main_make_clear:
                mActivityMainLogview.clear();
                break;
            default:
                break;
        }
    }

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

    private void makeInvocations() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    request();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        methodA();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        for (int i = 0; i < 1000; i++) {
            methodC();
            methodD();
        }
    }

    private void request() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = mZygote;
                    Request request = new Request.Builder()
                            .url("https://tech.hikyson.cn/")
                            .build();
                    Response response = client.newCall(request).execute();
                    String body = response.body().string();
                    L.d("testRequest111 result:" + response.code());
                } catch (IOException e) {
                    e.printStackTrace();
                    L.d("testRequest111 result:" + String.valueOf(e));
                }
            }
        }).start();
    }

    private void block() {
        EditText editText = findViewById(R.id.activity_main_block_et);
        try {
            final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
            runOnUiThread(() -> {
                try {
                    Thread.sleep(blockTime);
                } catch (Throwable e) {
                }
            });
        } catch (Throwable e) {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Input valid time for jank(block)!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void jumpToLeak() {
        Intent intent = new Intent(MainActivity.this, LeakActivity.class);
        startActivity(intent);
    }

    private void jumpToLeakV4() {
        Intent intent = new Intent(MainActivity.this, LeakActivityV4.class);
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (mActivityMainMethodCanary.isChecked()) {
//                    GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).install(new GodEyeConfig.MethodCanaryConfig());
//                }
//                if (mActivityMainCpu.isChecked()) {
//                    GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).install(new GodEyeConfig.CpuConfig());
//                }
//                if (mActivityMainBattery.isChecked()) {
//                    GodEye.instance().<Battery>getModule(GodEye.ModuleName.BATTERY).install(new GodEyeConfig.BatteryConfig());
//                }
//                if (mActivityMainFps.isChecked()) {
//                    GodEye.instance().<Fps>getModule(GodEye.ModuleName.FPS).install(new GodEyeConfig.FpsConfig());
//                }
//                if (mActivityMainLeak.isChecked()) {
//                    GodEyeConfig.LeakConfig leakConfig = new GodEyeConfig.LeakConfig();
//                    leakConfig.debug = true;
//                    leakConfig.debugNotification = true;
//                    GodEye.instance().<LeakDetector>getModule(GodEye.ModuleName.LEAK).install(leakConfig);
//                }
//                if (mActivityMainHeap.isChecked()) {
//                    GodEye.instance().<Heap>getModule(GodEye.ModuleName.HEAP).install(new GodEyeConfig.HeapConfig());
//                }
//                if (mActivityMainPss.isChecked()) {
//                    GodEye.instance().<Pss>getModule(GodEye.ModuleName.PSS).install(new GodEyeConfig.PssConfig());
//                }
//                if (mActivityMainRam.isChecked()) {
//                    GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).install(new GodEyeConfig.RamConfig());
//                }
//                if (mActivityMainSm.isChecked()) {
//                    GodEyeConfig.SmConfig smConfig = new GodEyeConfig.SmConfig();
//                    smConfig.debugNotification = true;
//                    GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).install(smConfig);
//                }
//                if (mActivityMainTraffic.isChecked()) {
//                    GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).install(new GodEyeConfig.TrafficConfig());
//                }
//                if (mActivityMainCrash.isChecked()) {
//                    GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).install(new GodEyeConfig.CrashConfig());
//                }
//                if (mActivityMainThread.isChecked()) {
//                    GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).install(new GodEyeConfig.ThreadConfig());
//                }
//                if (mActivityMainPageload.isChecked()) {
//                    GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).install(new GodEyeConfig.PageloadConfig());
//                }
//            }
//        }).start();

    }

    @Override
    public void log(String msg) {
        mActivityMainLogview.log(msg);
    }
}
