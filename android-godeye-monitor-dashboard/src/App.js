import React, {Component} from 'react';
import './App.css';
import AppInfo from "./appinfo/appInfo";
import {Row, Col, Grid} from 'react-bootstrap'
import globalWs from './communication/websocket'
import BatteryInfo from "./batteryinfo/batteryInfo";
import Startup from "./startup/startup";
import Ram from "./ram/ram";
import Pss from "./pss/pss";
import Fps from "./fps/fps";
import Cpu from "./cpu/cpu";
import Heap from "./heap/heap";
import Pageload from "./pageload/pageload";
import Traffic from "./traffic/traffic";
import Crash from "./crash/crash";
import Block from "./block/block";
import Network from "./network/network";
import Thread from "./thread/thread";
import MemoryLeak from "./memoryleak/memoryLeak";
import RefreshStatus from "./refreshstatus/refreshStatus";

class App extends Component {

    constructor(props) {
        super(props);
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
        this.refreshMock = this.refreshMock.bind(this);
        this._setCanRefresh = this._setCanRefresh.bind(this);
        this.canRefresh = true;
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        globalWs.start();
        // setInterval(this.refreshMock, 2000);
    }

    _onReceiveMessage(moduleName, payload) {
        if (!this.canRefresh) {
            return;
        }
        this.refs.refreshStatus.refresh(new Date());
        if ("appInfo" === moduleName) {
            this.refs.appInfo.refresh(payload);
            return;
        }
        if ("startupInfo" === moduleName) {
            this.refs.startupInfo.refresh(payload);
            return;
        }
        if ("fpsInfo" === moduleName) {
            this.refs.fpsInfo.refresh(payload);
            return;
        }
        if ("cpuInfo" === moduleName) {
            this.refs.cpuInfo.refresh(payload);
            return;
        }
        if ("heapInfo" === moduleName) {
            this.refs.heapInfo.refresh(payload);
            return;
        }
        if ("batteryInfo" === moduleName) {
            this.refs.batteryInfo.refresh(payload);
            return;
        }
        if ("ramInfo" === moduleName) {
            this.refs.ramInfo.refresh(payload);
            return;
        }
        if ("pssInfo" === moduleName) {
            this.refs.pssInfo.refresh(payload);
            return;
        }
        if ("pageloadInfo" === moduleName) {
            this.refs.pageloadInfo.refresh(payload);
            return;
        }
        if ("trafficInfo" === moduleName) {
            this.refs.trafficInfo.refresh(payload);
            return;
        }
        if ("crashInfo" === moduleName) {
            this.refs.crashInfo.refresh(payload);
            return;
        }
        if ("blockInfo" === moduleName) {
            this.refs.blockInfo.refresh(payload);
            return;
        }
        if ("networkInfo" === moduleName) {
            this.refs.networkInfo.refresh(payload);
            return;
        }
        if ("threadInfo" === moduleName) {
            this.refs.threadInfo.refresh(payload);
            return;
        }
        if ("leakInfo" === moduleName) {
            this.refs.leakInfo.refresh(payload);
            return;
        }
    }

    _setCanRefresh(canRefresh) {
        this.canRefresh = canRefresh;
    }

