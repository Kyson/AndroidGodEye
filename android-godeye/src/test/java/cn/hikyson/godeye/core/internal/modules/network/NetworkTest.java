package cn.hikyson.godeye.core.internal.modules.network;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedHashMap;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class NetworkTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withNetworkConfig(new NetworkConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            NetworkInfo networkInfo0 = new NetworkInfo();
            networkInfo0.message = "networkInfo0";
            networkInfo0.networkTime = new NetworkTime();
            networkInfo0.networkTime.totalTimeMillis = 1000;
            networkInfo0.networkTime.networkTimeMillisMap = new LinkedHashMap<>();
            networkInfo0.networkTime.networkTimeMillisMap.put("AndroidGodEye-Network-Queue", 800L);

            NetworkInfo networkInfo1 = new NetworkInfo();
            networkInfo1.message = "networkInfo1";
            NetworkInfo networkInfo2 = new NetworkInfo();
            networkInfo2.message = "networkInfo2";
            NetworkInfo networkInfo3 = new NetworkInfo();
            networkInfo3.message = "networkInfo3";

            GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).produce(networkInfo0);
            GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).produce(networkInfo1);
            TestObserver<NetworkInfo> testObserver = GodEye.instance().<Network, NetworkInfo>moduleObservable(GodEye.ModuleName.NETWORK).test();
            GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).produce(networkInfo2);
            GodEye.instance().<Network>getModule(GodEye.ModuleName.NETWORK).produce(networkInfo3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<NetworkInfo>() {
                @Override
                public boolean test(NetworkInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.message.equals("networkInfo0")
                            && info.networkTime.totalTimeMillis == 1000;
                }
            }).assertValueAt(1, new Predicate<NetworkInfo>() {
                @Override
                public boolean test(NetworkInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.message.equals("networkInfo1");
                }
            }).assertValueAt(2, new Predicate<NetworkInfo>() {
                @Override
                public boolean test(NetworkInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.message.equals("networkInfo2");
                }
            }).assertValueAt(3, new Predicate<NetworkInfo>() {
                @Override
                public boolean test(NetworkInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.message.equals("networkInfo3");
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}