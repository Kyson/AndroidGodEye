package cn.hikyson.godeye;

import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.Eater;
import cn.hikyson.godeye.internal.modules.network.Network;
import cn.hikyson.godeye.internal.modules.network.RequestBaseInfo;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class RequestEngine implements Consumer<RequestBaseInfo>, Eater<RequestBaseInfo> {
    private Network<RequestBaseInfo> mRequestBaseInfoNetwork;


    @Override
    public Observable<RequestBaseInfo> work() {
        return mRequestBaseInfoNetwork.consume();
    }

    @Override
    public void produce(RequestBaseInfo data) {
        mRequestBaseInfoNetwork.produce(data);
    }
}
