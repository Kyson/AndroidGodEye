package cn.hikyson.godeye.monitor.server;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Function;

public class ConvertServerMessageFunction<T> implements Function<T, ServerMessage> {
    private String mModuleName;

    ConvertServerMessageFunction(String moduleName) {
        mModuleName = moduleName;
    }

    @Override
    public ServerMessage apply(T input) {
        ThreadUtil.ensureWorkThread("ConvertServerMessageFunction:" + input.getClass().getSimpleName());
        return new ServerMessage(mModuleName, input);
    }
}
