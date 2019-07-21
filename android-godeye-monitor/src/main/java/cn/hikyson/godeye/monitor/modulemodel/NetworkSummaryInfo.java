package cn.hikyson.godeye.monitor.modulemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hikyson.android.godeye.toolbox.network.HttpContent;
import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.network.NetworkTime;

public class NetworkSummaryInfo {
    public String summary;
    public boolean isSuccessful;
    public String message;
    public long totalTime;
    public List<TimePair> networkTime;
    public Content networkContent;
    public Map<String, Object> extraInfo;

    public static class TimePair {
        public String name;
        public long time;

        public TimePair(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }

    public static class Content {
        public String networkType;
        public String requestContent;
        public String responseContent;

        public Content(String networkType, String requestContent, String responseContent) {
            this.networkType = networkType;
            this.requestContent = requestContent;
            this.responseContent = responseContent;
        }
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

    private static Content convertToNetworkContent(NetworkContent networkContent) {
        if (networkContent != null) {
            if (networkContent instanceof HttpContent) {
                return new Content(networkContent.getClass().getSimpleName(), ((HttpContent) networkContent).httpRequest.getStandardFormat(), ((HttpContent) networkContent).httpResponse.getStandardFormat());
            }
            return new Content(networkContent.getClass().getSimpleName(), "Request unknown network protocol", "Response unknown network protocol");
        }
        return new Content("NULL", "NULL", "NULL");
    }

    private static String convertToSummary(NetworkContent networkContent) {
        if (networkContent != null) {
            if (networkContent instanceof HttpContent) {
                return ((HttpContent) networkContent).httpRequest.method + " " + ((HttpContent) networkContent).httpRequest.url;
            }
            return "Summary Unknown network protocol";
        }
        return "NULL";
    }

    public static NetworkSummaryInfo convert(NetworkInfo networkInfo) {
        NetworkSummaryInfo networkSummaryInfo = new NetworkSummaryInfo();
        networkSummaryInfo.isSuccessful = networkInfo.isSuccessful;
        networkSummaryInfo.message = networkInfo.message;
        if (networkInfo.networkContent instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) networkInfo.networkContent;
            if (httpContent.httpResponse != null && networkInfo.isSuccessful) {
                networkSummaryInfo.message = httpContent.httpResponse.message;
                if (!isSuccessful(httpContent.httpResponse.code)) {
                    networkSummaryInfo.isSuccessful = false;
                }
            }
            networkSummaryInfo.networkContent = convertToNetworkContent(httpContent);
        } else {
            networkSummaryInfo.networkContent = new Content(networkInfo.getClass().getSimpleName(), "Request unknown network protocol", "Response unknown network protocol");
        }
        networkSummaryInfo.summary = networkInfo.summary;
        networkSummaryInfo.totalTime = networkInfo.networkTime == null ? 0 : networkInfo.networkTime.totalTimeMillis;
        networkSummaryInfo.networkTime = convertToTimePairs(networkInfo.networkTime);
        networkSummaryInfo.extraInfo = networkInfo.extraInfo;
        return networkSummaryInfo;
    }

    private static boolean isSuccessful(int code) {
        return code >= 200 && code < 300;
    }

    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int HTTP_PERM_REDIRECT = 308;
    private static final int HTTP_MULT_CHOICE = 300;
    private static final int HTTP_MOVED_PERM = 301;
    private static final int HTTP_MOVED_TEMP = 302;
    private static final int HTTP_SEE_OTHER = 303;

    private static boolean isRedirect(int code) {
        switch (code) {
            case HTTP_PERM_REDIRECT:
            case HTTP_TEMP_REDIRECT:
            case HTTP_MULT_CHOICE:
            case HTTP_MOVED_PERM:
            case HTTP_MOVED_TEMP:
            case HTTP_SEE_OTHER:
                return true;
            default:
                return false;
        }
    }
}
