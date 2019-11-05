package cn.hikyson.godeye.ideaplugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.util.TextUtils;

import java.util.Properties;

/**
 * Created by xiangxing on 2018/10/26.
 */
public class OpenAction extends AnAction {
    private Logger mLogger = Logger.getInstance(OpenAction.class);
    private static final String PORT = "5390";

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        try {
            String path = parseAndroidSDKPath(anActionEvent.getProject());
            if (TextUtils.isEmpty(path)) {
                mLogger.error("Can not parse sdk.dir.");
                Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", "Can not parse sdk.dir, Please add 'sdk.dir' to 'local.properties'.", NotificationType.ERROR));
                return;
            }
            String finalPort = PORT;
            String androidGodEyeMonitorPort = parseGradleProperties(anActionEvent.getProject(), "ANDROID_GODEYE_MONITOR_PORT");
            if (!TextUtils.isEmpty(androidGodEyeMonitorPort)) {
                finalPort = androidGodEyeMonitorPort;
            }
            final String commandTcpProxy = String.format("%s/platform-tools/adb forward tcp:%s tcp:%s", path, finalPort, finalPort);
            mLogger.info("Exec [" + commandTcpProxy + "].");
            Runtime.getRuntime().exec(commandTcpProxy);
            mLogger.info("Current os name is " + SystemUtils.OS_NAME);
            String commandOpenUrl;
            if (SystemUtils.IS_OS_WINDOWS) {
                commandOpenUrl = String.format("cmd /c start http://localhost:%s/index.html", finalPort);
            } else {
                commandOpenUrl = String.format("open http://localhost:%s/index.html", finalPort);
            }
            mLogger.info("Exec [" + commandOpenUrl + "].");
            Runtime.getRuntime().exec(commandOpenUrl);
            Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Success", String.format("http://localhost:%s/index.html", finalPort), NotificationType.INFORMATION));
        } catch (Throwable e) {
            mLogger.error(e);
            Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", e.getLocalizedMessage(), NotificationType.ERROR));
        }
    }

    private String parseAndroidSDKPath(Project project) {
        try {
            VirtualFile localP = project.getBaseDir().findFileByRelativePath("./local.properties");
            if (localP == null) {
                mLogger.error("Can not find local.properties");
                return "";
            }
            Properties localProperties = new Properties();
            localProperties.load(localP.getInputStream());
            return localProperties.getProperty("sdk.dir");
        } catch (Throwable e1) {
            mLogger.error(e1);
        }
        return "";
    }

    private String parseGradleProperties(Project project, String key) {
        try {
            VirtualFile gradlePropertiesFile = project.getBaseDir().findFileByRelativePath("./gradle.properties");
            if (gradlePropertiesFile == null) {
                mLogger.error("Can not find gradle.properties");
                return "";
            }
            Properties gradleProperties = new Properties();
            gradleProperties.load(gradlePropertiesFile.getInputStream());
            return gradleProperties.getProperty(key);
        } catch (Throwable e1) {
            mLogger.error(e1);
        }
        return "";
    }
}
