package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.methodcanary.lib.MethodEnterEvent;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.MethodExitEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

public class MethodCanaryConverter {

    static Map<ThreadInfo, List<MethodEvent>> convertToStructure(final File file) {
        Map<ThreadInfo, List<MethodEvent>> methodEventMap = new HashMap<>();
        if (!file.exists() || file.isDirectory()) {
            return methodEventMap;
        }
        BufferedReader reader = null;
        try {
            String line;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<MethodEvent> currentThreadMethodEvents = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[THREAD]")) {
                    Pattern r = Pattern.compile("^\\[THREAD]id=(\\d*);name=(.*);priority=(\\d*)$");
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        long id = Long.parseLong(m.group(1));
                        String name = m.group(2);
                        int priority = Integer.parseInt(m.group(3));
                        ThreadInfo currentThread = new ThreadInfo(id, name, priority);
                        currentThreadMethodEvents = methodEventMap.get(currentThread);
                        if (currentThreadMethodEvents == null) {
                            currentThreadMethodEvents = new ArrayList<>();
                            methodEventMap.put(currentThread, currentThreadMethodEvents);
                        }
                    } else {
                        throw new IllegalStateException("illegal format for [THREAD] line:" + line);
                    }
                } else if (line.startsWith("PUSH:")) {
                    Pattern r = Pattern.compile("^PUSH:et=(\\d*);cn=(.*);ma=(\\d*);mn=(.*);md=(.*)$");
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        long eventTime = Long.parseLong(m.group(1));
                        String className = m.group(2);
                        int methodAccessFlag = Integer.parseInt(m.group(3));
                        String methodName = m.group(4);
                        String methodDesc = m.group(5);
                        if (currentThreadMethodEvents != null) {
                            currentThreadMethodEvents.add(new MethodEnterEvent(className, methodAccessFlag, methodName, methodDesc, eventTime));
                        }
                    } else {
                        throw new IllegalStateException("illegal format for PUSH line:" + line);
                    }
                } else if (line.startsWith("POP:")) {
                    Pattern r = Pattern.compile("^POP:et=(\\d*);cn=(.*);ma=(\\d*);mn=(.*);md=(.*)$");
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        long eventTime = Long.parseLong(m.group(1));
                        String className = m.group(2);
                        int methodAccessFlag = Integer.parseInt(m.group(3));
                        String methodName = m.group(4);
                        String methodDesc = m.group(5);
                        if (currentThreadMethodEvents != null) {
                            currentThreadMethodEvents.add(new MethodExitEvent(className, methodAccessFlag, methodName, methodDesc, eventTime));
                        }
                    } else {
                        throw new IllegalStateException("illegal format for POP line:" + line);
                    }
                }
            }
            return methodEventMap;
        } catch (IOException e) {
            e.printStackTrace();
            return methodEventMap;
        } finally {
            IoUtil.closeSilently(reader);
        }
    }




}
