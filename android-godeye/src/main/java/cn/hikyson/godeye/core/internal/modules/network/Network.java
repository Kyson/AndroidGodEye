package cn.hikyson.godeye.core.internal.modules.network;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 网络模块
 * 发射数据未知线程
 * Created by kysonchao on 2017/11/22.
 */
public class Network extends ProduceableSubject<NetworkInfo> implements Install<NetworkContext> {

    private NetworkContext mConfig;

    @Override
    public synchronized void install(NetworkContext config) {
        if (config == null) {
            throw new IllegalArgumentException("Network module install fail because config is null.");
        }
        if (mConfig != null) {
            L.d("Network already installed, ignore.");
            return;
        }
        mConfig = config;
    }

    @Override
    public synchronized void uninstall() {
        if (mConfig == null) {
            L.d("Network already uninstalled, ignore.");
            return;
        }
        mConfig = null;
    }

    @Override
    public void produce(NetworkInfo data) {
        if (mConfig == null) {
            L.d("Network is not installed, produce data fail.");
            return;
        }
        super.produce(data);
    }
}
