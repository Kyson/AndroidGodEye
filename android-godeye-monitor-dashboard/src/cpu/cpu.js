import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import ReactHighcharts from '../../node_modules/react-highcharts'

exporting(Highcharts);
/**
 * Cpu
 */
class Cpu extends Component {

    constructor(props) {
        super(props);
        this.options = {
            credits: {
                enabled: false
            },
            chart: {
                type: 'area'
            },
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
                type: 'category'
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
                    stack: 'cpu_total',
                    stacking: 'normal',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'App',
                    stack: 'cpu',
                    stacking: 'normal',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'UserProcess',
                    stack: 'cpu',
                    stacking: 'normal',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'SystemProcess',
                    stack: 'cpu',
                    stacking: 'normal',
                    data: (Cpu.initSeries())
                }
            ]
        };
    }

    static initSeries() {
        let data = [];
        for (let i = 0; i < 20; i++) {
            data.push({
                x: i,
                y: 0
            });
        }
        return data;
    }

    refresh(cpuInfo) {
        if (cpuInfo) {
            let axisData = (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, cpuInfo.totalUseRatio * 100], false, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, cpuInfo.appCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().series[2].addPoint([axisData, cpuInfo.userCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().series[3].addPoint([axisData, cpuInfo.sysCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().redraw(true);
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Cpu(处理器)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactHighcharts
                        ref="chart"
                        config={this.options}
                    />
                </Panel.Body>
            </Panel>);
    }
}

export default Cpu;
