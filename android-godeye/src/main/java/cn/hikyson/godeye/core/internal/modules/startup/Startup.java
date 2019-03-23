package cn.hikyson.godeye.core.internal.modules.startup;

import cn.hikyson.godeye.core.internal.ProduceableSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 启动模块
 * 无需安装
 * 发射数据线程未知
 * Created by kysonchao on 2017/11/23.
 */
public class Startup extends ProduceableSubject<StartupInfo> {

    @Override
    protected Subject<StartupInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
