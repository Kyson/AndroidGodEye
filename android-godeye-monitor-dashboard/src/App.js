import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import AppInfo from "./appinfo/appInfo";
import {Row, Col, Clearfix, Grid, Panel, Label, PageHeader} from 'react-bootstrap'
import globalWs from './communication/websocket'
import BatteryInfo from "./batteryinfo/batteryInfo";
import Startup from "./startup/startup";
import Ram from "./ram/ram";
import Pss from "./pss/pss";
import Fps from "./fps/fps";
import Cpu from "./cpu/cpu";

class App extends Component {

    constructor(props) {
        super(props);
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
        this.refresh = this.refresh.bind(this);
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        globalWs.start();
        // setInterval(this.refresh, 2000)
    }

    refresh() {
        this._onReceiveMessage("cpuInfo", {
            totalUseRatio: 0.56,
            appCpuRatio: 0.22,
            userCpuRatio: 0.10,
            sysCpuRatio: 0.18
        })
    }

    _onReceiveMessage(moduleName, payload) {
        if ("cpuInfo" === moduleName) {
            this.refs.cpu.refresh(payload);
            return;
        }
        if ("appInfo" === moduleName) {
            this.refs.appInfo.refresh(payload);
            return;
        }
        if ("batteryInfo" === moduleName) {
            this.refs.batteryInfo.refresh(payload);
            return;
        }
        if ("startupInfo" === moduleName) {
            this.refs.startupInfo.refresh(payload);
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
        if ("fpsInfo" === moduleName) {
            this.refs.fpsInfo.refresh(payload);
            return;
        }
    }

    render() {
        return (
            <div className="App">
                <Grid>
                    <Row>
                        <Col md={12}><AppInfo ref="appInfo"/></Col>
                    </Row>
                    <Row>
                        <Col md={3}> <BatteryInfo ref="batteryInfo"/>
                        </Col>
                        <Col md={4}> <Startup ref="startupInfo"/>
                        </Col>
                        <Col md={5}> <Label>XXXX</Label>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={5}> <Ram ref="ramInfo"/>
                        </Col>
                        <Col md={5}> <Pss ref="pssInfo"/>
                        </Col>
                        <Col md={2}> <Fps ref="fpsInfo"/>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6}> <Cpu ref="cpu"/>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
}

export default App;
