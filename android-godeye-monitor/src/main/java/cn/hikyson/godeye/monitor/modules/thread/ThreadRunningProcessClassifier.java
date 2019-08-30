package cn.hikyson.godeye.monitor.modules.thread;

public interface ThreadRunningProcessClassifier {
    ThreadRunningProcess classify(ThreadInfo threadInfo);
}
