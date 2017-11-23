package cn.hikyson.godeye.internal;


/**
 * Created by kysonchao on 2017/11/22.
 */
public class Module<T extends Snapshotable, E extends Consumer> {
    private T mModule;
    private E mEngine;

    public Module(T module, E engine) {
        mModule = module;
        mEngine = engine;
    }

    public void install() {
        mEngine.work();
    }

    public void uninstall() {

    }
}
