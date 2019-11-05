import React, {Component} from 'react';
import '../App.css';
// import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
// import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import {Card} from 'antd'

/**
 * 存储信息
 */
class AppSize extends Component {

    constructor(props) {
        super(props);
        this.state = {
            appSizeInfo: {}
        }
    }

    refresh(appSizeInfo) {
        this.setState({appSizeInfo});
    }

    render() {
        let info = this.state.appSizeInfo;
        return (
            <Card title="App Size(存储占用)">
                <div style={{paddingTop: 4, paddingBottom: 4}}>
                    <span >
                        Code Size:&nbsp;&nbsp;
                        <span style={{fontSize: 32}}>{(info.codeSize / (1024 * 1024)).toFixed(2)}</span>&nbsp;&nbsp;MB
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cache Size:&nbsp;&nbsp;
                        <span style={{fontSize: 32}}>{(info.cacheSize / (1024 * 1024)).toFixed(2)}</span>&nbsp;&nbsp;MB
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Size:&nbsp;&nbsp;
                        <span style={{fontSize: 32}}>{(info.dataSize / (1024 * 1024)).toFixed(2)}</span>&nbsp;&nbsp;MB
                    </span>
                </div>
            </Card>);
    }
}

export default AppSize;
