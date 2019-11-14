/* eslint-disable react/no-string-refs */
import React, {Component} from 'react';
import './App.css';
import {Row, Col, Layout} from 'antd'
import 'antd/dist/antd.css';
import AppInfo from "./appinfo/appInfo";
import globalWs from './communication/websocket'
import BatteryInfo from "./batteryinfo/batteryInfo";
import Startup from "./startup/startup";
import AppSize from "./appSizeInfo/appsize";
import Ram from "./ram/ram";
import Pss from "./pss/pss";
import Fps from "./fps/fps";
import Cpu from "./cpu/cpu";
import Heap from "./heap/heap";
import Pageload from "./pageload/pageload";
import ViewCanary from "./viewcanary/viewcanary";
import Traffic from "./traffic/traffic";
import Crash from "./crash/crash";
import Block from "./block/block";
import Network from "./network/network";
import Thread from "./thread/thread";
import MemoryLeak from "./memoryleak/memoryLeak";
import RefreshStatus from "./refreshstatus/refreshStatus";
import MethodCanary from "./methodcanary/methodcanary";
import Mock from "./MockData";

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
            "methodCanary": this.refs.methodCanary,
            "appSizeInfo": this.refs.appSizeInfo,
            "viewIssueInfo": this.refs.viewIssueInfo
        };
        return moduleMap[moduleName];
    }

    _onReceiveMessage(moduleName, payload) {
        if (!this.canRefresh) {
            return;
        }
        this.refs.refreshStatus.refresh(new Date());
        if ("installedModuleConfigs" === moduleName) {
            this.refs.methodCanary.refreshConfig(payload["METHOD_CANARY"]);
            this.refs.blockInfo.refreshConfig(payload["SM"]);
        } else if ("methodCanaryMonitorState" === moduleName) {
            this.refs.methodCanary.refreshStatus(payload);
        } else if ("blockConfig" === moduleName) {
            this.refs.blockInfo.refreshConfig(payload);
        } else {
            if (this._getModuleRef(moduleName)) {
                this._getModuleRef(moduleName).refresh(payload);
            }
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
                    <Row gutter={16} align="top" style={{marginTop: 16}} type="flex" justify="start">
                        <Col md={24} lg={12} xl={6}>
                            <Fps ref="fpsInfo"/>
                        </Col>
                        <Col md={24} lg={12} xl={6}>
                            <BatteryInfo ref="batteryInfo"/>
                        </Col>
                        <Col md={24} lg={12} xl={6}>
                            <Ram ref="ramInfo"/>
                        </Col>
                        <Col md={24} lg={12} xl={6}>
                            <Pss ref="pssInfo"/>
                        </Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col lg={24} xl={7}>
                            <Startup ref="startupInfo"/>
                        </Col>
                        <Col lg={24} xl={9}>
                            <AppSize ref="appSizeInfo"/>
                        </Col>
                        <Col lg={24} xl={8}>
                            <Crash ref="crashInfo"/>
                        </Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col lg={24} xl={7}> <Cpu ref="cpuInfo"/>
                        </Col>
                        <Col lg={24} xl={7}> <Heap ref="heapInfo"/>
                        </Col>
                        <Col lg={24} xl={10}><Traffic ref="trafficInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col lg={24} xl={12}><MemoryLeak ref="leakInfo"/></Col>
                        <Col lg={24} xl={12}><Block ref="blockInfo" globalWs={globalWs}/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col lg={24} xl={12}><Pageload ref="pageLifecycle"/></Col>
                        <Col lg={24} xl={12}><ViewCanary globalWs={globalWs} ref="viewIssueInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}><Thread ref="threadInfo"/></Col>
                    </Row>
                    <Row gutter={16} align="top" style={{marginTop: 16}}>
                        <Col span={24}><Network ref="networkInfo"/></Col>
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
