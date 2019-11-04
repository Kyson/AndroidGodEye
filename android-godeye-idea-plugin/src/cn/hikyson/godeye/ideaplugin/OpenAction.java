package cn.hikyson.godeye.ideaplugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.util.Objects;
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
            String path = parseAndroidSDKPath(Objects.requireNonNull(anActionEvent.getProject()));
            if (TextUtils.isEmpty(path)) {
                Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", "Can not parse sdk.dir, Please add 'sdk.dir' to 'local.properties'.", NotificationType.ERROR));
                return;
            }
            String finalPort = PORT;
            String inputPort = askForPort(anActionEvent.getProject());
            if (inputPort != null && inputPort.length() > 0) {
                finalPort = inputPort;
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
            Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", e.getLocalizedMessage(), NotificationType.ERROR));
        }
    }

    private String askForPort(Project project) {
        return Messages.showInputDialog(project,
                "Input Dashboard Port, Default is " + PORT, "AndroidGodEye",
                Messages.getQuestionIcon(), PORT, new InputValidator() {

                    private boolean isValid(String s) {
                        if (s == null || s.length() == 0) {
                            return false;
                        }
                        try {
                            int port = Integer.parseInt(s);
                            return true;
                        } catch (Throwable e) {
                            return false;
                        }
                    }

                    @Override
                    public boolean checkInput(String s) {
                        return isValid(s);
                    }

                    @Override
                    public boolean canClose(String s) {
                        return isValid(s);
                    }
                });
    }

    private String parseAndroidSDKPath(Project project) throws IOException {
        VirtualFile localP = project.getBaseDir().findFileByRelativePath("./local.properties");
        if (localP == null) {
            return "";
        }
        Properties localProperties = new Properties();
        localProperties.load(localP.getInputStream());
        return localProperties.getProperty("sdk.dir");
    }
}
