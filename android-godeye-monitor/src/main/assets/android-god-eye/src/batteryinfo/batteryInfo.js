import React, {Component} from 'react';
import '../App.css';
import {Label} from 'react-bootstrap';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {div, Col, Clearfix, Grid, Panel} from 'react-bootstrap'


/**
 * 电池信息
 */
class BatteryInfo extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        let info = this.props.batteryInfo;
        if (!info) {
            info = {};
        }
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Battery(电池)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <div>
                        <div>
                            {/*<div className="barline" id="probar">*/}
                            {/*<div className="battery_line" width={info.level * 100 / info.scale + "%"}/>*/}
                            {/*<div className="battery_btop"/>*/}
                            {/*</div>*/}
                        </div>
                        <div>
                            <span>Level(电量):</span><span>{info.level}</span></div>
                        <div>
                            <span >StateOfCharge(充电状态):</span><span>{info.status}</span>
                        </div>
                        <div>
                            <span >TypeOfCharge(充电方式):</span><span>{info.plugged}</span>
                        </div>
                        <div>
                            <span >Present(使用状态):</span><span>{info.present}</span>
                        </div>
                        <div>
                            <span >Health(健康状态):</span><span>{info.health}</span>
                        </div>
                        <div>
                            <span >Voltage(电压):</span><span>{info.voltage}</span>
                        </div>
                        <div>
                            <span >Temperature(温度):</span><span>{info.temperature}</span>
                        </div>
                        <div>
                            <span >Technology(电池类型):</span><span>{info.technology}</span>
                        </div>
                    </div>
                </Panel.Body>
            </Panel>);
    }
}

export default BatteryInfo;
