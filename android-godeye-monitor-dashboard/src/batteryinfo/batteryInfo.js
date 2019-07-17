import React, {Component} from 'react';
import '../App.css';
// import {Label} from 'react-bootstrap';
// import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
// import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import {Card} from 'antd'


import Util from "../libs/util";


/**
 * 电池信息
 */
class BatteryInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {batteryInfo: {}}
    }

    refresh(batteryInfo) {
        this.setState({batteryInfo});
    }

    render() {
        let info = this.state.batteryInfo;
        if (!info) {
            info = {};
        }
        let progress = info.level * 100 / info.scale;
        return (
            <Card title="Battery(电池)">
                <div
                    style={{
                        width: '100%',
                        height: 15,
                        backgroundColor: Util.getGrey(),
                    }}>
                    <div
                        style={{
                            height: 15,
                            width: progress + '%',
                            backgroundColor: Util.getGreen(),
                            transition: 'all .2s ease-out'
                        }}/>
                </div>
                <div style={{marginTop: 8}}>
                    <small>Level(电量):&nbsp;&nbsp;<strong>{info.level}</strong></small>
                </div>
                <div>
                    <small>StateOfCharge(充电状态):&nbsp;&nbsp;<strong>{info.status}</strong></small>
                </div>
                <div>
                    <small>TypeOfCharge(充电方式):&nbsp;&nbsp;<strong>{info.plugged}</strong></small>
                </div>
                <div>
                    <small>Present(使用状态):&nbsp;&nbsp;<strong>{info.present}</strong></small>
                </div>
                <div>
                    <small>Health(健康状态):&nbsp;&nbsp;<strong>{info.health}</strong></small>
                </div>
                <div>
                    <small>Voltage(电压):&nbsp;&nbsp;<strong>{info.voltage}</strong></small>
                </div>
                <div>
                    <small>Temperature(温度):&nbsp;&nbsp;<strong>{info.temperature}</strong></small>
                </div>
                <div>
                    <small>Technology(电池类型):&nbsp;&nbsp;<strong>{info.technology}</strong></small>
                </div>
            </Card>);
    }
}

export default BatteryInfo;
