package cn.hikyson.android.godeye.toolbox.network;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import okhttp3.Call;
import okhttp3.EventListener;

public class OkNetworkCollectorFactory implements EventListener.Factory {
    private Producer<NetworkInfo> mNetworkInfoProducer;
    private HttpContentTimeMapping mHttpContentTimeMapping;

    public OkNetworkCollectorFactory(Producer<NetworkInfo> producer) {
        this.mNetworkInfoProducer = producer;
        this.mHttpContentTimeMapping = new HttpContentTimeMapping();
    }

    @Override
    public EventListener create(Call call) {
        return new OkNetworkEventListener(this.mNetworkInfoProducer, this.mHttpContentTimeMapping);
    }

    public OkHttpNetworkContentInterceptor createInterceptor() {
        return new OkHttpNetworkContentInterceptor(this.mHttpContentTimeMapping);
    }
}
