import React, {Component} from 'react';
import '../App.css';
import {Label} from 'react-bootstrap';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
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
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Battery(电池)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <div
                        style={{
                            width: '100%',
                            height: 30,
                            backgroundColor: '#dadada',
                            borderRadius: '2px'
                        }}>
                        <div
                            style={{
                                height: 30,
                                width: progress + '%',
                                backgroundColor: Util.getGreen(),
                                borderRadius: '2px',
                                transition: 'all .2s ease-out'
                            }}/>
                    </div>
                    <div style={{marginTop: 15}}>
                        <span>Level(电量):&nbsp;&nbsp;&nbsp;</span><strong>{info.level}</strong></div>
                    <div>
                        <span>StateOfCharge(充电状态):&nbsp;&nbsp;&nbsp;</span><strong>{info.status}</strong>
                    </div>
                    <div>
                        <span>TypeOfCharge(充电方式):&nbsp;&nbsp;&nbsp;</span><strong>{info.plugged}</strong>
                    </div>
                    <div>
                        <span>Present(使用状态):&nbsp;&nbsp;&nbsp;</span><strong>{info.present}</strong>
                    </div>
                    <div>
                        <span>Health(健康状态):&nbsp;&nbsp;&nbsp;</span><strong>{info.health}</strong>
                    </div>
                    <div>
                        <span>Voltage(电压):&nbsp;&nbsp;&nbsp;</span><strong>{info.voltage}</strong>
                    </div>
                    <div>
                        <span>Temperature(温度):&nbsp;&nbsp;&nbsp;</span><strong>{info.temperature}</strong>
                    </div>
                    <div>
                        <span>Technology(电池类型):&nbsp;&nbsp;&nbsp;</span><strong>{info.technology}</strong>
                    </div>

                </Panel.Body>
            </Panel>);
    }
}

export default BatteryInfo;
