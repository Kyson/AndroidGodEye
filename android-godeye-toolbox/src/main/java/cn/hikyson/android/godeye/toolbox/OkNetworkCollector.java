package cn.hikyson.android.godeye.toolbox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
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
    private NetworkPerformance.NetworkPerformanceBuilder networkPerformanceBuilder;
    private RequestBaseInfo.RequestBaseInfoBuilder requestBaseInfoBuilder;
    private Response response;

    public OkNetworkCollector(Producer<RequestBaseInfo> producer) {
        this.mRequestBaseInfoProducer = producer;
        networkPerformanceBuilder = NetworkPerformance.NetworkPerformanceBuilder.aNetworkPerformance();
        requestBaseInfoBuilder = RequestBaseInfo.RequestBaseInfoBuilder.aRequestBaseInfo();
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        requestBaseInfoBuilder.withRequestId(callToString(call)).withNetworkInfoRequest(new NetworkInfoRequest(call.request().url().toString(), call.request().method()));
        networkPerformanceBuilder.withStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:callStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        networkPerformanceBuilder.withDnsStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:dnsStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        networkPerformanceBuilder.withDnsEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:dnsEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        networkPerformanceBuilder.withConnectStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        networkPerformanceBuilder.withConnectEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol,
                              IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        networkPerformanceBuilder.withConnectEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:connectFailed", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
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
        requestBaseInfoBuilder.withNetworkInfoConnection(
                new NetworkInfoConnection(protocol, cipherSuite, tlsVersion, localIp, localPort, remoteIp, remotePort));
        L.d(String.format("[%s]:connectionAcquired", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        networkPerformanceBuilder.withSendHeaderStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestHeadersStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        networkPerformanceBuilder.withSendHeaderEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestHeadersEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        networkPerformanceBuilder.withSendBodyStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestBodyStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        requestBaseInfoBuilder.withRequestBodySizeByte(byteCount);
        networkPerformanceBuilder.withSendBodyEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:requestBodyEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        networkPerformanceBuilder.withReceiveHeaderStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseHeadersStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        this.response = response;
        networkPerformanceBuilder.withReceiveHeaderEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseHeadersEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        networkPerformanceBuilder.withReceiveBodyStartTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseBodyStart", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        requestBaseInfoBuilder.withResponseBodySizeByte(byteCount);
        networkPerformanceBuilder.withReceiveBodyEndTieMillis(System.currentTimeMillis());
        L.d(String.format("[%s]:responseBodyEnd", requestBaseInfoBuilder.requestId));
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        if (response != null) {
            requestBaseInfoBuilder.withResultCode(String.valueOf(response.code()));
        } else {
            requestBaseInfoBuilder.withResultCode("ResponseNull");
        }
        networkPerformanceBuilder.withEndTieMillis(System.currentTimeMillis());
        requestBaseInfoBuilder.withNetworkSiplePerformance(new NetworkSimplePerformance(networkPerformanceBuilder.build()));
        L.d(String.format("[%s]:callEnd", requestBaseInfoBuilder.requestId));
        this.mRequestBaseInfoProducer.produce(requestBaseInfoBuilder.build());
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        requestBaseInfoBuilder.withResultCode("IOException");
        networkPerformanceBuilder.withEndTieMillis(System.currentTimeMillis());
        requestBaseInfoBuilder.withNetworkSiplePerformance(new NetworkSimplePerformance(networkPerformanceBuilder.build()));
        L.d(String.format("[%s]:callFailed", requestBaseInfoBuilder.requestId));
        this.mRequestBaseInfoProducer.produce(requestBaseInfoBuilder.build());
    }

    private static String callToString(Call call) {
        if (call == null) {
            return "[Unknown]";
        }
        return String.format("%s:%s", String.valueOf(call.hashCode()), String.valueOf(call.request().url()));
    }

}