    render() {
        return (

            <div className="App">
                <Grid>
                    <Row style={{marginBottom: 30}}>
                        <Col md={12}><AppInfo ref="appInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            <RefreshStatus ref="refreshStatus" setCanRefresh={this._setCanRefresh}/>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={9}> <Startup ref="startupInfo"/>
                        </Col>
                        <Col md={3}> <Fps ref="fpsInfo"/>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={4}> <Ram ref="ramInfo"/>
                        </Col>
                        <Col md={4}> <Pss ref="pssInfo"/>
                        </Col>
                        <Col md={4}> <BatteryInfo ref="batteryInfo"/>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6}> <Cpu ref="cpuInfo"/>
                        </Col>
                        <Col md={6}> <Heap ref="heapInfo"/>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6}><Traffic ref="trafficInfo"/></Col>
                        <Col md={6}><Crash ref="crashInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={12}><Pageload ref="pageloadInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={6}><Block ref="blockInfo"/></Col>
                        <Col md={6}><Network ref="networkInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={12}><Thread ref="threadInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={12}><MemoryLeak ref="leakInfo"/></Col>
                    </Row>
                </Grid>
            </div>

        );
    }

    refreshMock() {
        this._onReceiveMessage("appInfo", {
            appName: "I am Name",
            labels: ["label1", "label2", "label3"]
        });
        this._onReceiveMessage("startupInfo", {
            startupType: "cold",
            startupTime: 1003
        });
        this._onReceiveMessage("batteryInfo", {
            level: 24,
            status: "ok",
            plugged: "plugged",
            present: "present",
            health: "health",
            voltage: "voltage",
            temperature: "temperature",
            technology: "technology",
            scale: 100,
        });
        this._onReceiveMessage("cpuInfo", {
            totalUseRatio: 0.56,
            appCpuRatio: 0.12,
            userCpuRatio: 0.23,
            sysCpuRatio: 0.09
        });
        this._onReceiveMessage("heapInfo", {
            freeMemKb: 1024 * 520,
            allocatedKb: 1024 * 1520,
            maxMemKb: 1024 * 6000
        });
        this._onReceiveMessage("ramInfo", {
            totalMemKb: 1024 * 1024 * 3,
            availMemKb: 1024 * 1024 * 1.5
        });
        this._onReceiveMessage("pssInfo", {
            totalPssKb: 1024 * 300,
            dalvikPssKb: 1024 * 125,
            nativePssKb: 1024 * 200,
            otherPssKb: 1024 * 7,
        });
        this._onReceiveMessage("fpsInfo", {
            currentFps: "12",
            systemFps: "34"
        });
        this._onReceiveMessage("pageloadInfo", {
            pageId: "11",
            pageName: "ActivityA",
            pageStatus: "created",
            pageStatusTime: "2018-03-00",
            loadTimeInfo: {
                createTime: 100,
                didDrawTime: 120,
                loadTime: 150
            }
        });
        this._onReceiveMessage("crashInfo", {
            timestampMillis: new Date().getMilliseconds(),
            throwableMessage: "throwableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessagethrowableMessage",
            throwableStacktrace: ["1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111", "1111"]
        });
        this._onReceiveMessage("blockInfo", {
            blockTime: 123,
            blockBaseinfo: {df: "sdf", vvv: "1312", bb: ["fewefwf", "fwewfe"]}
        });
        this._onReceiveMessage("networkInfo", {
            networkInfoConnection: {
                cipherSuite: "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                localIp: "10.0.2.15",
                localPort: 53010,
                protocol: "h2",
                remoteIp: "117.184.207.147",
                remotePort: 443,
                tlsVersion: "TLSv1.2"
            },
            networkInfoRequest: {
                method: "https://www.trip.com/",
                url: "GET"
            },
            networkSimplePerformance: {
                connectTimeMillis: 1345,
                dnsTimeMillis: 41,
                receiveBodyTimeMillis: 389,
                receiveHeaderTimeMillis: 713,
                sendBodyTimeMillis: 0,
                sendHeaderTimeMillis: 11,
                totalTimeMillis: 2527
            },
            requestBodySizeByte: 0,
            requestId: "117910124:https://www.trip.com/",
            responseBodySizeByte: 14133,
            resultCode: "200"
        });
        this._onReceiveMessage("trafficInfo", {
            rxTotalRate: 56,
            txTotalRate: 48,
            rxUidRate: 34,
            txUidRate: 42
        });
        this._onReceiveMessage("leakInfo", {
            referenceKey: "referenceKey",
            leakTime: "leakTime",
            leakObjectName: "leakObjectName",
            statusSummary: "statusSummary",
            pathToGcRoot: ["leakStack", "leakStack", "leakStack", "leakStack", "leakStack"]
        });
        this._onReceiveMessage("threadInfo", [
            {
                id: 1,
                name: "name",
                state: "state",
                deadlock: "deadlock",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
            },
            {
                id: 1,
                name: "name",
                state: "state",
                deadlock: "deadlock",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
            }, {
                id: 1,
                name: "name",
                state: "state",
                deadlock: "deadlock",
                priority: "priority",
                deamon: "deamon",
                isAlive: "isAlive",
                isInterrupted: "isInterrupted",
            }
        ]);
    }

}

export default App;
