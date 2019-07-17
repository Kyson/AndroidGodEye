import React, {Component} from 'react';
import './App.css';
import {Row, Col, Layout} from 'antd'
import 'antd/dist/antd.css';
import AppInfo from "./appinfo/appInfo";
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
import MethodCanary from "./methodcanary/methodcanary";
import Mock from "./MockData";
import {ToastContainer, toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class App extends Component {

    constructor(props) {
        super(props);
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
        this._setCanRefresh = this._setCanRefresh.bind(this);
        this._getModuleRef = this._getModuleRef.bind(this);
        this.canRefresh = true;
        this.mock = new Mock();
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        this.onWsOpenCallback = () => {
            globalWs.sendMessage('{"moduleName": "clientOnline"}')
        };
        globalWs.registerCallback(this.onWsOpenCallback);
        globalWs.start();
        // this.mock.start(this._onReceiveMessage);
    }

    componentWillUnmount() {
        globalWs.unregisterCallback(this.onWsOpenCallback)
    }

    _getModuleRef(moduleName) {
        const moduleMap = {
            "appInfo": this.refs.appInfo,
            "startupInfo": this.refs.startupInfo,
            "fpsInfo": this.refs.fpsInfo,
            "cpuInfo": this.refs.cpuInfo,
            "heapInfo": this.refs.heapInfo,
            "batteryInfo": this.refs.batteryInfo,
            "ramInfo": this.refs.ramInfo,
            "pssInfo": this.refs.pssInfo,
            "pageLifecycle": this.refs.pageLifecycle,
            "trafficInfo": this.refs.trafficInfo,
            "crashInfo": this.refs.crashInfo,
            "blockInfo": this.refs.blockInfo,
            "networkInfo": this.refs.networkInfo,
            "threadInfo": this.refs.threadInfo,
            "leakInfo": this.refs.leakInfo,
            "methodCanary": this.refs.methodCanary
        };
        return moduleMap[moduleName];
    }

    _onReceiveMessage(moduleName, payload) {
        if (!this.canRefresh) {
            return;
        }
        this.refs.refreshStatus.refresh(new Date());
        if ("MethodCanaryStatus" === moduleName) {
            this.refs.methodCanary.refreshStatus(payload);
        } else {
            this._getModuleRef(moduleName).refresh(payload);
        }
    }

    _setCanRefresh(canRefresh) {
        this.canRefresh = canRefresh;
    }

    render() {
        return (
            <Layout>
                <Layout.Content style={{marginLeft: 16, marginRight: 16}}>
                    <Row align="top" style={{backgroundColor: '#93c756', marginLeft: -16, marginRight: -16}}>
                        <Col span={24}>
                            <AppInfo ref="appInfo" globalWs={globalWs}/>
                        </Col>
                    </Row>
                    <ToastContainer autoClose={1200} position={toast.POSITION.TOP_LEFT}/>
                    <Row gutter={16} align="top" style={{textAlign: 'right', marginTop: 16}}>
                        <Col span={24}>
                            <RefreshStatus ref="refreshStatus" setCanRefresh={this._setCanRefresh}/>
                        </Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}>
                            <MethodCanary ref="methodCanary" globalWs={globalWs}/>
                        </Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={15}>
                            <Row gutter={16} align="top">
                                <Col span={8}>
                                    <Fps ref="fpsInfo"/>
                                </Col>
                                <Col span={16}>
                                    <Startup ref="startupInfo"/>
                                </Col>
                            </Row>
                            <Row gutter={16} align="top" style={{marginTop: 16}}>
                                <Col span={8}> <Ram ref="ramInfo"/>
                                </Col>
                                <Col span={8}> <Pss ref="pssInfo"/>
                                </Col>
                                <Col span={8}> <BatteryInfo ref="batteryInfo"/>
                                </Col>
                            </Row>
                        </Col>
                        <Col span={9}>
                            <Crash ref="crashInfo"/>
                        </Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={8}> <Cpu ref="cpuInfo"/>
                        </Col>
                        <Col span={8}> <Heap ref="heapInfo"/>
                        </Col>
                        <Col span={8}><Traffic ref="trafficInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={12}><Pageload ref="pageLifecycle"/></Col>
                        <Col span={12}><MemoryLeak ref="leakInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}><Network ref="networkInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}><Block ref="blockInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}><Thread ref="threadInfo"/></Col>
                    </Row>
                </Layout.Content>
                <Layout.Footer style={{textAlign: "center"}}>
                    <span>Powered by <a href="https://github.com/Kyson/AndroidGodEye"
                                        target="_blank" rel="noopener noreferrer">AndroidGodEye</a></span>
                </Layout.Footer>
            </Layout>
        );
    }
}

export default App;
