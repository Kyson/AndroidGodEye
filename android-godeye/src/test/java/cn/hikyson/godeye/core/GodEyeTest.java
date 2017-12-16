//package cn.hikyson.godeye.core;
//
//import android.content.Context;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.shadows.ShadowApplication;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
//import io.reactivex.functions.Consumer;
//
///**
// * Created by kysonchao on 2017/12/16.
// */
//@RunWith(RobolectricTestRunner.class)
//public class GodEyeTest {
//
//    @Before
//    public void setup() {
////        GodEye.instance().battery().install(context);
////        GodEye.instance().fps().install(context);
////        GodEye.instance().heap().install();
////        GodEye.instance().ram().install(context);
////        GodEye.instance().sm().install(context);
////        GodEye.instance().traffic().install();
//    }
//
//    @After
//    public void tearDown() {
////        GodEye.instance().battery().uninstall();
////        GodEye.instance().fps().uninstall();
////        GodEye.instance().heap().uninstall();
////        GodEye.instance().ram().uninstall();
////        GodEye.instance().sm().uninstall();
////        GodEye.instance().traffic().uninstall();
//    }
//
//    @Test
//    public void cpu() throws ExecutionException, InterruptedException {
//        Context context = ShadowApplication.getInstance().getApplicationContext();
//        GodEye.instance().cpu().install();
//        final CompletableFuture<CpuInfo> future = new CompletableFuture<>();
//        GodEye.instance().cpu().subject().subscribe(new Consumer<CpuInfo>() {
//            @Override
//            public void accept(CpuInfo cpuInfo) throws Exception {
//                future.complete(cpuInfo);
//            }
//        });
//        CpuInfo cpuInfo = future.get();
//        Assert.assertTrue(cpuInfo != null);
//        Assert.assertTrue(cpuInfo.totalUseRatio >= cpuInfo.appCpuRatio
//                && cpuInfo.totalUseRatio >= cpuInfo.ioWaitRatio
//                && cpuInfo.totalUseRatio >= cpuInfo.sysCpuRatio
//                && cpuInfo.totalUseRatio >= cpuInfo.userCpuRatio
//                && cpuInfo.appCpuRatio >= 0
//                && cpuInfo.ioWaitRatio >= 0
//                && cpuInfo.sysCpuRatio >= 0
//                && cpuInfo.userCpuRatio >= 0
//        );
//        GodEye.instance().cpu().uninstall();
//    }
//}
