/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import MethodCanaryThread from "./methodcanary_thread";
import Util from "../libs/util";
import Highcharts from 'highcharts/highcharts';


(xrange)(Highcharts);

class MethodCanary extends Component {

    constructor(props) {
        super(props);
        this.state = {
            config: {},
            isRunning: false
        };
        this.record = {};
        this.chart = null;
        this.toggleIsMonitor = this.toggleIsMonitor.bind(this);
        this.clear = this.clear.bind(this);
        this.openMethodCanaryThread = this.openMethodCanaryThread.bind(this);
    }

    toggleIsMonitor() {
        if (!this.state.isRunning) {
            this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"start"}');
        } else {
            this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"stop"}');
        }
    }

    clear() {
        this.record = {};
        if (this.chart) {
            this.chart.destroy();
            this.chart = null;
        }
        this.refs.chartForThread.clear();
    }

    openMethodCanaryThread(threadName) {
        this.refs.chartForThread.refresh(threadName, this.record);
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    refreshConfig(config) {
        this.setState({
            config: config
        });
    }

    refreshStatus(payload) {
        this.setState({
            isRunning: payload.isRunning
        });
    }

    refresh(record) {
        this.record = record;
        const threadSeries = [];
        if (this.record && this.record.methodInfoOfThreadInfos) {
            for (let i = 0; i < this.record.methodInfoOfThreadInfos.length; i++) {
                const methodEventCount = this.record.methodInfoOfThreadInfos[i].methodInfos.length;
                const threadInfo = this.record.methodInfoOfThreadInfos[i].threadInfo;
                if (threadInfo.id === 2 && threadInfo.name === 'main') {
                    threadSeries.push({
                        name: MethodCanary.getThreadNameByThreadInfo(threadInfo),
                        data: [methodEventCount],
                        color: 'black'
                    })
                } else {
                    threadSeries.push({
                        name: MethodCanary.getThreadNameByThreadInfo(threadInfo),
                        data: [methodEventCount],
                        color: Util.getCommonColors()[i % Util.getCommonColors().length]
                    })
                }
            }
        }
        const openMethodCanaryThread = this.openMethodCanaryThread;
        const options = {
            chart: {
                type: 'bar',
                height: 60,
            },
            title: {
                text: null
            },
            colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00',
                '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
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
                visible: false,
                min: 0,
                gridLineWidth: 0
            },
            xAxis: {
                visible: false,
                categories: ['执行方法数'],
            },
            plotOptions: {
                series: {
                    stacking: 'percent',
                    events: {
                        click: function (event) {
                            openMethodCanaryThread(this.name);
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return this.series.name
                        }
                    },
                    borderRadius: 0,
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: 25,
                }
            },
            series: threadSeries
        };
        this.chart = Highcharts.chart('chartForSummary', options);
    }

    render() {
        let instruction = "";
        if (!this.state.config) {
            instruction = "Please install method canary first!"
        } else if (this.state.isRunning) {
            instruction = `Monitoring... | lowCostMethod: ${this.state.config.lowCostMethodThresholdMillis}ms, maxSingleThread: ${this.state.config.maxMethodCountSingleThreadByCost}`
        } else {
            instruction = `lowCostMethod: ${this.state.config.lowCostMethodThresholdMillis}ms, maxSingleThread: ${this.state.config.maxMethodCountSingleThreadByCost}`
        }
        return (
            <Card title="MethodCanary" extra={
                <div>
                    <span>{instruction}&nbsp;&nbsp;</span>
                    <Button
                        onClick={this.toggleIsMonitor}>{this.state.isRunning ? "Stop" : "Start"}</Button>
                    &nbsp;&nbsp;
                    <Button
                        onClick={this.clear}>Clear</Button>
                </div>
            }>
                <Row>
                    <Col span={24}>
                        <div id="chartForSummary"/>
                    </Col>
                </Row>
                <Row>
                    <Col span={24} style={{marginTop: 16}}>
                        <MethodCanaryThread
                            ref="chartForThread"
                        />
                    </Col>
                </Row>
            </Card>);
    }
}


export default MethodCanary;