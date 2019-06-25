import React, {Component} from 'react';
import '../App.css';

import {Card, Button} from 'antd'
import ReactHighcharts from '../../node_modules/react-highcharts'
import RefreshStatus from "../refreshstatus/refreshStatus";

class MethodCanary extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isMonitoring: false
        };
        this.methodEventsMap = {};
        this.toggleIsMonitor = this.toggleIsMonitor.bind(this);
        this.openLastRecord = this.openLastRecord.bind(this);

        this.optionsForSummary = {
            chart: {
                type: 'bar'
            },
            yAxis: {
                min: 0,
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
            series: []
        };
        this.options = {
            chart: {
                type: 'xrange'
            },
            yAxis: {
                title: {
                    text: ''
                },
                categories: ['1', '2', '3'],
                reversed: true
            },
            series: [{
                name: '1111',
                pointPadding: 0,
                groupPadding: 0,
                data: [{
                    x: 10,
                    x2: 15,
                    y: 0
                }, {
                    x: 16,
                    x2: 18,
                    y: 1
                }, {
                    x: 5,
                    x2: 7,
                    y: 2
                }, {
                    x: 19,
                    x2: 24,
                    y: 1
                }, {
                    x: 8,
                    x2: 12,
                    y: 2
                }],
                dataLabels: {
                    enabled: true
                }
            }]
        };
    }

    toggleIsMonitor() {
        this.setState((prevState, props) => ({
            isMonitoring: !prevState.isMonitoring,
        }));
        // TODO KYSON send command to phone
    }

    openLastRecord() {

    }

    refresh(methodEventsMap) {
        this.methodEventsMap = methodEventsMap;
        const threadSeries = [];
        for (const key in this.methodEventsMap) {
            const methodEventCount = this.methodEventsMap[key].length;
            const threadInfo = key;

            threadSeries.push({name: (threadInfo.id + threadInfo.name), data: [methodEventCount]})
        }
        this.optionsForSummary.series = threadSeries;
        this.refs.chartForSummary.getChart().update(this.optionsForSummary);

        // for (let i = 0; i < this.methodEvents.length; i++) {
        //     const stack = methodEvents[i].stack;
        //     const start = methodEvents[i].start;
        //     const end = methodEvents[i].end;
        // }
        // this.refs.chart.getChart().update(this.options);
    }

    render() {
        return (
            <Card title="MethodCanary" extra={<div><Button
                onClick={this.toggleIsMonitor}>{this.state.isMonitoring ? "Stop" : "Start"}</Button>
                <Button onClick={this.openLastRecord}>Open</Button></div>}>
                <ReactHighcharts
                    ref="chartForSummary"
                    config={this.optionsForSummary}
                />
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
            </Card>);
    }
}


export default MethodCanary;