package cn.hikyson.android.godeye.toolbox.network;

import okhttp3.Call;
import okhttp3.EventListener;

public class GodEyePluginOkNetwork extends OkHttpNetworkContentInterceptor implements EventListener.Factory {

    public GodEyePluginOkNetwork() {
        super(new HttpContentTimeMapping());
    }

    @Override
    public EventListener create(Call call) {
        return new OkNetworkEventListener(this.mHttpContentTimeMapping);
    }
}
