package cn.hikyson.godeye;

import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.Module;
import cn.hikyson.godeye.internal.modules.cpu.Cpu;
import io.reactivex.Observable;

/**
 * 入口
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
    public void install() {
        Module<Cpu,Consumer> module = new Module<>(new Cpu(), new Consumer() {
            @Override
            public Observable work() {
                return null;
            }
        });
    }
}
