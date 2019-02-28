package cn.hikyson.android.godeye.toolbox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfoConnection;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfoRequest;
import cn.hikyson.godeye.core.internal.modules.network.NetworkPerformance;
import cn.hikyson.godeye.core.internal.modules.network.NetworkSimplePerformance;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.core.utils.L;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class OkNetworkCollector extends EventListener {
    private Producer<RequestBaseInfo> mRequestBaseInfoProducer;
    private Map<Call, CachedCallInfo> mCachedCallInfoMap;

    public static class CachedCallInfo {
        public NetworkPerformance.NetworkPerformanceBuilder networkPerformanceBuilder;
        public RequestBaseInfo.RequestBaseInfoBuilder requestBaseInfoBuilder;
        public Response response;

        public CachedCallInfo(NetworkPerformance.NetworkPerformanceBuilder networkPerformanceBuilder, RequestBaseInfo.RequestBaseInfoBuilder requestBaseInfoBuilder) {
            this.networkPerformanceBuilder = networkPerformanceBuilder;
            this.requestBaseInfoBuilder = requestBaseInfoBuilder;
        }
    }

    public OkNetworkCollector(Producer<RequestBaseInfo> producer) {
        this.mRequestBaseInfoProducer = producer;
        this.mCachedCallInfoMap = new ConcurrentHashMap<>();
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        CachedCallInfo cachedCallInfo = new CachedCallInfo(NetworkPerformance.NetworkPerformanceBuilder.aNetworkPerformance(), RequestBaseInfo.RequestBaseInfoBuilder.aRequestBaseInfo());
        cachedCallInfo.requestBaseInfoBuilder.withRequestId(callToString(call)).withNetworkInfoRequest(new NetworkInfoRequest(call.request().url().toString(), call.request().method()));
        cachedCallInfo.networkPerformanceBuilder.withStartTieMillis(System.currentTimeMillis());
        mCachedCallInfoMap.put(call, cachedCallInfo);
        L.d(String.format("[%s]:callStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withDnsStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:dnsStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withDnsEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:dnsEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withConnectStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withConnectEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol,
                              IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withConnectEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectFailed", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        String protocol = connection.protocol().toString();
        Handshake handshake = connection.handshake();
        String cipherSuite = "";
        String tlsVersion = "";
        if (handshake != null) {
            cipherSuite = handshake.cipherSuite().javaName();
            tlsVersion = handshake.tlsVersion().javaName();
        }
        String localIp = connection.socket().getLocalAddress().getHostAddress();
        int localPort = connection.socket().getLocalPort();
        String remoteIp = connection.socket().getInetAddress().getHostAddress();
        int remotePort = connection.socket().getPort();
        cachedCallInfo.requestBaseInfoBuilder.withNetworkInfoConnection(
                new NetworkInfoConnection(protocol, cipherSuite, tlsVersion, localIp, localPort, remoteIp, remotePort));
        L.d(String.format("[%s]:connectionAcquired", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withSendHeaderStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestHeadersStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withSendHeaderEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestHeadersEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withSendBodyStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestBodyStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.requestBaseInfoBuilder.withRequestBodySizeByte(byteCount);
        cachedCallInfo.networkPerformanceBuilder.withSendBodyEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestBodyEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withReceiveHeaderStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseHeadersStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.response = response;
        cachedCallInfo.networkPerformanceBuilder.withReceiveHeaderEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseHeadersEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.networkPerformanceBuilder.withReceiveBodyStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseBodyStart", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.requestBaseInfoBuilder.withResponseBodySizeByte(byteCount);
        cachedCallInfo.networkPerformanceBuilder.withReceiveBodyEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseBodyEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        if (cachedCallInfo.response != null) {
            cachedCallInfo.requestBaseInfoBuilder.withResultCode(String.valueOf(cachedCallInfo.response.code()));
        } else {
            cachedCallInfo.requestBaseInfoBuilder.withResultCode("ResponseNull");
        }
        cachedCallInfo.networkPerformanceBuilder.withEndTieMillis(System.currentTimeMillis());
        cachedCallInfo.requestBaseInfoBuilder.withNetworkSiplePerformance(new NetworkSimplePerformance(cachedCallInfo.networkPerformanceBuilder.build()));
        L.d(String.format("[%s]:callEnd", cachedCallInfo.requestBaseInfoBuilder.requestId));
        this.mRequestBaseInfoProducer.produce(cachedCallInfo.requestBaseInfoBuilder.build());
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        CachedCallInfo cachedCallInfo = mCachedCallInfoMap.get(call);
        cachedCallInfo.requestBaseInfoBuilder.withResultCode("IOException");
        cachedCallInfo.networkPerformanceBuilder.withEndTieMillis(System.currentTimeMillis());
        cachedCallInfo.requestBaseInfoBuilder.withNetworkSiplePerformance(new NetworkSimplePerformance(cachedCallInfo.networkPerformanceBuilder.build()));
        L.d(String.format("[%s]:callFailed", cachedCallInfo.requestBaseInfoBuilder.requestId));
        this.mRequestBaseInfoProducer.produce(cachedCallInfo.requestBaseInfoBuilder.build());
    }

    private static String callToString(Call call) {
        if (call == null) {
            return "[Unknown]";
        }
        return String.format("[%s:%s]", String.valueOf(call.hashCode()), String.valueOf(call.request().url()));
    }

}
