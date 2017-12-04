package cn.hikyson.godeye.core.internal.modules.network;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class RequestBaseInfo {
    public long startTimeMillis;
    public long endTimeMillis;
    public long respBodySizeByte;
    public String url;

    public RequestBaseInfo(long startTimeMillis, long endTimeMillis, long respBodySizeByte, String url) {
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.respBodySizeByte = respBodySizeByte;
        this.url = url;
    }

    @Override
    public String toString() {
        return "RequestBaseInfo{" +
                "startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                ", respBodySizeByte=" + respBodySizeByte +
                ", url='" + url + '\'' +
                '}';
    }
}
