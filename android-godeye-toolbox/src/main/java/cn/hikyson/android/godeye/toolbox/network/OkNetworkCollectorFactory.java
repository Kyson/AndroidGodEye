package cn.hikyson.android.godeye.toolbox.network;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import okhttp3.Call;
import okhttp3.EventListener;

public class OkNetworkCollectorFactory extends OkHttpNetworkContentInterceptor implements EventListener.Factory {
    private Producer<NetworkInfo> mNetworkInfoProducer;

    public OkNetworkCollectorFactory(Producer<NetworkInfo> producer) {
        super(new HttpContentTimeMapping());
        this.mNetworkInfoProducer = producer;
    }

    @Override
    public EventListener create(Call call) {
        return new OkNetworkEventListener(this.mNetworkInfoProducer, this.mHttpContentTimeMapping);
    }
}
