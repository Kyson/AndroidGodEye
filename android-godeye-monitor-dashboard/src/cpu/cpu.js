/* eslint-disable react/no-string-refs */
import React, {Component} from 'react';
import '../App.css';
import {Card, message} from 'antd'

import ReactHighcharts from '../../node_modules/react-highcharts'
import CpuInfo from "./cpu_info";

/**
 * Cpu
 */
class Cpu extends Component {

    constructor(props) {
        super(props);
        this.options = {
            chart: {
                spacingLeft: 0,
                spacingRight: 0,
                height: 200,
                type: "line"
            },
            exporting: {
                enabled: false
            },
            legend: {
                enabled: false
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
                type: 'category',
                labels: {
                    enabled: false
                },
                lineWidth: 0,
                tickLength: 0,
                gridLineWidth: 1
            },
            yAxis: {
                min: 0,
                max: 100,
                visible: false
            },
            plotOptions: {
                line: {
                    lineWidth: 2,
                    marker: {
                        enabled: false
                    }
                }
            },
            series: [
                {
                    name: 'Device',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'App',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'UserProcess',
                    data: (Cpu.initSeries())
                },
                {
                    name: 'SystemProcess',
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
            message.error("CPU overload(CPU负载过重)!!!");
        }
        this.refs.info.refresh(cpuInfo);
    }

    render() {
        return (
            <Card title="CPU">
                <CpuInfo ref="info"/>
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
            </Card>);
    }
}

export default Cpu;
