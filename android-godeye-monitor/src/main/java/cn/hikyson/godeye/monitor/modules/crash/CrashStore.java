package cn.hikyson.godeye.monitor.modules.crash;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.content.PermissionChecker;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.GsonSerializer;
import cn.hikyson.godeye.core.helper.Serializer;
import cn.hikyson.godeye.core.utils.FileUtil;
import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import xcrash.TombstoneParser;

/**
 * observe crash and store for godeye monitor
 */
public class CrashStore {
    public static Observable<List<Map<String, String>>> observeCrashAndCache(Context context) {
        return Observable.create(emitter -> {
            try {
                GodEye.instance().observeModule(GodEye.ModuleName.CRASH, (List<Map<String, String>> maps) -> {
                    if (!maps.isEmpty()) {
                        storeCrash(context, maps);
                    }
                    emitter.onNext(restoreCrash(context));
                    emitter.onComplete();
                });
            } catch (UninstallException e) {
                L.e(e);
            }
        });
    }

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS_Z", Locale.US);
    private static final SimpleDateFormat FORMATTER_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    private static final Serializer sSerializer = new GsonSerializer();
    private static final int sMaxStoreCount = 10;
    private static final String SUFFIX = ".crash";
    private static final FilenameFilter mCrashFilenameFilter = (dir, filename) -> filename.endsWith(SUFFIX);

    private static synchronized void storeCrash(Context context, List<Map<String, String>> crashInfos) {
        List<File> crashFiles = null;
        try {
            crashFiles = Arrays.asList(makeSureCrashDir(context).listFiles(mCrashFilenameFilter));
        } catch (IOException e) {
            L.e(e);
        }
        if (crashFiles != null && crashFiles.size() >= sMaxStoreCount) {
            Collections.sort(crashFiles, (o1, o2) -> {
                try {
                    return Long.compare(FORMATTER.parse(o2.getName()).getTime(), FORMATTER.parse(o1.getName()).getTime());
                } catch (Throwable e) {
                    return Long.compare(o2.lastModified(), o1.lastModified());
                }
            });
            for (int i = 0; i < (crashFiles.size() + 1 - sMaxStoreCount); i++) {
                try {
                    FileUtil.deleteIfExists(crashFiles.get(crashFiles.size() - i - 1));
                } catch (FileUtil.FileException e) {
                    L.e(e);
                }
            }
        }
        for (Map<String, String> m : crashInfos) {
            File file = null;
            try {
                file = getStoreFile(context, getStoreFileName(FORMATTER_2.parse(m.get(TombstoneParser.keyCrashTime)).getTime()));
            } catch (Throwable e) {
                L.e(e);
            }
            if (file == null) {
                continue;
            }
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(sSerializer.serialize(m));
            } catch (IOException e) {
                L.e(e);
            } finally {
                IoUtil.closeSilently(fileWriter);
            }
        }
        L.d("CrashStore storeCrash count:%s", crashInfos.size());
    }

    private static synchronized List<Map<String, String>> restoreCrash(Context context) {
        File[] crashFiles = new File[0];
        try {
            crashFiles = makeSureCrashDir(context).listFiles(mCrashFilenameFilter);
        } catch (IOException e) {
            L.e(e);
        }
        List<Map<String, String>> crashInfos = new ArrayList<>();
        if (crashFiles == null || crashFiles.length == 0) {
            return crashInfos;
        }
        for (File crashFile : crashFiles) {
            FileReader reader = null;
            try {
                reader = new FileReader(crashFile);
                crashInfos.add(sSerializer.deserialize(reader, new TypeToken<Map<String, String>>() {
                }.getType()));
            } catch (Throwable e) {
                L.e(e);
            } finally {
                IoUtil.closeSilently(reader);
            }
        }
        // sort by crash time [2019-09-04 12:00:00.crash,2019-09-02 12:00:00.crash,2019-09-02 11:00:00.crash]
        Collections.sort(crashInfos, (o1, o2) -> {
            try {
                // TODO KYSON Attempt to invoke interface method 'java.lang.Object java.util.Map.get(java.lang.Object)' on a null object reference
                return Long.compare(FORMATTER_2.parse(o2.get(TombstoneParser.keyCrashTime)).getTime(), FORMATTER_2.parse(o1.get(TombstoneParser.keyCrashTime)).getTime());
            } catch (Throwable throwable) {
                L.e(throwable);
            }
            return 0;
        });
        L.d("CrashStore restoreCrash count:%s", crashInfos.size());
        return crashInfos;
    }

    private static String getStoreFileName(long crashTime) {
        return FORMATTER.format(new Date(crashTime)) + SUFFIX;
    }

    private static File getStoreFile(Context context, String fileName) throws IOException {
        File file = new File(makeSureCrashDir(context), fileName);
        if (file.exists() && !file.delete()) {
            throw new IOException(file.getAbsolutePath() + " already exist and delete failed");
        }
        return file;
    }

    private static File makeSureCrashDir(Context context) throws IOException {
        File crashDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            crashDir = new File(context.getExternalCacheDir(), "AndroidGodEyeCrash");
        } else {
            crashDir = new File(context.getCacheDir(), "AndroidGodEyeCrash");
        }
        if (!crashDir.exists() && !crashDir.mkdirs()) {
            throw new IOException("can not get crash directory");
        }
        return crashDir;
    }
}
