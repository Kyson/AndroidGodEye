/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import ReactHighcharts from 'react-highcharts'
import MethodCanaryThread from "./methodcanary_thread";

(xrange)(ReactHighcharts.Highcharts);

class MethodCanary extends Component {

    constructor(props) {
        super(props);
        this.state = {
            MethodCanaryStatus: {}
        };
        this.record = {};
        this.toggleIsMonitor = this.toggleIsMonitor.bind(this);
        this.openMethodCanaryThread = this.openMethodCanaryThread.bind(this);
        const functionOpenThread = this.openMethodCanaryThread;

        this.optionsForSummary = {
            chart: {
                type: 'bar',
                height: 1
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
                min: 0,
                gridLineWidth: 0
            },
            xAxis: {
                visible: false,
                categories: ['执行方法数']
            },
            plotOptions: {
                series: {
                    stacking: 'percent',
                    events: {
                        click: function (event) {
                            functionOpenThread(this.name);
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
                    pointWidth: 30,
                }
            },
            series: []
        };
    }

    componentDidMount() {
        this.onWsOpenCallback = () => {
            this.props.globalWs.sendMessage('{"moduleName": "MethodCanaryStatus"}');
        };
        this.props.globalWs.registerCallback(this.onWsOpenCallback);
    }

    componentWillUnmount() {
        this.props.globalWs.unregisterCallback(this.onWsOpenCallback);
    }

    toggleIsMonitor() {
        if (this.state.MethodCanaryStatus.isInstalled && !this.state.MethodCanaryStatus.isMonitoring) {
            this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"start"}');
        } else if (this.state.MethodCanaryStatus.isInstalled && this.state.MethodCanaryStatus.isMonitoring) {
            this.props.globalWs.sendMessage('{"moduleName": "methodCanary","payload":"stop"}');
        }
    }

    openMethodCanaryThread(threadName) {
        this.refs.chartForThread.openMethodCanaryThread(threadName, this.record);
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    refreshStatus(record) {
        this.setState({
            MethodCanaryStatus: record
        });
    }

    refresh(record) {
        let i;
        this.record = record;
        const threadSeries = [];
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
                    data: [methodEventCount]
                })
            }
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
            chart: {
                height: 100
            },
            series: threadSeries
        });
    }

    render() {
        let instruction = "";
        if (!this.state.MethodCanaryStatus.isInstalled) {
            instruction = "Please install method canary first."
        } else if (this.state.MethodCanaryStatus.isMonitoring) {
            instruction = `Monitoring... | lowCostMethod: ${this.state.MethodCanaryStatus.lowCostMethodThresholdMillis}ms, maxSingleThread: ${this.state.MethodCanaryStatus.maxMethodCountSingleThreadByCost}`
        } else {
            instruction = `Click and drag to zoom in, Hold down shift to pan(框选放大，按住shift左右拖动)`
        }
        return (
            <Card title="MethodCanary" extra={
                <div>
                    <span>{instruction}&nbsp;&nbsp;</span>
                    <Button
                        onClick={this.toggleIsMonitor}>{this.state.MethodCanaryStatus.isMonitoring ? "Stop" : "Start"}</Button>
                </div>
            }>
                <Row>
                    <Col span={24}>
                        <ReactHighcharts
                            ref="chartForSummary"
                            config={this.optionsForSummary}/>
                    </Col>
                </Row>
                <Row>
                    <Col span={24}>
                        <MethodCanaryThread
                            ref="chartForThread"
                        />
                    </Col>
                </Row>
            </Card>);
    }
}


export default MethodCanary;