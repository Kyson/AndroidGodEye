package cn.hikyson.godeye.ideaplugin;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.util.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiangxing on 2018/10/26.
 */
public class OpenAction extends AnAction {
    private Logger mLogger = Logger.getInstance(OpenAction.class);
    private static final String PORT = "5390";
    private static final String KEY_PORT = "KEY_PORT";
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        try {
            Project project = anActionEvent.getProject();
            String path = parseAndroidSDKPath(Objects.requireNonNull(project));
            if (TextUtils.isEmpty(path)) {
                Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", "Can not parse sdk.dir, Please add 'sdk.dir' to 'local.properties'.", NotificationType.ERROR));
                return;
            }
            mLogger.info("Current os name is " + SystemUtils.OS_NAME);
            String adbPath = String.format("%s/platform-tools/adb", path);
            mLogger.info("ADB path is " + adbPath);
            String parsedPort = parsePortByLogcatWithTimeout(project, adbPath);
            mLogger.info("Parse AndroidGodEye port is running at " + parsedPort);
            String inputPort = askForPort(project, parsedPort, getSavedPort(project));
            if (inputPort == null) {
                mLogger.warn("inputPort == null");
                return;
            }
            saveDefaultPort(project, inputPort);
            final String commandTcpProxy = String.format("%s forward tcp:%s tcp:%s", adbPath, inputPort, inputPort);
            mLogger.info("Exec [" + commandTcpProxy + "].");
            Runtime.getRuntime().exec(commandTcpProxy);
            String commandOpenUrl;
            if (SystemUtils.IS_OS_WINDOWS) {
                commandOpenUrl = String.format("cmd /c start http://localhost:%s/index.html", inputPort);
            } else {
                commandOpenUrl = String.format("open http://localhost:%s/index.html", inputPort);
            }
            mLogger.info("Exec [" + commandOpenUrl + "].");
            Runtime.getRuntime().exec(commandOpenUrl);
            Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Success", String.format("http://localhost:%s/index.html", inputPort), NotificationType.INFORMATION));
        } catch (Throwable e) {
            mLogger.warn(e);
            Notifications.Bus.notify(new Notification("AndroidGodEye", "Open AndroidGodEye Failed", String.valueOf(e), NotificationType.ERROR));
        }
    }

    private String getSavedPort(Project project) {
        return PropertiesComponent.getInstance(project).getValue(KEY_PORT, PORT);
    }

    private void saveDefaultPort(Project project, String port) {
        PropertiesComponent.getInstance(project).setValue(KEY_PORT, port);
    }

    private String parsePortByLogcatWithTimeout(Project project, String adbPath) {
        final String[] parsedPort = new String[1];
        ProgressManager.getInstance().
                runProcessWithProgressSynchronously(new Runnable() {
                    public void run() {
                        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
                        progressIndicator.setIndeterminate(false);
                        progressIndicator.setFraction(0.3);
                        mLogger.info("parsePortByLogcatWithTimeout start parsing");
                        Future<String> result = mExecutorService.submit(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                return parsePortByLogcat(adbPath, progressIndicator);
                            }
                        });
                        mLogger.info("parsePortByLogcatWithTimeout wait parsing");
                        try {
                            parsedPort[0] = result.get(5, TimeUnit.SECONDS);
                            mLogger.info("parsePortByLogcatWithTimeout wait end,get parsed port:" + parsedPort[0]);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            mLogger.info("parsePortByLogcatWithTimeout wait end, timeout.");
                        }
                        progressIndicator.setFraction(1.0);
                    }
                }, "Parsing Port, Wait For 5 Seconds...", true, project);
        mLogger.info("parsePortByLogcatWithTimeout return");
        return parsedPort[0];
    }

    private String parsePortByLogcat(String adbPath, ProgressIndicator progressIndicator) {
        try {
            String cmd = String.format("%s logcat -d -s AndroidGodEye", adbPath);
            mLogger.info("parsePortByLogcat exec: " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            progressIndicator.setFraction(0.6);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String logcat = String.valueOf(sb);
            mLogger.info("parsePortByLogcat find logcat of tag AndroidGodEye: " + logcat);
            progressIndicator.setFraction(0.8);
            if (logcat != null && !logcat.equals("")) {
                List<String> list = new ArrayList<>();
                Pattern pattern = Pattern.compile("AndroidGodEye monitor is running at port \\[(.*)]");
                Matcher m = pattern.matcher(logcat);
                while (m.find()) {
                    list.add(m.group(1));
                }
                if (!list.isEmpty()) {
                    mLogger.info("parsePortByLogcat find port list: " + String.valueOf(list));
                    progressIndicator.setFraction(0.9);
                    return list.get(list.size() - 1);
                }
            }
            p.waitFor();
            reader.close();
            p.destroy();
            return "";
        } catch (IOException | InterruptedException e) {
            mLogger.warn("parsePortByLogcat error: " + String.valueOf(e));
            progressIndicator.setFraction(0.9);
            return "";
        }
    }

    private String askForPort(Project project, String portRunning, String portSaved) {
        StringBuilder sb = new StringBuilder();
        sb.append("Input AndroidGodEye Debug Monitor Port.").append("\n");
        String initPort = portSaved;
        if (portRunning != null && !portRunning.equals("")) {
            initPort = portRunning;
            sb.append("AndroidGodEye monitor is running at ").append(initPort);
        } else {
            sb.append("Can not find which port AndroidGodEye monitor is running at, initialize with default ").append(initPort);
        }
        return Messages.showInputDialog(project,
                String.valueOf(sb), "AndroidGodEye", IconLoader.getIcon("/icons/android_god_eye_logo_2.png"), initPort, new InputValidator() {

                    private boolean isValid(String s) {
                        if (s == null || s.length() == 0) {
                            return false;
                        }
                        try {
                            int port = Integer.parseInt(s);
                            return port >= 1 && port <= 65535;
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
