import React, {Component} from 'react';
import '../App.css';
import {Icon} from 'antd';

/**
 * TrafficInfo
 */
class TrafficInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            trafficInfo: {}
        };
    }

    // trafficInfo.rxTotalRate], false, true, true);
    // ([axisData, trafficInfo.txTotalRate], false, true, true);
    // [axisData, trafficInfo.rxUidRate], false, true, true);
    // ([axisData, trafficInfo.txUidRate]

    refresh(trafficInfo) {
        if (trafficInfo) {
            this.setState({
                trafficInfo
            })
        } else {
            this.setState({
                trafficInfo: {
                    rxTotalRate: "*",
                    txTotalRate: "*",
                    rxUidRate: "*",
                    txUidRate: "*",
                }
            })
        }
    }

    render() {
        return (
            <div>
            <span style={{fontSize: 15}}>App:&nbsp;
                <Icon type="arrow-down"/>
                <span style={{fontSize: 25}}>{this.state.trafficInfo.rxUidRate}</span>
                &nbsp;
                <Icon type="arrow-up"/>
                <span style={{fontSize: 25}}>{this.state.trafficInfo.txUidRate}</span>
                ,&nbsp;Total:&nbsp;
                <Icon type="arrow-down"/>
                <span style={{fontSize: 25}}>{this.state.trafficInfo.rxTotalRate}</span>
                &nbsp;
                <Icon type="arrow-up"/>
                <span style={{fontSize: 25}}>{this.state.trafficInfo.txTotalRate}</span>
            </span>
            </div>
        )
    }
}

export default TrafficInfo;
