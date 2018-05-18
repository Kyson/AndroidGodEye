import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import AppInfo from "./appinfo/appInfo";
import {Row, Col, Clearfix, Grid} from 'react-bootstrap'
import globalWs from './communication/websocket'

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {appInfo: ''};
        this._onReceiveMessage = this._onReceiveMessage.bind(this);
    }

    componentDidMount() {
        globalWs.setReceiveMessageCallback(this._onReceiveMessage);
        globalWs.start();
    }

    _onReceiveMessage(moduleName, payload) {
        if ('appInfo' === moduleName) {
            this.setState({appInfo: payload});
            return;
        }
        if ('' === moduleName) {

        }
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
                </Grid>
            </div>
        );
    }
}

export default App;
