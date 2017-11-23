package cn.hikyson.android.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import cn.hikyson.android.godeye.R;
import cn.hikyson.godeye.internal.modules.battery.Battery;
import cn.hikyson.godeye.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.internal.modules.fps.Fps;
import cn.hikyson.godeye.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.internal.modules.network.Network;
import cn.hikyson.godeye.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.internal.modules.sm.Sm;
import cn.hikyson.godeye.internal.modules.sm.core.SmConfig;
import cn.hikyson.godeye.internal.modules.traffic.Traffic;
import cn.hikyson.godeye.internal.modules.traffic.TrafficInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testCpu(View view) {
        new Cpu(1000).stream(this).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<CpuInfo>() {
            @Override
            public void onNext(CpuInfo cpuInfo) {
                Log.d("kyson", String.valueOf(cpuInfo));
            }

            @Override
            public void onError(Throwable e) {
                Log.d("kyson", "onError:" + String.valueOf(e));
            }

            @Override
            public void onComplete() {
                Log.d("kyson", "onComplete");
            }
        });
    }

    public void testBattery(View view) {
        new Battery().stream(this).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<BatteryInfo>() {
            @Override
            public void onNext(BatteryInfo batteryInfo) {
                Log.d("kyson", String.valueOf(batteryInfo));
            }

            @Override
            public void onError(Throwable e) {
                Log.d("kyson", "onError:" + String.valueOf(e));
            }

            @Override
            public void onComplete() {
                Log.d("kyson", "onComplete");
            }
        });
    }

    public void installSM(View view) {
        Sm.instance().install(MainActivity.this, new SmConfig());
    }

    public void uninstallSM(View view) {
        Sm.instance().uninstall(MainActivity.this);
    }

    public void blockWatch(View view) {
        Observable<BlockInfo> observable = Sm.instance().consume(MainActivity.this);
        observable.subscribe(new Consumer<BlockInfo>() {
            @Override
            public void accept(BlockInfo blockInfo) throws Exception {
                Log.d("kyson", String.valueOf(blockInfo));
            }
        });
    }

    public void block(View view) {
        try {
            EditText editText = findViewById(R.id.activity_main_block_et);
            final long blockTime = Long.parseLong(String.valueOf(editText.getText()));
            Thread.sleep(blockTime);
        } catch (Throwable e) {
        }
    }

    private Network<RequestBaseInfo> network;

    public void network(View view) {
        if (network == null) {
            network = new Network<RequestBaseInfo>();
        }
        RequestBaseInfo requestBaseInfo = new RequestBaseInfo(1000, 2000, 12312, "http://www.trip.com");
        network.produce(requestBaseInfo);
        network.consume(MainActivity.this).subscribe(new Consumer<RequestBaseInfo>() {
            @Override
            public void accept(RequestBaseInfo requestBaseInfo) throws Exception {
                Log.d("kyson", String.valueOf(requestBaseInfo));
            }
        });
    }

    public void jumpToLeak(View view) {
        Intent intent = new Intent(MainActivity.this, LeakActivity.class);
        startActivity(intent);
    }

    public void installLeak(View view) {
        LeakDetector.instance().install(MainActivity.this, getApplication());
        LeakDetector.instance().consume(MainActivity.this).subscribe(new Consumer<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void accept(LeakQueue.LeakMemoryInfo leakMemoryInfo) throws Exception {
                Log.d("kyson", String.valueOf(leakMemoryInfo));
            }
        });
    }

    public void testFps(View view) {
        new Fps().stream(MainActivity.this).subscribe(new Consumer<FpsInfo>() {
            @Override
            public void accept(FpsInfo fpsInfo) throws Exception {
                Log.d("kyson", String.valueOf(fpsInfo));
            }
        });
    }


    public void testTraffic(View view) {
        new Traffic(1000).stream(MainActivity.this).subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo trafficInfo) throws Exception {
                Log.d("kyson", "traffic:" + String.valueOf(trafficInfo));
            }
        });
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

    private String request() {
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
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
