package cn.hikyson.godeye.core.internal.exception;

/**
 * 无效数据的异常
 * Created by kysonchao on 2017/12/16.
 */
public class GodEyeInvalidDataException extends RuntimeException {
    public GodEyeInvalidDataException() {
    }

    public GodEyeInvalidDataException(String message) {
        super(message);
    }

    public GodEyeInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public GodEyeInvalidDataException(Throwable cause) {
        super(cause);
    }
}
