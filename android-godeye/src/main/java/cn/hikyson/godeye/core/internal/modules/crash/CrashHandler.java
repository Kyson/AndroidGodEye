package cn.hikyson.godeye.core.internal.modules.crash;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Producer<CrashInfo> mProducer;
//    private Context mContext;

    public CrashHandler(Producer<CrashInfo> producer, Thread.UncaughtExceptionHandler defaultHandler) {
        mProducer = producer;
        mDefaultHandler = defaultHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        CrashInfo crashInfo = new CrashInfo(thread, ex);
        mProducer.produce(crashInfo);
        L.d(String.valueOf(crashInfo));
        mDefaultHandler.uncaughtException(thread, ex);
    }

//    /**
//     * 保存错误信息到文件中
//     *
//     * @param ex
//     * @return 返回文件名称, 便于将文件传送到服务器
//     */
//    private String saveCrashInfo2File(Throwable ex) {
//
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : infos.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "=" + value + "\n");
//        }
//
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        ex.printStackTrace(printWriter);
//        Throwable cause = ex.getCause();
//        while (cause != null) {
//            cause.printStackTrace(printWriter);
//            cause = cause.getCause();
//        }
//        printWriter.close();
//        String result = writer.toString();
//        sb.append(result);
//        try {
//            long timestamp = System.currentTimeMillis();
//
//            String fileName = "crash-" + str + "-" + timestamp + ".log";
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                String path = Environment.getExternalStorageDirectory().getPath() + "/crash/";
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                FileOutputStream fos = new FileOutputStream(path + fileName);
//                fos.write(sb.toString().getBytes());
//                System.out.println(sb.toString());
//                fos.close();
//            }
//            return fileName;
//        } catch (Exception e) {
//        }
//        return null;
//    }

//    private static String getNowTimeString() {
//        return FORMATTER.format(new Date(System.currentTimeMillis()));
//    }
}