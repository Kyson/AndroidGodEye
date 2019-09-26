package cn.hikyson.godeye.core.internal.modules.crash;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Keep;
import android.support.v4.content.PermissionChecker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.GsonSerializer;
import cn.hikyson.godeye.core.helper.Serializer;
import cn.hikyson.godeye.core.utils.FileUtil;
import cn.hikyson.godeye.core.utils.IoUtil;

/**
 * Created by kysonchao on 2017/12/18.
 */
@Keep
public class CrashFileProvider implements CrashProvider {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS_Z", Locale.US);

    private Context mContext;
    private Serializer mSerializer;
    private int mMaxStoreCount;

    public CrashFileProvider(Serializer serializer, int maxStoreCount) {
        mContext = GodEye.instance().getApplication();
        mSerializer = serializer;
        mMaxStoreCount = maxStoreCount;
    }

    public CrashFileProvider() {
        this(new GsonSerializer(), 10);
    }

    @Override
    public synchronized void storeCrash(CrashInfo crashInfo) throws IOException, FileUtil.FileException {
        List<File> crashFiles = Arrays.asList(makeSureCrashDir(mContext).listFiles(mCrashFilenameFilter));
        if (crashFiles.size() >= mMaxStoreCount) {
            Collections.sort(crashFiles, (o1, o2) -> {
                try {
                    return Long.compare(FORMATTER.parse(o2.getName()).getTime(), FORMATTER.parse(o1.getName()).getTime());
                } catch (Throwable e) {
                    return Long.compare(o2.lastModified(), o1.lastModified());
                }
            });
            for (int i = 0; i < (crashFiles.size() + 1 - mMaxStoreCount); i++) {
                FileUtil.deleteIfExists(crashFiles.get(crashFiles.size() - i - 1));
            }
        }
        File file = getStoreFile(mContext, getStoreFileName(crashInfo.timestampMillis));
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(mSerializer.serialize(crashInfo));
        } catch (IOException ignore) {
        } finally {
            IoUtil.closeSilently(fileWriter);
        }
    }

    @Override
    public synchronized List<CrashInfo> restoreCrash() throws IOException {
        File[] crashFiles = makeSureCrashDir(mContext).listFiles(mCrashFilenameFilter);
        List<CrashInfo> crashInfos = new ArrayList<>();
        for (File crashFile : crashFiles) {
            FileReader reader = null;
            try {
                reader = new FileReader(crashFile);
                crashInfos.add(mSerializer.deserialize(reader, CrashInfo.class));
            } catch (IOException ignore) {
            } finally {
                IoUtil.closeSilently(reader);
            }
        }
        // sort by crash time [2019_09_04_12_00_00.crash,2019_09_02_12_00_00.crash,2019_09_02_11_00_00.crash]
        Collections.sort(crashInfos, (o1, o2) -> Long.compare(o2.timestampMillis, o1.timestampMillis));
        return crashInfos;
    }

    private static final String SUFFIX = ".crash";

    private FilenameFilter mCrashFilenameFilter = (dir, filename) -> filename.endsWith(SUFFIX);

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
