import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import AppInfo from "./appinfo/appInfo";
import {Row, Col, Clearfix, Grid, Panel, Label} from 'react-bootstrap'
import globalWs from './communication/websocket'
import BatteryInfo from "./batteryinfo/batteryInfo";
import Startup from "./startup/startup";
import Ram from "./ram/ram";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {appInfo: '', batteryInfo: '', startupInfo: '', ramInfo: ''};
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        globalWs.start();
    }

    _onReceiveMessage(moduleName, payload) {
        this.setState({[moduleName]: payload});
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
                <Grid>
                    <Row><AppInfo appInfo={this.state.appInfo}/></Row>
                    <Row>
                        <Col md={3}> <BatteryInfo batteryInfo={this.state.batteryInfo}/>
                        </Col>
                        <Col md={4}> <Startup startupInfo={this.state.startupInfo}/>
                        </Col>
                        <Col md={5}> <Label >XXXX</Label>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={5}> <Ram ramInfo={this.state.ramInfo}/>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
}

export default App;
