//package cn.hikyson.godeye.monitor.modules;
//
//import android.net.Uri;
//
//import com.ctrip.ibu.performance.SessionPerformanceService;
//import com.ctrip.ibu.performance.internal.sm.Fpser;
//
///**
// * 简单命令的模块
// * Created by kysonchao on 2017/9/3.
// */
//public class CommandModule implements Module {
//
//    @Override
//    public byte[] process(String path, Uri uri) throws Throwable {
//        String command = uri.getQueryParameter("its");
//        if ("StartSession".equals(command)) {
//            SessionPerformanceService.get().start();
//        } else if ("StopSession".equals(command)) {
//            SessionPerformanceService.get().stop();
//        } else if ("TickSession".equals(command)) {
//            SessionPerformanceService.get().tick();
//        } else if ("StartRefreshFrame".equals(command)) {
//            Fpser.get().start();
//        } else if ("StopRefreshFrame".equals(command)) {
//            Fpser.get().stop();
//        } else {
//            throw new RuntimeException("no such command for " + String.valueOf(uri));
//        }
//        return new ResultWrapper<>(true).toBytes();
//    }
//}
