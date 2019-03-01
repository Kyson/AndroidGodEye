package cn.hikyson.android.godeye.toolbox;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import okhttp3.Call;
import okhttp3.EventListener;

public class OkNetworkCollectorFactory implements EventListener.Factory {
    private Producer<RequestBaseInfo> mRequestBaseInfoProducer;

    public OkNetworkCollectorFactory(Producer<RequestBaseInfo> requestBaseInfoProducer) {
        this.mRequestBaseInfoProducer = requestBaseInfoProducer;
    }

    @Override
    public EventListener create(Call call) {
        return new OkNetworkCollector(this.mRequestBaseInfoProducer);
    }
}
