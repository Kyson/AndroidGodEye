package cn.hikyson.godeye;

import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.Producer;
import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.internal.modules.sm.Sm;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class BlockEngine implements Consumer<BlockInfo>, Producer<BlockInfo> {
    private Sm mSm;

    @Override
    public void produce(BlockInfo data) {

    }

    @Override
    public Observable<BlockInfo> consume() {
        return mSm.;
    }
}
