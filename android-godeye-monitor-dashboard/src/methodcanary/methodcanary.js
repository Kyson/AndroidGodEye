import React, {Component} from 'react';
import '../App.css';

import {Card, Button} from 'antd'
import ReactHighcharts from '../../node_modules/react-highcharts'

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
            title: {
                text: null
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            yAxis: {
                title: {
                    text: null
                },
                min: 0,
            },
            xAxis: {
                title: {
                    text: null
                },
                categories: ['']
            },
            plotOptions: {
                series: {
                    stacking: 'percent',
                    events: {
                        click: function (event) {
                            alert(
                                this.name + ' 被点击了\n' +
                                '最近点：' + event.point.category + '\n' +
                                'Alt 键: ' + event.altKey + '\n' +
                                'Ctrl 键: ' + event.ctrlKey + '\n' +
                                'Meta 键（win 键）： ' + event.metaKey + '\n' +
                                'Shift 键：' + event.shiftKey
                            );
                        }
                    },
                    pointPadding: 0,
                    groupPadding: 0
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
        let i;
        this.methodEventsMap = methodEventsMap;
        const threadSeries = [];
        for (let i = 0; i < this.methodEventsMap.length; i++) {
            const methodEventCount = this.methodEventsMap[i].methodEvents.length;
            const threadInfo = this.methodEventsMap[i].threadInfo;
            threadSeries.push({name: (threadInfo.id + threadInfo.name), data: [methodEventCount]})
        }
        this.optionsForSummary.series = threadSeries;


        const diff = this.refs.chartForSummary.getChart().series.length - threadSeries.length;
        if (diff > 0) {
            for (i = this.refs.chartForSummary.getChart().series.length; i > diff; i--) {
                this.refs.chartForSummary.getChart().series[i - 1].remove(true);
            }
        } else if (diff < 0) {
            for (i = this.refs.chartForSummary.getChart().series.length; i < threadSeries.length; i++) {
                this.refs.chartForSummary.getChart().addSeries({});
            }
        }

        this.refs.chartForSummary.getChart().update({
            series: threadSeries
        })
        ;        // for (let i = 0; i < this.methodEvents.length; i++) {
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
            </Card>);
    }
}


export default MethodCanary;