import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import HighchartsReact from '../../node_modules/highcharts-react-official'

/**
 * Cpu
 */
class Cpu extends Component {

    constructor(props) {
        super(props);
        this.options = {
            title: {
                text: null
            },
            tooltip: {
                shared: true,
                formatter: function () {
                    let tip = "";
                    for (let i = 0; i < this.points.length; i++) {
                        let point = this.points[i].point;
                        let seriesName = this.points[i].series.name;
                        tip = tip + seriesName + ' : ' + point.y.toFixed(1) + ' % <br/>';
                    }
                    return tip;
                }
            },
            xAxis: {
                type: 'category',
                data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            },
            yAxis: {
                title: {
                    text: "Cpu Usage Rate(Percentage)",
                    align: "middle",
                },
                min: 0,
                max: 100
            },
            series: [
                {
                    name: 'Total',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'App',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'UserProcess',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'SystemProcess',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
    }

    updateRenderData(cpuInfos) {
        if (cpuInfos && cpuInfos.length > 0) {
            for (let i = 0; i < cpuInfos.length; i++) {
                let cpuInfo = cpuInfos[i];
                let axisData = (new Date()).toLocaleTimeString();
                this.options.xAxis.data.shift();
                this.options.xAxis.data.push(axisData);
                this.options.series[0].data.shift();
                this.options.series[0].data.push(cpuInfo.totalUseRatio * 100);
                this.options.series[1].data.shift();
                this.options.series[1].data.push(cpuInfo.appCpuRatio * 100);
                this.options.series[2].data.shift();
                this.options.series[2].data.push(cpuInfo.userCpuRatio * 100);
                this.options.series[3].data.shift();
                this.options.series[3].data.push(cpuInfo.sysCpuRatio * 100);
            }
        }
    }

    render() {
        this.updateRenderData(this.props.cpuInfos);
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Cpu
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <HighchartsReact
                        highcharts={Highcharts}
                        options={this.options}
                    />
                </Panel.Body>
            </Panel>);
    }
}

export default Cpu;
