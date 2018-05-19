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
        this.state = {
            appInfo: {
                appName: "***",
                labels: []
            },
            batteryInfo: '',
            startupInfo: '',
            ramInfo: '',
            pssInfo: '',
            fpsInfo: ''
        };
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
            this.refs.cpu.updateRenderData(payload);
            return;
        }
        this.setState({[moduleName]: payload});
    }

    render() {
        return (
            <div className="App">
                <Grid>
                    <Row>
                        <Col md={12}><AppInfo appInfo={this.state.appInfo}/></Col>
                    </Row>
                    <Row>
                        <Col md={3}> <BatteryInfo batteryInfo={this.state.batteryInfo}/>
                        </Col>
                        <Col md={4}> <Startup startupInfo={this.state.startupInfo}/>
                        </Col>
                        <Col md={5}> <Label>XXXX</Label>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={5}> <Ram ramInfo={this.state.ramInfo}/>
                        </Col>
                        <Col md={5}> <Pss pssInfo={this.state.pssInfo}/>
                        </Col>
                        <Col md={2}> <Fps fpsInfo={this.state.fpsInfo}/>
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
