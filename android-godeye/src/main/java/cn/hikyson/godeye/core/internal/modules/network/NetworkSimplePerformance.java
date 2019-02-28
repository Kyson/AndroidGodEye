package cn.hikyson.godeye.core.internal.modules.network;

public class NetworkSimplePerformance {
    public long dnsTimeMillis;
    public long connectTimeMillis;
    public long sendHeaderTimeMillis;
    public long sendBodyTimeMillis;
    public long receiveHeaderTimeMillis;
    public long receiveBodyTimeMillis;
    public long totalTimeMillis;

    public NetworkSimplePerformance(NetworkPerformance networkPerformance) {
        this.dnsTimeMillis = networkPerformance.dnsTimeMillis();
        this.connectTimeMillis = networkPerformance.connectTimeMillis();
        this.sendHeaderTimeMillis = networkPerformance.sendHeaderTimeMillis();
        this.sendBodyTimeMillis = networkPerformance.sendBodyTimeMillis();
        this.receiveHeaderTimeMillis = networkPerformance.receiveHeaderTimeMillis();
        this.receiveBodyTimeMillis = networkPerformance.receiveBodyTimeMillis();
        this.totalTimeMillis = networkPerformance.totalTimeMillis();
    }

    @Override
    public String toString() {
        return "NetworkSimplePerformance{" +
                "dnsTimeMillis=" + dnsTimeMillis +
                ", connectTimeMillis=" + connectTimeMillis +
                ", sendHeaderTimeMillis=" + sendHeaderTimeMillis +
                ", sendBodyTimeMillis=" + sendBodyTimeMillis +
                ", receiveHeaderTimeMillis=" + receiveHeaderTimeMillis +
                ", receiveBodyTimeMillis=" + receiveBodyTimeMillis +
                ", totalTimeMillis=" + totalTimeMillis +
                '}';
    }
}
