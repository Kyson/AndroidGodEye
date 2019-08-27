/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';

import {Card, Button, Row, Col} from 'antd'
import xrange from 'highcharts/modules/xrange';
import Highcharts from 'highcharts/highstock';

(xrange)(Highcharts);

class MethodCanaryThread extends Component {

    constructor(props) {
        super(props);
        this.state = {
            MethodCanaryStatus: {}
        };
        this.record = {};
        this.openThread = this.openThread.bind(this);
    }

    componentDidMount() {
        Highcharts.stockChart('container2', {
            chart: {
                type: 'xrange',
                height: 500,
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
                series: {
                    type: 'xrange',
                    pointPlacement: 0.5,
                    pointPadding: 0.25
                },
                yAxis: {
                    min: 0,
                    max: 5,
                    reversed: true,
                    categories: []
                }
            },
            scrollbar: {
                enabled: true
            },
            rangeSelector: {
                enabled: true,
                selected: 0
            },
            xAxis: {
                title: {
                    text: 'Time(ms)',
                    align: "high",
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    }
                },
                lineWidth: 0,
                gridLineWidth: 1,
            },
            yAxis: {
                categories: ['制作产品原型', '开发', '测试'],
                reversed: true,
                title: null,
            },
            series: [
                {
                    data: [
                        {
                            y: 0,
                            x: 0,
                            x2: 10000,
                            color: 'rgba(0, 0, 0, 0.1)',
                        },
                        {
                            y: 1,
                            x: 0,
                            x2: 10000,
                            color: 'rgba(0, 0, 0, 0.1)',
                        },
                        {
                            y: 2,
                            x: 0,
                            x2: 10000,
                            color: 'rgba(0, 0, 0, 0.1)',
                        },
                        {
                            y: 0,
                            x: 0,
                            x2: 1000,
                            color: '#00c73c',
                        },
                        {
                            y: 0,
                            x: 1200,
                            x2: 2500,
                            color: '#f7c41d',
                        },
                        {
                            y: 1,
                            x: 2500,
                            x2: 3000,
                            color: '#00c73c',
                        },
                        {
                            y: 2,
                            x: 3000,
                            x2: 4000,
                            color: '#f84d3a',
                        }
                    ]
                }
            ]
        });
    }

    openThread(threadName) {
        // let methodInfos = {};
        // for (let i = 0; i < this.record.methodInfoOfThreadInfos.length; i++) {
        //     if (threadName === MethodCanary.getThreadNameByThreadInfo(this.record.methodInfoOfThreadInfos[i].threadInfo)) {
        //         methodInfos = this.record.methodInfoOfThreadInfos[i].methodInfos
        //     }
        // }
        // const min = this.record.start;
        // const max = this.record.end;
        // const datas = [];
        // let maxStack = 0;
        // for (let i = 0; i < methodInfos.length; i++) {
        //     datas.push({
        //         x: methodInfos[i].start === 0 ? min : methodInfos[i].start,
        //         x2: (methodInfos[i].end === 0 ? max : methodInfos[i].end),
        //         y: methodInfos[i].stack,
        //         methodEvent: methodInfos[i]
        //     });
        //     if (methodInfos[i].stack > maxStack) {
        //         maxStack = methodInfos[i].stack
        //     }
        // }
        // const categories = [];
        // let height = 90;
        // for (let i = 0; i < (maxStack + 1); i++) {
        //     categories.push("");
        //     height = height + 35
        // }
        // this.refs.chartForThread.getChart().update({
        //     chart: {
        //         height: height
        //     },
        //     series: [{
        //         data: datas,
        //     }],
        //     yAxis: {
        //         categories: categories
        //     },
        //     xAxis: {
        //         min: min,
        //         max: max
        //     },
        // });
    }

    static getThreadNameByThreadInfo(threadInfo) {
        return "id:[" + threadInfo.id + "],name:[" + threadInfo.name + "]"
    }

// <ReactHighstock
// ref="chartForThread"
// config={this.optionsForThread}
// />
    render() {
        return (
            <Row>
                <Col span={24}>
                    <div id="container2"></div>
                </Col>
            </Row>
        );
    }
}


export default MethodCanaryThread;