import React, {Component} from 'react';
import '../App.css';


class CpuInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cpuInfo: {}
        };
    }

    refresh(cpuInfo) {
        if (cpuInfo) {
            this.setState({
                cpuInfo: {
                    totalUseRatio: (cpuInfo.totalUseRatio * 100).toFixed(1),
                    appCpuRatio: (cpuInfo.appCpuRatio * 100).toFixed(1)
                }
            })
        } else {
            this.setState({
                cpuInfo: {
                    totalUseRatio: "*",
                    appCpuRatio: "*"
                }
            })
        }

    }

    render() {
        return (
            <span
                style={{fontSize: 15}}>App:&nbsp;
                <span style={{fontSize: 25}}>{this.state.cpuInfo.appCpuRatio}</span>&nbsp;%&nbsp;&nbsp;&nbsp;Device:&nbsp;
                <span style={{fontSize: 25}}>{this.state.cpuInfo.totalUseRatio}</span>&nbsp;%</span>
        )
    }
}

export default CpuInfo;
