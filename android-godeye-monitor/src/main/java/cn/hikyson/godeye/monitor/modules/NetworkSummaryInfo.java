package cn.hikyson.godeye.monitor.modules;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.network.NetworkTime;

@Keep
public class NetworkSummaryInfo implements Serializable {
    public String summary;
    public boolean isSuccessful;
    public String message;
    public long totalTime;
    public List<TimePair> networkTime;
    public Content networkContent;
    public Map<String, Object> extraInfo;

    @Keep
    public static class TimePair implements Serializable {
        public String name;
        public long time;

        TimePair(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }

    @Keep
    public static class Content implements Serializable {
        public String networkType;
        public String requestContent;
        public String responseContent;

        public Content(String networkType, String requestContent, String responseContent) {
            this.networkType = networkType;
            this.requestContent = requestContent;
            this.responseContent = responseContent;
        }
    }

    public static NetworkSummaryInfo convert(NetworkInfo networkInfo) {
        NetworkSummaryInfo networkSummaryInfo = new NetworkSummaryInfo();
        networkSummaryInfo.isSuccessful = networkInfo.isSuccessful;
        networkSummaryInfo.message = networkInfo.message;
        networkSummaryInfo.networkContent = convertNetworkContentToSummaryContent(networkInfo.networkContent);
        networkSummaryInfo.summary = networkInfo.summary;
        networkSummaryInfo.totalTime = networkInfo.networkTime == null ? 0 : networkInfo.networkTime.totalTimeMillis;
        networkSummaryInfo.networkTime = convertToTimePairs(networkInfo.networkTime);
        networkSummaryInfo.extraInfo = networkInfo.extraInfo;
        return networkSummaryInfo;
    }

    private static List<TimePair> convertToTimePairs(NetworkTime networkTime) {
        List<TimePair> networkTimeList = new ArrayList<>();
        if (networkTime == null || networkTime.networkTimeMillisMap == null) {
            return networkTimeList;
        }
        for (Map.Entry<String, Long> entry : networkTime.networkTimeMillisMap.entrySet()) {
            networkTimeList.add(new TimePair(entry.getKey(), entry.getValue()));
        }
        return networkTimeList;
    }

    private static Content convertNetworkContentToSummaryContent(NetworkContent networkContent) {
        if (networkContent != null) {
            return new Content(networkContent.getClass().getSimpleName(), networkContent.requestToString(), networkContent.responseToString());
        }
        return new Content("NULL", "NULL", "NULL");
    }
}
