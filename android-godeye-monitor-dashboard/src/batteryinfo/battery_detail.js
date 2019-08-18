import React, {Component} from 'react';
import '../App.css';

/**
 * 电池信息
 */
class BatteryDetail extends Component {

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
        return (
            <div>
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
            </div>);
    }
}

export default BatteryDetail;
