package cn.hikyson.godeye.core.internal.modules.network;

public class NetworkPerformance {
    public long startTimeMillis;
    public long dnsStartTimeMillis;
    public long dnsEndTimeMillis;
    public long connectStartTimeMillis;
    public long connectEndTimeMillis;
    public long sendHeaderStartTimeMillis;
    public long sendHeaderEndTimeMillis;
    public long sendBodyStartTimeMillis;
    public long sendBodyEndTimeMillis;
    public long receiveHeaderStartTimeMillis;
    public long receiveHeaderEndTimeMillis;
    public long receiveBodyStartTimeMillis;
    public long receiveBodyEndTimeMillis;
    public long endTimeMillis;

    public long dnsTimeMillis() {
        return dnsEndTimeMillis - dnsStartTimeMillis;
    }

    public long connectTimeMillis() {
        return connectEndTimeMillis - connectStartTimeMillis;
    }

    public long sendHeaderTimeMillis() {
        return sendHeaderEndTimeMillis - sendHeaderStartTimeMillis;
    }

    public long sendBodyTimeMillis() {
        return sendBodyEndTimeMillis - sendBodyStartTimeMillis;
    }

    public long receiveHeaderTimeMillis() {
        return receiveHeaderEndTimeMillis - receiveHeaderStartTimeMillis;
    }

    public long receiveBodyTimeMillis() {
        return receiveBodyEndTimeMillis - receiveBodyStartTimeMillis;
    }

    public long totalTimeMillis() {
        return endTimeMillis - startTimeMillis;
    }

    public long otherTimeMillis() {
        return totalTimeMillis() - dnsTimeMillis() - connectTimeMillis() - sendHeaderTimeMillis() - sendBodyTimeMillis();
    }

    @Override
    public String toString() {
        return "NetworkPerformance{" +
                "startTimeMillis=" + startTimeMillis +
                ", dnsStartTimeMillis=" + dnsStartTimeMillis +
                ", dnsEndTimeMillis=" + dnsEndTimeMillis +
                ", connectStartTimeMillis=" + connectStartTimeMillis +
                ", connectEndTimeMillis=" + connectEndTimeMillis +
                ", sendHeaderStartTimeMillis=" + sendHeaderStartTimeMillis +
                ", sendHeaderEndTimeMillis=" + sendHeaderEndTimeMillis +
                ", sendBodyStartTimeMillis=" + sendBodyStartTimeMillis +
                ", sendBodyEndTimeMillis=" + sendBodyEndTimeMillis +
                ", receiveHeaderStartTimeMillis=" + receiveHeaderStartTimeMillis +
                ", receiveHeaderEndTimeMillis=" + receiveHeaderEndTimeMillis +
                ", receiveBodyStartTimeMillis=" + receiveBodyStartTimeMillis +
                ", receiveBodyEndTimeMillis=" + receiveBodyEndTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                '}';
    }


    public static final class NetworkPerformanceBuilder {
        public long startTimeMillis;
        public long dnsStartTimeMillis;
        public long dnsEndTimeMillis;
        public long connectStartTimeMillis;
        public long connectEndTimeMillis;
        public long sendHeaderStartTimeMillis;
        public long sendHeaderEndTimeMillis;
        public long sendBodyStartTimeMillis;
        public long sendBodyEndTimeMillis;
        public long receiveHeaderStartTimeMillis;
        public long receiveHeaderEndTimeMillis;
        public long receiveBodyStartTimeMillis;
        public long receiveBodyEndTimeMillis;
        public long endTimeMillis;

        private NetworkPerformanceBuilder() {
        }

        public static NetworkPerformanceBuilder aNetworkPerformance() {
            return new NetworkPerformanceBuilder();
        }

        public NetworkPerformanceBuilder withStartTieMillis(long startTieMillis) {
            this.startTimeMillis = startTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withDnsStartTieMillis(long dnsStartTieMillis) {
            this.dnsStartTimeMillis = dnsStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withDnsEndTieMillis(long dnsEndTieMillis) {
            this.dnsEndTimeMillis = dnsEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withConnectStartTieMillis(long connectStartTieMillis) {
            this.connectStartTimeMillis = connectStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withConnectEndTieMillis(long connectEndTieMillis) {
            this.connectEndTimeMillis = connectEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withSendHeaderStartTieMillis(long sendHeaderStartTieMillis) {
            this.sendHeaderStartTimeMillis = sendHeaderStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withSendHeaderEndTieMillis(long sendHeaderEndTieMillis) {
            this.sendHeaderEndTimeMillis = sendHeaderEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withSendBodyStartTieMillis(long sendBodyStartTieMillis) {
            this.sendBodyStartTimeMillis = sendBodyStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withSendBodyEndTieMillis(long sendBodyEndTieMillis) {
            this.sendBodyEndTimeMillis = sendBodyEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withReceiveHeaderStartTieMillis(long receiveHeaderStartTieMillis) {
            this.receiveHeaderStartTimeMillis = receiveHeaderStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withReceiveHeaderEndTieMillis(long receiveHeaderEndTieMillis) {
            this.receiveHeaderEndTimeMillis = receiveHeaderEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withReceiveBodyStartTieMillis(long receiveBodyStartTieMillis) {
            this.receiveBodyStartTimeMillis = receiveBodyStartTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withReceiveBodyEndTieMillis(long receiveBodyEndTieMillis) {
            this.receiveBodyEndTimeMillis = receiveBodyEndTieMillis;
            return this;
        }

        public NetworkPerformanceBuilder withEndTieMillis(long endTieMillis) {
            this.endTimeMillis = endTieMillis;
            return this;
        }

        public NetworkPerformance build() {
            NetworkPerformance networkPerformance = new NetworkPerformance();
            networkPerformance.receiveBodyStartTimeMillis = this.receiveBodyStartTimeMillis;
            networkPerformance.sendHeaderStartTimeMillis = this.sendHeaderStartTimeMillis;
            networkPerformance.dnsEndTimeMillis = this.dnsEndTimeMillis;
            networkPerformance.receiveBodyEndTimeMillis = this.receiveBodyEndTimeMillis;
            networkPerformance.sendBodyEndTimeMillis = this.sendBodyEndTimeMillis;
            networkPerformance.connectEndTimeMillis = this.connectEndTimeMillis;
            networkPerformance.receiveHeaderEndTimeMillis = this.receiveHeaderEndTimeMillis;
            networkPerformance.sendHeaderEndTimeMillis = this.sendHeaderEndTimeMillis;
            networkPerformance.dnsStartTimeMillis = this.dnsStartTimeMillis;
            networkPerformance.receiveHeaderStartTimeMillis = this.receiveHeaderStartTimeMillis;
            networkPerformance.sendBodyStartTimeMillis = this.sendBodyStartTimeMillis;
            networkPerformance.connectStartTimeMillis = this.connectStartTimeMillis;
            networkPerformance.endTimeMillis = this.endTimeMillis;
            networkPerformance.startTimeMillis = this.startTimeMillis;
            return networkPerformance;
        }
    }
}
