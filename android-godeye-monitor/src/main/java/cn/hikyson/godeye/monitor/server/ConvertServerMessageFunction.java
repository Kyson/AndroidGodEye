package cn.hikyson.godeye.monitor.server;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Function;

public class ConvertServerMessageFunction<T> implements Function<T, ServerMessage> {
    private MessageCache mMessageCache;
    private String mModuleName;

    public ConvertServerMessageFunction(MessageCache messageCache, String moduleName) {
        mMessageCache = messageCache;
        mModuleName = moduleName;
    }

    @Override
    public ServerMessage apply(T input) {
        ThreadUtil.ensureWorkThread("ConvertServerMessageFunction:" + input.getClass().getSimpleName());
        if (mMessageCache != null) {
            mMessageCache.put(mModuleName, input);
        }
        return new ServerMessage(mModuleName, input);
    }
}
