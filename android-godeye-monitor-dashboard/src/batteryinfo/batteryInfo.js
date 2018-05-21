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

                    <Row>
                        <Col md={10}>

                            <div>
                                <span>Level(电量):</span><span>{info.level}</span></div>
                            <div>
                                <span>StateOfCharge(充电状态):</span><span>{info.status}</span>
                            </div>
                            <div>
                                <span>TypeOfCharge(充电方式):</span><span>{info.plugged}</span>
                            </div>
                            <div>
                                <span>Present(使用状态):</span><span>{info.present}</span>
                            </div>
                            <div>
                                <span>Health(健康状态):</span><span>{info.health}</span>
                            </div>
                            <div>
                                <span>Voltage(电压):</span><span>{info.voltage}</span>
                            </div>
                            <div>
                                <span>Temperature(温度):</span><span>{info.temperature}</span>
                            </div>
                            <div>
                                <span>Technology(电池类型):</span><span>{info.technology}</span>
                            </div>
                        </Col>

                        <Col md={2} style={{
                            height: 0,
                            paddingBottom: '50%',
                            paddingTop: '50%',
                            paddingLeft: 0,
                            paddingRight: 0
                        }}>
                            <div
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    backgroundColor: '#dadada',
                                    borderRadius: '2px'
                                }}>
                                <div
                                    style={{
                                        height: `${progress}%`,
                                        width: '100%',
                                        backgroundColor: Util.getGreen(),
                                        borderRadius: '2px',
                                        transition: 'all .2s ease-out'
                                    }}/>
                            </div>
                        </Col>
                    </Row>
                </Panel.Body>
            </Panel>);
    }
}

export default BatteryInfo;
