package cn.hikyson.godeye.internal.engines;

import cn.hikyson.godeye.internal.Eater;
import cn.hikyson.godeye.internal.Install;

/**
 * Created by kysonchao on 2017/11/23.
 */

public abstract class InstallEngine<T, C> extends EngineImpl<T, C> implements Install<C> {

    public InstallEngine(Eater<T> eater) {
        super(eater);
    }

    @Override
    public void work(C config) {
        install(config);
    }

    @Override
    public void shutdown() {
        uninstall();
    }
}
