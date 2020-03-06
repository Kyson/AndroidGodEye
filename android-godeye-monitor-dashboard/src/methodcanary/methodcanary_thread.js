/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, { Component } from 'react';
import '../App.css';
import Util from "../libs/util";

import { Collapse, Input } from 'antd'

import xrange from 'highcharts/modules/xrange';
import Highcharts from 'highcharts/highstock';
import MethodCanaryThreadTree from "./methodcanary_thread_tree";

(xrange)(Highcharts);

class MethodCanaryThread extends Component {

    static getMethodValueWithRange(realValue, range) {
        return realValue === 0 ? range : realValue;
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    constructor(props) {
        super(props);
        this.refresh = this.refresh.bind(this);
        this.renderDetail = this.renderDetail.bind(this);
        this.renderExtra = this.renderExtra.bind(this);
        this.afterSetExtremes = this.afterSetExtremes.bind(this);
        this.clear = this.clear.bind(this);
        this.refreshByParams = this.refreshByParams.bind(this);
        this.chart = null;
        this.threadName = null;
        this.record = null;
        this.searchText = null;
        this.methodInfos = [];
        this.afterSetExtremesTimerId = null;
        this.state = {
            startMillis: 0,
            endMillis: 0
        }
    }

    afterSetExtremes(e) {
        this.setState({
            startMillis: e.min,
            endMillis: e.max
        });
        clearTimeout(this.afterSetExtremesTimerId);
        this.afterSetExtremesTimerId = setTimeout(() => {
            if (this.refs.methodCanaryThreadTree) {
                this.refs.methodCanaryThreadTree.refresh(e.min, e.max, this.methodInfos);
            }
        }, 300);
    }

    clear() {
        this.methodInfos = [];
        if (this.chart) {
            this.chart.destroy();
            this.chart = null;
        }
        this.setState({
            startMillis: 0,
            endMillis: 0,
        });
        if (this.refs.methodCanaryThreadTree) {
            this.refs.methodCanaryThreadTree.clear();
        }
    }

    refreshByParams() {
        const record = this.record;
        const threadName = this.threadName;
        const searchText = this.searchText;
        if (!record || !threadName) {
            this.clear();
            return;
        }
        let methodInfos = [];
        for (let i = 0; i < record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanaryThread.getThreadNameByThreadInfo(record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
        methodInfos.sort((a, b) => {
            if (a.startMillis === b.startMillis) {
                if (a.endMillis === b.endMillis) {
                    return a.stack - b.stack;
                }
                return b.endMillis - a.endMillis;
            }
            return a.startMillis - b.startMillis;
        });
        const min = record.startMillis;
        const max = record.endMillis;
        this.setState({ startMillis: min, endMillis: max });
        const datas = [];
        let maxStack = 0;
        for (let i = 0; i < methodInfos.length; i++) {
            methodInfos[i].startMillis = MethodCanaryThread.getMethodValueWithRange(methodInfos[i].startMillis, min);
            methodInfos[i].endMillis = MethodCanaryThread.getMethodValueWithRange(methodInfos[i].endMillis, max);
            let selected = false;
            if (searchText) {
                selected = JSON.stringify(methodInfos[i]).toLowerCase().search(searchText) !== -1;
            }
            datas.push({
                x: methodInfos[i].startMillis,
                x2: methodInfos[i].endMillis,
                y: methodInfos[i].stack,
                color: Util.getColorForMethod(methodInfos[i]),
                methodEvent: methodInfos[i],
                selected: selected
            });
            if (methodInfos[i].stack > maxStack) {
                maxStack = methodInfos[i].stack
            }
        }

        this.methodInfos = methodInfos;
        if (this.refs.methodCanaryThreadTree) {
            this.refs.methodCanaryThreadTree.refresh(min, max, methodInfos);
        }

        const categories = [];
        for (let i = 0; i < (maxStack + 1); i++) {
            categories.push("");
        }
        const thumbnail_point_width = 5;
        const main_point_width = 20;
        let thumbnail_height = (maxStack + 4) * thumbnail_point_width * 1.5;
        let main_height = 120 + thumbnail_height + (maxStack + 1) * main_point_width * 1.2;
        this.chart = Highcharts.stockChart('chart', {
            chart: {
                type: 'xrange',
                height: main_height,
                zoomType: 'x'
            },
            title: null,
            credits: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            navigator: {
                enabled: true,
                height: thumbnail_height,
                series: {
                    type: 'xrange',
                    borderRadius: 0,
                    pointWidth: thumbnail_point_width
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    gridLineWidth: 0,
                    reversed: true,
                    startOnTick: false,
                    endOnTick: false,
                    crosshair: false,
                    min: -1,
                    max: maxStack + 3
                },
                xAxis: {
                    title: {
                        text: null
                    },
                    crosshair: false,
                    startOnTick: false,
                    endOnTick: false,
                    ordinal: false,
                    lineWidth: 1,
                    gridLineWidth: 1,
                    labels: {
                        formatter: function () {
                            return Util.getFormatMAndSAndMS(this.value);
                        }
                    },
                }
            },
            tooltip: {
                enabled: true,
                split: false,
                shared: false,
                formatter: function () {
                    let s = "";
                    const e = this.point;
                    if (e.methodEvent) {
                        s += "cost " + Util.getFormatDuration(e.methodEvent.endMillis - e.methodEvent.startMillis) + '<br/>';
                        s += e.methodEvent.className + "." + e.methodEvent.methodName + '<br/>';
                        s += 'From ' + Util.getDetailDate(e.methodEvent.startMillis) + " to " + Util.getDetailDate(e.methodEvent.endMillis);
                    }
                    return s;
                }
            },
            rangeSelector: {
                enabled: false,
            },
            xAxis: {
                title: {
                    text: 'Time(ms)',
                    align: "high",
                },
                crosshair: false,
                startOnTick: false,
                endOnTick: false,
                ordinal: false,
                min: min,
                max: max,
                lineWidth: 1,
                gridLineWidth: 1,
                labels: {
                    formatter: function () {
                        return Util.getFormatMAndSAndMS(this.value);
                    }
                },
                events: {
                    afterSetExtremes: this.afterSetExtremes
                },
            },
            yAxis: {
                title: {
                    text: null
                },
                gridLineWidth: 0,
                reversed: true,
                startOnTick: false,
                endOnTick: false,
                crosshair: false,
                categories: categories,
                min: 0,
                max: maxStack
            },
            series: [
                {
                    data: datas,
                    borderRadius: 0,
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: main_point_width,
                    dataLabels: {
                        enabled: true,
                        style: {
                            fontSize: 12,
                            fontWeight: "regular",
                            textOutline: "0px 0px contrast",
                            color: "black"
                        },
                        formatter: function () {
                            if (this.point.methodEvent) {
                                const index = this.point.methodEvent.className.lastIndexOf("/");
                                const className = this.point.methodEvent.className.substring(index + 1, this.point.methodEvent.className.length);
                                return className + "." + this.point.methodEvent.methodName
                            }
                            return null
                        }
                    },
                }
            ]
        });
    }

    refresh(threadName, record) {
        this.threadName = threadName;
        this.record = record;
        this.refreshByParams();
    }

    renderDetail() {
        return <Collapse bordered={true} defaultActiveKey={['1']}>
            <Collapse.Panel header={this.renderTimeRange()} key="1">
                <MethodCanaryThreadTree ref="methodCanaryThreadTree" />
            </Collapse.Panel>
        </Collapse>
    }

    renderTimeRange() {
        if (this.state.endMillis !== 0 || this.state.startMillis !== 0) {
            return <span>Selected duration:&nbsp;
                <strong>{Util.getFormatDuration(this.state.endMillis - this.state.startMillis)}</strong>
                ,&nbsp;Range from&nbsp;<strong>{Util.getDetailDate(this.state.startMillis)}</strong>
                &nbsp;to&nbsp;<strong>{Util.getDetailDate(this.state.endMillis)}</strong></span>
        } else {
            return <span>Empty...</span>
        }
    }

    renderExtra() {
        if (this.state.endMillis !== 0 || this.state.startMillis !== 0) {
            return (
                <div style={{ textAlign: 'right' }}>
                    <Input.Search
                        style={{ width: 200 }}
                        placeholder="Input search text"
                        onSearch={value => {
                            this.searchText = value;
                            this.refreshByParams()

                        }}
                    /></div>)
        } else {
            return <div />
        }
    }

    render() {
        return (
            <div>
                {this.renderExtra()}
                <div id="chart" />
                {this.renderDetail()}
            </div>
        );
    }
}


export default MethodCanaryThread;