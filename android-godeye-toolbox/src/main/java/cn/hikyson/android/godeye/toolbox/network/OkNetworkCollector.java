package cn.hikyson.android.godeye.toolbox.network;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.network.NetworkSimplePerformance;
import cn.hikyson.godeye.core.utils.L;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class OkNetworkCollector extends EventListener {
    private Producer<NetworkInfo> mNetworkInfoProducer;
    private NetworkInfo<HttpContent> mNetworkInfo;
    private Response response;
    private long mCallStartTimeMillis;
    private long mDnsStartTimeMillis;
    private long mConnectionStartTimeMillis;
    private long mRequestHeadersStartTimeMillis;
    private long mRequestBodyStartTimeMillis;
    private long mResponseHeadersStartTimeMillis;
    private long mResponseBodyStartTimeMillis;

    public OkNetworkCollector(Producer<NetworkInfo> producer) {
        this.mNetworkInfoProducer = producer;
        this.mNetworkInfo = new NetworkInfo<>();
        this.mNetworkInfo.networkTimeMillisMap = new LinkedHashMap<>();
        this.mNetworkInfo.extraInfo = new HashMap<>();
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        mCallStartTimeMillis = System.currentTimeMillis();
        mNetworkInfo.networkContent.httpRequest.method = call.request().method();
        mNetworkInfo.networkContent.httpRequest.url = call.request().url().toString();
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        mDnsStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        mNetworkInfo.networkTimeMillisMap.put("DnsTime", System.currentTimeMillis() - mDnsStartTimeMillis);
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        mConnectionStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        mNetworkInfo.networkTimeMillisMap.put("ConnectTime", System.currentTimeMillis() - mConnectionStartTimeMillis);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol,
                              IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        mNetworkInfo.networkTimeMillisMap.put("ConnectTime", System.currentTimeMillis() - mConnectionStartTimeMillis);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        Handshake handshake = connection.handshake();
        String cipherSuite = "";
        String tlsVersion = "";
        if (handshake != null) {
            cipherSuite = handshake.cipherSuite().javaName();
            tlsVersion = handshake.tlsVersion().javaName();
        }
        Socket socket = connection.socket();
        int localPort = socket.getLocalPort();
        int remotePort = socket.getPort();
        String localIp = "";
        String remoteIp = "";
        InetAddress localAddress = socket.getLocalAddress();
        if (localAddress != null) {
            localIp = localAddress.getHostAddress();
        }
        InetAddress remoteAddress = socket.getInetAddress();
        if (remoteAddress != null) {
            remoteIp = remoteAddress.getHostAddress();
        }
        mNetworkInfo.networkContent.httpRequest.protocol = connection.protocol().toString();
        mNetworkInfo.extraInfo.put("cipherSuite", cipherSuite);
        mNetworkInfo.extraInfo.put("tlsVersion", tlsVersion);
        mNetworkInfo.extraInfo.put("localIp", localIp);
        mNetworkInfo.extraInfo.put("localPort", localPort);
        mNetworkInfo.extraInfo.put("remoteIp", remoteIp);
        mNetworkInfo.extraInfo.put("remotePort", remotePort);
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        mRequestHeadersStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        mNetworkInfo.networkTimeMillisMap.put("RequestHeadersTime", System.currentTimeMillis() - mRequestHeadersStartTimeMillis);
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        mRequestBodyStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        mNetworkInfo.networkTimeMillisMap.put("RequestBodyTime", System.currentTimeMillis() - mRequestBodyStartTimeMillis);
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        mResponseHeadersStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        this.response = response;
        mNetworkInfo.networkTimeMillisMap.put("ResponseHeadersTime", System.currentTimeMillis() - mResponseHeadersStartTimeMillis);
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        mResponseBodyStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        mNetworkInfo.networkTimeMillisMap.put("ResponseBodyTime", System.currentTimeMillis() - mResponseBodyStartTimeMillis);
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        mNetworkInfo.networkTimeMillisMap.put("TotalTime", System.currentTimeMillis() - mCallStartTimeMillis);
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
        mNetworkInfo.networkTimeMillisMap.put("TotalTime", System.currentTimeMillis() - mCallStartTimeMillis);

        requestBaseInfoBuilder.withResultCode("IOException");
        networkPerformanceBuilder.withEndTieMillis(System.currentTimeMillis());
        requestBaseInfoBuilder.withNetworkSiplePerformance(new NetworkSimplePerformance(networkPerformanceBuilder.build()));
        L.d(String.format("[%s]:callFailed", requestBaseInfoBuilder.requestId));
        this.mRequestBaseInfoProducer.produce(requestBaseInfoBuilder.build());
    }
}
