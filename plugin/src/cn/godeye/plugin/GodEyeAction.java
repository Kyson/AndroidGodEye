package cn.godeye.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.Properties;

/**
 * Created by xiangxing on 2018/10/26.
 */
public class GodEyeAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        try {
            String path = parseAndroidSDKPath(anActionEvent.getProject());
            Runtime.getRuntime().exec(path + "/platform-tools/adb forward tcp:5390 tcp:5390");
            Runtime.getRuntime().exec("open http://localhost:5390/index.html"); // 不适合 windows
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析 Android SDK位置
     *
     * @param project
     * @return
     */
    public static String parseAndroidSDKPath(Project project) {
        try {
            VirtualFile localP = project.getBaseDir().findFileByRelativePath("./local.properties");
            Properties localProperties;
            localProperties = new Properties();
            localProperties.load(localP.getInputStream());
            return localProperties.getProperty("sdk.dir");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

}
