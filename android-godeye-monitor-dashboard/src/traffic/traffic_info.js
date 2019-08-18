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

    refresh(trafficInfo) {
        if (trafficInfo) {
            this.setState({
                trafficInfo: {
                    rxUidRate: trafficInfo.rxUidRate.toFixed(1),
                    txUidRate: trafficInfo.txUidRate.toFixed(1),
                    rxTotalRate: trafficInfo.rxTotalRate.toFixed(1),
                    txTotalRate: trafficInfo.txTotalRate.toFixed(1),
                }
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
