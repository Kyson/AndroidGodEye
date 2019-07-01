import React, {Component} from 'react';
import '../App.css';
import {Card} from 'antd'

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import ReactHighcharts from '../../node_modules/react-highcharts'
import {toast} from 'react-toastify';

exporting(Highcharts);

/**
 * Cpu
 */
class Cpu extends Component {

    constructor(props) {
        super(props);
        this.options = {
            chart: {
                type: 'area',
                spacingLeft: 0,
                spacingRight: 0,
            },
            title: {
                text: null
            },
            credits: {
                enabled: false
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
        this.index = 0;
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

    generateIndex() {
        this.index = this.index + 1;
        return this.index;
    }

    refresh(cpuInfo) {
        if (cpuInfo) {
            let axisData = this.generateIndex() + (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, cpuInfo.totalUseRatio * 100], false, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, cpuInfo.appCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().series[2].addPoint([axisData, cpuInfo.userCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().series[3].addPoint([axisData, cpuInfo.sysCpuRatio * 100], false, true, true);
            this.refs.chart.getChart().redraw(true);
        }
        if (cpuInfo.appCpuRatio >= 0.9 || cpuInfo.totalUseRatio >= 0.9) {
            toast.error("CPU overload(CPU负载过重)!!!");
        }
    }

    render() {
        return (
            <Card title="Cpu(处理器)">
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
            </Card>);
    }
}

export default Cpu;
