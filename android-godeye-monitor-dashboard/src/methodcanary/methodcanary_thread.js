/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import Highcharts from 'highcharts/highstock.src';
import MethodCanaryThreadTree from "./methodcanary_thread_tree";

(xrange)(Highcharts);

class MethodCanaryThread extends Component {

    constructor(props) {
        super(props);
        this.openMethodCanaryThread = this.openMethodCanaryThread.bind(this);
        this.afterSetExtremes = this.afterSetExtremes.bind(this);
        this.methodInfos = [];
        this.state = {
            start: 0,
            end: 0
        }
    }

    componentDidMount() {

    }

    afterSetExtremes(e) {
        this.setState({
            start: e.min,
            end: e.max
        });
        this.refs.methodCanaryThreadTree.refresh(e.min, e.max, this.methodInfos);
    }

    openMethodCanaryThread(threadName, record) {
        let methodInfos = [];
        for (let i = 0; i < record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanaryThread.getThreadNameByThreadInfo(record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
        methodInfos.sort((a, b) => {
            return a.start - b.start;
        });
        const min = record.start;
        const max = record.end;
        const datas = [];
        let maxStack = 0;
        datas.push({
            x: min,
            x2: max,
            y: 0,
            color: "#00000000",
            methodEvent: null
        });
        for (let i = 0; i < methodInfos.length; i++) {
            datas.push({
                x: methodInfos[i].start === 0 ? min : methodInfos[i].start,
                x2: (methodInfos[i].end === 0 ? max : methodInfos[i].end),
                y: methodInfos[i].stack,
                color: Util.getCommonColors()[i % Util.getCommonColors().length],
                methodEvent: methodInfos[i]
            });
            if (methodInfos[i].stack > maxStack) {
                maxStack = methodInfos[i].stack
            }
        }

        const categories = [];
        let thumbnail_height = 30;
        for (let i = 0; i < (maxStack + 1); i++) {
            categories.push("");
            thumbnail_height = thumbnail_height + 7
        }
        let main_height = 120 + thumbnail_height;
        for (let i = 0; i < (maxStack + 1); i++) {
            main_height = main_height + 22
        }
        methodInfos.sort((a, b) => {
            if (a.stack === b.stack) {
                return a.start - b.start;
            }
            return a.stack - b.stack;
        });
        this.methodInfos = methodInfos;

        const chart = Highcharts.stockChart('chart', {
            chart: {
                type: 'xrange',
                height: main_height,
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
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: 7
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    min: 0,
                    max: categories.length,
                    gridLineWidth: 0,
                    reversed: true,
                    startOnTick: false,
                    endOnTick: false,
                    categories: []
                },
                xAxis: {
                    startOnTick: false,
                    endOnTick: false,
                    ordinal: false,
                    labels: {
                        formatter: function () {
                            return (this.value / 1000000000).toFixed(3) + "s";
                        }
                    },
                }
            },
            tooltip: {
                formatter: function () {
                    let s = "";
                    for (let i = 0; i < this.points.length; i += 1) {
                        const e = this.points[i].point;
                        if (e.methodEvent) {
                            s += '<b>' + "cost " + ((e.methodEvent.end - e.methodEvent.start) / 1000000).toFixed(2) + " ms" + '</b>'
                            s += '<br/>' + e.methodEvent.className + "#" + e.methodEvent.methodName;
                            s += '<br/> from ' + e.methodEvent.start + " to " + e.methodEvent.end;
                        }
                    }
                    if (s === "") {
                        return null;
                    }
                    return s;
                }
            },
            scrollbar: {
                enabled: true
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
                labels: {
                    formatter: function () {
                        return (this.value / 1000000000).toFixed(3) + "s";
                    }
                },
                ordinal: false,
                lineWidth: 0.5,
                gridLineWidth: 0.5,
                startOnTick: false,
                endOnTick: false,
                events: {
                    afterSetExtremes: this.afterSetExtremes
                },
            },
            yAxis: {
                title: {
                    text: null
                },
                min: 0,
                gridLineWidth: 0,
                reversed: true,
                startOnTick: false,
                endOnTick: false,
                categories: categories,
                crosshair: false,
            },
            series: [
                {
                    data: datas,
                    borderRadius: 0,
                    pointPadding: 0,
                    groupPadding: 0,
                    pointWidth: 20,
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
                                return className + "#" + this.point.methodEvent.methodName
                            }
                            return null
                        }
                    },
                }
            ]
        });
        this.refs.methodCanaryThreadTree.refresh(min, max, methodInfos);
        this.setState({start: min, end: max})
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    render() {
        return (
            <div>
                <div id="chart"/>
                <p>{((this.state.end - this.state.start)) + "s, from " + (this.state.start ) + " to " + (this.state.end )}</p>
                <MethodCanaryThreadTree ref="methodCanaryThreadTree"/>
            </div>
        );
    }
}


export default MethodCanaryThread;