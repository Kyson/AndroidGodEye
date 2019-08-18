package cn.hikyson.godeye.ideaplugin;

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
                return;
            }
            final String commandTcpProxy = String.format("%s/platform-tools/adb forward tcp:%s tcp:%s", path, PORT, PORT);
            mLogger.info("Exec [" + commandTcpProxy + "].");
            Runtime.getRuntime().exec(commandTcpProxy);
            mLogger.info("Current os name is " + SystemUtils.OS_NAME);
            String commandOpenUrl = "";
            if (SystemUtils.IS_OS_WINDOWS) {
                commandOpenUrl = String.format("cmd /c start http://localhost:%s/index.html", PORT);
            } else {
                commandOpenUrl = String.format("open http://localhost:%s/index.html", PORT);
            }
            mLogger.info("Exec [" + commandOpenUrl + "].");
            Runtime.getRuntime().exec(commandOpenUrl);
        } catch (Throwable e) {
            mLogger.error(e);
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

}
