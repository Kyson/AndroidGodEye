package cn.hikyson.godeye.core.internal.modules.network;

public class NetworkInfoConnection {
    public String protocol;
    public String cipherSuite;
    public String tlsVersion;
    public String localIp;
    public int localPort;
    public String remoteIp;
    public int remotePort;

    public NetworkInfoConnection(String protocol, String cipherSuite, String tlsVersion, String localIp, int localPort, String remoteIp, int remotePort) {
        this.protocol = protocol;
        this.cipherSuite = cipherSuite;
        this.tlsVersion = tlsVersion;
        this.localIp = localIp;
        this.localPort = localPort;
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    @Override
    public String toString() {
        return "NetworkInfoConnection{" +
                "protocol='" + protocol + '\'' +
                ", cipherSuite='" + cipherSuite + '\'' +
                ", tlsVersion='" + tlsVersion + '\'' +
                ", localIp='" + localIp + '\'' +
                ", localPort='" + localPort + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", remotePort='" + remotePort + '\'' +
                '}';
    }
}
