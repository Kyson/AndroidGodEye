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
            MethodCanaryStatus: {}
        };
        this.record = {};
        this.toggleIsMonitor = this.toggleIsMonitor.bind(this);
        this.openThread = this.openThread.bind(this);
        const functionOpenThread = this.openThread;

        this.optionsForSummary = {
            chart: {
                type: 'bar',
                height: 1
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
                    pointWidth: 30
                }
            },
            series: []
        };
        this.optionsForThread = {
            chart: {
                type: 'xrange',
                height: 1,
                zoomType: 'x',
                panning: true,
                panKey: 'shift',
                marginTop: 30,
                marginBottom: 60,
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
            tooltip: {
                formatter: function () {
                    return "cost " + ((this.point.methodEvent.end - this.point.methodEvent.start) / 1000000).toFixed(2) + " ms, " + this.point.methodEvent.className + "#" + this.point.methodEvent.methodName + " ,from "
                        + this.point.methodEvent.start + " to " + this.point.methodEvent.end
                }
            },
            xAxis: {
                title: {
                    text: null
                },
                gridLineWidth: 0,
                labels: {
                    formatter: function () {
                        return (this.value / 1000000000).toFixed(3) + "s";
                    }
                }
            },
            yAxis: {
                title: {
                    text: null
                },
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
                pointPadding: 0,
                groupPadding: 0,
                pointWidth: 30,
                dataLabels: {
                    enabled: true,
                    style: {
                        fontSize: 12,
                        fontWeight: "regular",
                        textOutline: "0px 0px contrast",
                        color: "black"
                    },
                    formatter: function () {
                        return this.point.methodEvent.className + "#" + this.point.methodEvent.methodName
                    }
                },
                data: [{}]
            }]
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

    openThread(threadName) {
        let methodInfos = {};
        for (let i = 0; i < this.record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanary.getThreadNameByThreadInfo(this.record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = this.record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
        const min = this.record.start;
        const max = this.record.end;
        const datas = [];
        let maxStack = 0;
        for (let i = 0; i < methodInfos.length; i++) {
            datas.push({
                x: methodInfos[i].start === 0 ? min : methodInfos[i].start,
                x2: (methodInfos[i].end === 0 ? max : methodInfos[i].end),
                y: methodInfos[i].stack,
                methodEvent: methodInfos[i]
            });
            if (methodInfos[i].stack > maxStack) {
                maxStack = methodInfos[i].stack
            }
        }
        const categories = [];
        let height = 90;
        for (let i = 0; i < (maxStack + 1); i++) {
            categories.push("");
            height = height + 35
        }
        this.refs.chartForThread.getChart().update({
            chart: {
                height: height
            },
            series: [{
                data: datas,
            }],
            yAxis: {
                categories: categories
            },
            xAxis: {
                min: min,
                max: max
            },
        });
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
            threadSeries.push({
                name: MethodCanary.getThreadNameByThreadInfo(threadInfo),
                data: [methodEventCount]
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
            chart: {
                height: 100
            },
            series: threadSeries
        });
    }

    render() {
        let instruction = "";
        if(!this.state.MethodCanaryStatus.isInstalled){
            instruction = "Please install method canary first."
        } else if (this.state.MethodCanaryStatus.isMonitoring) {
            instruction = `Monitoring... | lowCostMethod: ${this.state.MethodCanaryStatus.lowCostMethodThresholdMillis}ms, maxSingleThread: ${this.state.MethodCanaryStatus.maxMethodCountSingleThreadByCost}`
        } else {
            instruction = `Select to zoom in, hold shift and drag(框选放大，按住shift左右拖动)`
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