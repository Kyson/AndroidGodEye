package cn.hikyson.godeye.core.internal.modules.network;

import java.util.Map;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class RequestBaseInfo {
    public String requestId;
    public NetworkInfoRequest networkInfoRequest;
    public NetworkInfoConnection networkInfoConnection;
    public long requestBodySizeByte;
    public long responseBodySizeByte;
    public String resultCode;
    //暂未实现
    public Map<String, String> tags;
    public NetworkSimplePerformance networkSimplePerformance;

    @Override
    public String toString() {
        return "RequestBaseInfo{" +
                "requestId='" + requestId + '\'' +
                ", networkInfoRequest=" + networkInfoRequest +
                ", networkInfoConnection=" + networkInfoConnection +
                ", requestBodySizeByte=" + requestBodySizeByte +
                ", responseBodySizeByte=" + responseBodySizeByte +
                ", resultCode='" + resultCode + '\'' +
                ", tags=" + tags +
                ", networkSimplePerformance=" + networkSimplePerformance +
                '}';
    }

    public static final class RequestBaseInfoBuilder {
        public String requestId;
        public NetworkInfoRequest networkInfoRequest;
        public NetworkInfoConnection networkInfoConnection;
        public long requestBodySizeByte;
        public long responseBodySizeByte;
        public String resultCode;
        public Map<String, String> tags;
        public NetworkSimplePerformance networkSimplePerformance;

        private RequestBaseInfoBuilder() {
        }

        public static RequestBaseInfoBuilder aRequestBaseInfo() {
            return new RequestBaseInfoBuilder();
        }

        public RequestBaseInfoBuilder withRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public RequestBaseInfoBuilder withNetworkInfoRequest(NetworkInfoRequest networkInfoRequest) {
            this.networkInfoRequest = networkInfoRequest;
            return this;
        }

        public RequestBaseInfoBuilder withNetworkInfoConnection(NetworkInfoConnection networkInfoConnection) {
            this.networkInfoConnection = networkInfoConnection;
            return this;
        }

        public RequestBaseInfoBuilder withRequestBodySizeByte(long requestBodySizeByte) {
            this.requestBodySizeByte = requestBodySizeByte;
            return this;
        }

        public RequestBaseInfoBuilder withResponseBodySizeByte(long responseBodySizeByte) {
            this.responseBodySizeByte = responseBodySizeByte;
            return this;
        }

        public RequestBaseInfoBuilder withResultCode(String resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public RequestBaseInfoBuilder withTags(Map<String, String> tags) {
            this.tags = tags;
            return this;
        }

        public RequestBaseInfoBuilder withNetworkSiplePerformance(NetworkSimplePerformance networkSiplePerformance) {
            this.networkSimplePerformance = networkSiplePerformance;
            return this;
        }

        public RequestBaseInfo build() {
            RequestBaseInfo requestBaseInfo = new RequestBaseInfo();
            requestBaseInfo.networkInfoRequest = this.networkInfoRequest;
            requestBaseInfo.networkInfoConnection = this.networkInfoConnection;
            requestBaseInfo.tags = this.tags;
            requestBaseInfo.networkSimplePerformance = this.networkSimplePerformance;
            requestBaseInfo.requestId = this.requestId;
            requestBaseInfo.requestBodySizeByte = this.requestBodySizeByte;
            requestBaseInfo.resultCode = this.resultCode;
            requestBaseInfo.responseBodySizeByte = this.responseBodySizeByte;
            return requestBaseInfo;
        }
    }
}
