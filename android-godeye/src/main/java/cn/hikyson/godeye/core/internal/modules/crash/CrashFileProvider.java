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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.GsonSerializer;
import cn.hikyson.godeye.core.helper.Serializer;
import cn.hikyson.godeye.core.utils.IoUtil;

/**
 * Created by kysonchao on 2017/12/18.
 */
@Keep
public class CrashFileProvider implements CrashProvider {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    private Context mContext;
    private Serializer mSerializer;

    public CrashFileProvider(Serializer serializer) {
        mContext = GodEye.instance().getApplication();
        mSerializer = serializer;
    }

    public CrashFileProvider() {
        this(new GsonSerializer());
    }

    @Override
    public synchronized void storeCrash(CrashInfo crashInfo) throws IOException {
        File file = getStoreFile(mContext, getStoreFileName());
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
        return crashInfos;
    }

    private static final String SUFFIX = ".crash";

    private FilenameFilter mCrashFilenameFilter = (dir, filename) -> filename.endsWith(SUFFIX);

    private static String getStoreFileName() {
        return FORMATTER.format(new Date(System.currentTimeMillis())) + SUFFIX;
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
