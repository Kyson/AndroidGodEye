//package cn.hikyson.godeye.monitor.modules;
//
//import android.net.Uri;
//
//import java.util.Collection;
//
///**
// * Created by kysonchao on 2017/11/27.
// */
//public abstract class BaseListModule<T> implements Module {
//    @Override
//    public byte[] process(String path, Uri uri) throws Throwable {
//        Collection<T> ts = popData();
//        if (ts == null || ts.isEmpty()) {
//            return new ResultWrapper("no datas for " + getClass().getSimpleName()).toBytes();
//        }
//        return new ResultWrapper<>(ts).toBytes();
//    }
//
//    abstract Collection<T> popData();
//}
