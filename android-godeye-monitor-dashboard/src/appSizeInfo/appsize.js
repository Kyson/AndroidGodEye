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
                        <span style={{fontSize: 32}}>{info.codeSize}</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cache Size:&nbsp;&nbsp;
                        <span style={{fontSize: 32}}>{info.cacheSize}</span>&nbsp;&nbsp;ms
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Size:&nbsp;&nbsp;
                        <span style={{fontSize: 32}}>{info.dataSize}</span>&nbsp;&nbsp;ms
                    </span>
                </div>
            </Card>);
    }
}

export default AppSize;
