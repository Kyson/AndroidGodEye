package cn.hikyson.godeye.monitor.driver;

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
        mMessageCache.put(mModuleName, input);
        return new ServerMessage(mModuleName, input);
    }
}
