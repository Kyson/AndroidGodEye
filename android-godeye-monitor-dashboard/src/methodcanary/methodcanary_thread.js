/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

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
        this.afterSetExtremesTimerId = null;
        this.state = {
            start: 0,
            end: 0
        }
    }

    afterSetExtremes(e) {
        this.setState({
            start: e.min,
            end: e.max
        });
        clearTimeout(this.afterSetExtremesTimerId);
        this.afterSetExtremesTimerId = setTimeout(() => {
            this.refs.methodCanaryThreadTree.refresh(e.min, e.max, this.methodInfos);
        }, 500);
    }

    openMethodCanaryThread(threadName, record) {
        let methodInfos = [];
        for (let i = 0; i < record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanaryThread.getThreadNameByThreadInfo(record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
        methodInfos.sort((a, b) => {
            if (a.start === b.start) {
                if (a.end === b.end) {
                    return a.stack - b.stack;
                }
                return b.end - a.end;
            }
            return a.start - b.start;
        });
        const min = record.start;
        const max = record.end;
        const datas = [];
        let maxStack = 0;
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
        for (let i = 0; i < (maxStack + 1); i++) {
            categories.push("");
        }
        const thumbnail_point_width = 5;
        const main_point_width = 20;
        let thumbnail_height = (maxStack + 4) * thumbnail_point_width * 1.5;
        let main_height = 120 + thumbnail_height + (maxStack + 1) * main_point_width * 1.2;
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
                            return Util.getFormatDuration(this.value);
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
                        s += '<b>' + "cost " + Util.getFormatDuration(e.methodEvent.end - e.methodEvent.start) + '</b>'
                        s += '<br/>' + e.methodEvent.className + "#" + e.methodEvent.methodName;
                        s += '<br/> from ' + e.methodEvent.start + " to " + e.methodEvent.end;
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
                        return Util.getFormatDuration(this.value);
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
                                return className + "#" + this.point.methodEvent.methodName
                            }
                            return null
                        }
                    },
                }
            ]
        });
        methodInfos.sort((a, b) => {
            if (a.stack === b.stack) {
                return a.start - b.start;
            }
            return a.stack - b.stack;
        });
        this.methodInfos = methodInfos;
        this.refs.methodCanaryThreadTree.refresh(min, max, this.methodInfos);
        this.setState({start: min, end: max});
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    renderTimeRange() {
        if (this.state.end !== 0 || this.state.start !== 0) {
            return <p>{"Selected duration: " + Util.getFormatDuration(this.state.end - this.state.start) + ", From " + Util.getFormatDuration(this.state.start) + " to " + Util.getFormatDuration(this.state.end)}</p>
        }
        return <div/>
    }

    render() {
        return (
            <div>
                <div id="chart"/>
                {this.renderTimeRange()}
                <MethodCanaryThreadTree ref="methodCanaryThreadTree"/>
            </div>
        );
    }
}


export default MethodCanaryThread;