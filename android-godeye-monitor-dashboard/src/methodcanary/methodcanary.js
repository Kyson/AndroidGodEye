import React, {Component} from 'react';
import '../App.css';

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import ReactHighcharts from 'react-highcharts'

(xrange)(ReactHighcharts.Highcharts);

class MethodCanary extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isMonitoring: false
        };
        this.record = {};
        this.toggleIsMonitor = this.toggleIsMonitor.bind(this);
        this.openThread = this.openThread.bind(this);
        const functionOpenThread = this.openThread;

        this.optionsForSummary = {
            chart: {
                type: 'bar',
                height: 60
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
            credits: {
                enabled: false
            },
            yAxis: {
                title: {
                    text: null
                },
                min: 0,
                gridLineWidth: 0
            },
            xAxis: {
                visible: false
            },
            plotOptions: {
                series: {
                    stacking: 'percent',
                    events: {
                        click: function (event) {
                            functionOpenThread(this.name);
                        }
                    },
                    borderRadius: 0,
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: 40
                }
            },
            series: []
        };
        this.optionsForThread = {
            chart: {
                type: 'xrange',
                height: 1
            },
            colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00',
                '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
            title: {
                text: null
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false
            },
            xAxis: {
                title: {
                    text: null
                },
                gridLineWidth: 0
            },
            yAxis: {
                visible: false,
                reversed: true
            },
            plotOptions: {
                series: {
                    borderRadius: 0,
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: 20
                },
                column: {
                    colorByPoint: true
                }
            },
            series: [{
                name: '',
                data: [{}]
            }]
        };
    }

    toggleIsMonitor() {
        this.setState((prevState, props) => ({
            isMonitoring: !prevState.isMonitoring,
        }), () => {
            if (this.state.isMonitoring) {
                this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"start"}')
            } else {
                this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"stop"}')
            }
        });
    }

    openThread(threadName) {
        let methodInfos = {};
        for (let i = 0; i < this.record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanary.getThreadNameByThreadInfo(this.record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = this.record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
        const datas = [];
        let maxStack = 0;
        for (let i = 0; i < methodInfos.length; i++) {
            datas.push({
                x: methodInfos[i].start,
                x2: methodInfos[i].end,
                y: methodInfos[i].stack,
                methodEvent: methodInfos[i]
            });
            if (methodInfos[i].stack > maxStack) {
                maxStack = methodInfos[i].stack
            }
        }
        const categories = [];
        let height = 50;
        for (let i = 0; i < (maxStack + 1); i++) {
            categories.push("");
            height = height + 20
        }
        const min = this.record.start;
        const max = this.record.end;
        this.refs.chartForThread.getChart().update({
            chart: {
                type: 'xrange',
                height: height
            },
            tooltip: {
                formatter: function () {
                    return this.point.methodEvent.className + "#" + this.point.methodEvent.methodName + " ,from "
                        + this.point.methodEvent.start + " to " + this.point.methodEvent.end + ", cost " + (this.point.methodEvent.end - this.point.methodEvent.start) / 1000000 + "ms"
                }
            },
            series: [{
                name: '',
                pointPadding: 0,
                groupPadding: 0,
                pointWidth: 20,
                data: datas,
                dataLabels: {
                    enabled: true,
                    style: {
                        fontSize: 10,
                        fontWeight: "regular",
                        textOutline: "0px 0px contrast",
                        color: "black"
                    },
                    formatter: function () {
                        return this.point.methodEvent.className + "#" + this.point.methodEvent.methodName
                    }
                }
            }],
            yAxis: {
                title: {
                    text: null
                },
                reversed: true,
                categories: categories
            },
            xAxis: {
                title: {
                    text: null
                },
                gridLineWidth: 0,
                min: min,
                max: max
            },
        });
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    refresh(record) {
        let i;
        this.record = record;
        const threadSeries = [];
        for (let i = 0; i < this.record.methodInfoOfThreadInfos.length; i++) {
            const methodEventCount = this.record.methodInfoOfThreadInfos[i].methodInfos.length;
            const threadInfo = this.record.methodInfoOfThreadInfos[i].threadInfo;
            threadSeries.push({
                name: MethodCanary.getThreadNameByThreadInfo(threadInfo),
                data: [methodEventCount],
            })
        }
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
        });
    }


    render() {
        return (
            <Card title="MethodCanary" extra={<Button
                onClick={this.toggleIsMonitor}>{this.state.isMonitoring ? "Stop" : "Start"}</Button>}>
                <Row>
                    <Col span={24}>
                        <ReactHighcharts
                            ref="chartForSummary"
                            config={this.optionsForSummary}/>
                    </Col>
                </Row>
                <Row>
                    <Col span={24}>
                        <ReactHighcharts
                            ref="chartForThread"
                            config={this.optionsForThread}
                        />
                    </Col>
                </Row>
            </Card>);
    }
}


export default MethodCanary;