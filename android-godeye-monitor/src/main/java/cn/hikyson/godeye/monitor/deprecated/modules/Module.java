//package cn.hikyson.godeye.monitor.modules;
//
//import android.net.Uri;
//
//import java.io.UnsupportedEncodingException;
//
//import cn.hikyson.godeye.monitor.utils.GsonUtil;
//
///**
// * Created by kysonchao on 2017/9/3.
// */
//public interface Module {
//    public byte[] process(String path, Uri uri) throws Throwable;
//
//    public static class ResultWrapper<T> {
//
//        public static final int SUCCESS = 1;
//        public static final int DEFAULT_FAIL = 0;
//        public T data;
//        public int code;
//        public String message;
//
//        public ResultWrapper(int code, String message, T data) {
//            this.data = data;
//            this.code = code;
//            this.message = message;
//        }
//
//        public ResultWrapper(String message) {
//            this.data = null;
//            this.code = DEFAULT_FAIL;
//            this.message = message;
//        }
//
//        public ResultWrapper(T data) {
//            this.data = data;
//            this.code = SUCCESS;
//            this.message = "success";
//        }
//
//        public byte[] toBytes() throws UnsupportedEncodingException {
//            return GsonUtil.toJson(this).getBytes("UTF-8");
//        }
//
//        @Override
//        public String toString() {
//            return GsonUtil.toJson(this);
//        }
//    }
//}
