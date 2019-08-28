/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import Highcharts from 'highcharts/highstock';

(xrange)(Highcharts);

class MethodCanaryThread extends Component {

    constructor(props) {
        super(props);
        this.openMethodCanaryThread = this.openMethodCanaryThread.bind(this);
        this.afterSetExtremes = this.afterSetExtremes.bind(this);
        this.state = {
            tt: ""
        };
        this.record = {};
    }

    componentDidMount() {
        this.chart = Highcharts.stockChart('chart', {
            chart: {
                type: 'xrange',
                height: 0.1,
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
                height: 0,
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
                    gridLineWidth: 0,
                    reversed: true,
                    categories: []
                },
                xAxis: {
                    startOnTick: false,
                    endOnTick: false,
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
                categories: [],
                crosshair: false,
            },
            events: {
                selection: function (e) {
                    console.log(e);
                }
            },
            series: [
                {
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
                        }
                    },
                }
            ]
        });
    }

    afterSetExtremes(e) {
        this.setState({
            tt: e.min + "-" + e.max
        });
    }

    openMethodCanaryThread(threadName, record) {
        this.record = record;
        let methodInfos = {};
        for (let i = 0; i < record.methodInfoOfThreadInfos.length; i++) {
            if (threadName === MethodCanaryThread.getThreadNameByThreadInfo(record.methodInfoOfThreadInfos[i].threadInfo)) {
                methodInfos = record.methodInfoOfThreadInfos[i].methodInfos
            }
        }
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
                color: Util.getCommonColors()[i % 5],
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
            categories.push("");
            main_height = main_height + 22
        }
        this.chart.update({
            chart: {
                height: main_height,
            },
            navigator: {
                height: thumbnail_height,
                yAxis: {
                    max: categories.length - 2,
                },
            },
            yAxis: {
                categories: categories,
            },
            series: [
                {
                    data: datas
                }
            ]
        })
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

    render() {
        return (
            <Row>
                <Col span={24}>
                    <div id="chart"/>
                </Col>
            </Row>
        );
    }
}


export default MethodCanaryThread;