import React, {Component} from 'react';
import '../App.css';

import JSONPretty from '../../node_modules/react-json-pretty';

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import ReactHighcharts from '../../node_modules/react-highcharts'
import {toast} from 'react-toastify';
import {Card, Modal, Timeline, Row, Col} from 'antd'

exporting(Highcharts);

/**
 * Pageload
 */
class Pageload extends Component {

    constructor(props) {
        super(props);
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.options = {
            chart: {
                type: 'column',
                spacingLeft: 0,
                spacingRight: 0,
                height: 400
            },
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            tooltip: {
                shared: true,
                formatter: function () {
                    let firstPoint = this.points[0].point;
                    let pageId = "unknown";
                    let pageName = "unknown";
                    if (firstPoint && firstPoint.pageloadInfo) {
                        pageId = firstPoint.pageloadInfo.pageId;
                        pageName = firstPoint.pageloadInfo.pageName;
                    }
                    return pageId + '<br/>' + pageName + '<br/>';
                }
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: "Page load time(ms)",
                    align: "middle",
                },
                min: 0
            },
            plotOptions: {
                series: {
                    point: {
                        events: {
                            click: this.handleClick
                        }
                    }
                },
                column: {
                    grouping: false,
                    shadow: false,
                    borderWidth: 0
                }
            },
            series: [
                {
                    name: 'DrawTime(绘制耗时)',
                    color: 'rgba(126,86,134,.9)',
                    pointPadding: 0.4,
                    pointPlacement: -0.1,
                    data: (Pageload.initSeries())
                }, {
                    name: 'LoadTime(加载耗时)',
                    color: 'rgba(165,170,217,0.5)',
                    pointPadding: 0.2,
                    pointPlacement: -0.1,
                    data: (Pageload.initSeries())
                }
            ]
        };
        this.state = {
            show: false,
            pageloadInfo: {}
        };
        this.index = 0;
    }

    handleClick(e) {
        if (e.point.pageloadInfo) {
            this.setState({pageloadInfo: e.point.pageloadInfo, show: true});
        }
    }

    handleClose() {
        this.setState({show: false});
    }

    static initSeries() {
        let data = [];
        for (let i = 0; i < 30; i++) {
            data.push({
                x: i,
                y: 0
            });
        }
        return data;
    }

    generateIndex() {
        this.index = this.index + 1;
        return this.index;
    }

    refresh(pageloadInfo) {
        if (pageloadInfo && pageloadInfo.loadTimeInfo) {
            let axisData = pageloadInfo.pageId;
            let drawTime = pageloadInfo.loadTimeInfo.didDrawTime - pageloadInfo.loadTimeInfo.createTime;
            let loadTime = pageloadInfo.loadTimeInfo.loadTime - pageloadInfo.loadTimeInfo.createTime;
            if (drawTime < 0) {
                drawTime = 0;
            }
            if (loadTime < 0) {
                loadTime = 0;
            }
            this.refs.chart.getChart().series[0].addPoint({//绘制时间
                name: axisData,
                y: drawTime,
                pageloadInfo: pageloadInfo
            }, false, true, true);
            this.refs.chart.getChart().series[1].addPoint({//加载时间
                name: axisData,
                y: loadTime,
                pageloadInfo: pageloadInfo
            }, false, true, true);
            this.refs.chart.getChart().redraw(true);
            if (drawTime >= 1000) {
                toast.error("Page rendering performance poor.(页面绘制性能差)");
            }
        }
    }

    render() {
        let drawTime = 0;
        let loadTime = 0;
        if (this.state.pageloadInfo && this.state.pageloadInfo.loadTimeInfo) {
            drawTime = this.state.pageloadInfo.loadTimeInfo.didDrawTime - this.state.pageloadInfo.loadTimeInfo.createTime;
            loadTime = this.state.pageloadInfo.loadTimeInfo.loadTime - this.state.pageloadInfo.loadTimeInfo.createTime;
            if (drawTime < 0) {
                drawTime = 0;
            }
            if (loadTime < 0) {
                loadTime = 0;
            }
        }
        const content = `Page ${this.state.pageloadInfo.pageName} draw cost ${drawTime}ms, load cost ${loadTime}ms`;
        return (
            <Card title="Pageload(页面加载)">
                <Row gutter={16} align="top">
                    <Col span={16}>
                        <ReactHighcharts
                            ref="chart"
                            config={this.options}
                        />
                    </Col>
                    <Col span={8} style={{height: 400, overflow: 'auto'}}>
                        <Timeline>
                            <Timeline.Item color="green">Create a services site 2015-09-01</Timeline.Item>
                            <Timeline.Item color="green">Create a services site 2015-09-01</Timeline.Item>
                            <Timeline.Item color="red">
                                <p>ClassName@id</p>
                                <p>CREATE at 2019-09-02 18:00:03</p>
                                <p>Draw cost 54ms</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                            <Timeline.Item>
                                <p>Technical testing 1</p>
                                <p>Technical testing 2</p>
                                <p>Technical testing 3 2015-09-01</p>
                            </Timeline.Item>
                        </Timeline>

                    </Col>
                </Row>
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Pageload detail" closable={true}
                       onOk={this.handleClose}>
                    <span><strong>{content}</strong></span><br/>
                    <JSONPretty id="json-pretty" json={this.state.pageloadInfo}/>
                </Modal>
            </Card>);
    }
}


export default Pageload;
