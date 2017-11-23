package cn.hikyson.godeye;

import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.Module;
import cn.hikyson.godeye.internal.engines.Enginee;
import cn.hikyson.godeye.internal.modules.cpu.Cpu;
import io.reactivex.Observable;

/**
 * 入口
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
    private Cpu mCpu;

    public void cpu(IntervalEnginee enginee){
        if(mCpu == null){
            mCpu = new Cpu();
        }

    }



    public void install() {
        Module<Cpu,Consumer> module = new Module<>(new Cpu(), new Consumer() {
            @Override
            public Observable work() {
                return null;
            }
        });
    }
}
