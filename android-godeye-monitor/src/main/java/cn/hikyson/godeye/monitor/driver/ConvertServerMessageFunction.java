package cn.hikyson.godeye.monitor.driver;

import java.util.Map;

import cn.hikyson.godeye.monitor.modulemodel.AppInfo;
import io.reactivex.functions.Function;

public class ConvertServerMessageFunction<T> implements Function<T,ServerMessage> {
    private Map<String, Object> mCachedMessages;
    private String mModuleName;

    public ConvertServerMessageFunction(Map<String, Object> cachedMessages, String moduleName) {
        mCachedMessages = cachedMessages;
        mModuleName = moduleName;
    }

    @Override
    public ServerMessage apply(T input) {
        mCachedMessages.put(mModuleName, input);
        return new ServerMessage(mModuleName, input);
    }
}
