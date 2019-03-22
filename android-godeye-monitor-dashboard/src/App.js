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
import Mock from "./MockData";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class App extends Component {

    constructor(props) {
        super(props);
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
        this._setCanRefresh = this._setCanRefresh.bind(this);
        this.canRefresh = true;
        this.mock = new Mock();
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        globalWs.start(function (evt) {
            globalWs.sendMessage('{"moduleName": "clientOnline"}')
        });
//        this.mock.start(this._onReceiveMessage);
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
                <ToastContainer autoClose={2000}/>
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
                        <Col md={12}><Network ref="networkInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={12}><Block ref="blockInfo"/></Col>
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
}

export default App;
