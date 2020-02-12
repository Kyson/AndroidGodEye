package cn.hikyson.godeye.core.helper;

import java.io.InputStream;

import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.utils.IoUtil;

public class GodEyeConfigHelper {
    public static GodEyeConfig createFromResource() {
        InputStream is = null;
        try {
            is = GodEyeConfigHelper.class.getClassLoader().getResourceAsStream("install.config");
            return GodEyeConfig.fromInputStream(is);
        } finally {
            IoUtil.closeSilently(is);
        }
    }

    public static GodEyeConfig createFromResource2() {
        InputStream is = null;
        try {
            is = GodEyeConfigHelper.class.getClassLoader().getResourceAsStream("install2.config");
            return GodEyeConfig.fromInputStream(is);
        } finally {
            IoUtil.closeSilently(is);
        }
    }
}
