package cn.hikyson.godeye;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.Producer;
import cn.hikyson.godeye.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class CpuEngine implements Consumer<CpuInfo>,Producer<CpuInfo> {

    @Override
    public Observable<CpuInfo> consume() {
        return Observable.interval(2, TimeUnit.SECONDS).flatMap(new Function<Long, ObservableSource<CpuInfo>>() {
            @Override
            public ObservableSource<CpuInfo> apply(Long aLong) throws Exception {
                return new Cpu().snapshot();
            }
        });
    }

    @Override
    public void produce(CpuInfo data) {

    }
}
