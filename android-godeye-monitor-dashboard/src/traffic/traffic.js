import React, {Component} from 'react';
import '../App.css';

import ReactHighcharts from '../../node_modules/react-highcharts'
import {Card} from 'antd'
import TrafficInfo from "./traffic_info";


/**
 * Traffic
 */
class Traffic extends Component {

    constructor(props) {
        super(props);
        this.options = {
            chart: {
                spacingLeft: 0,
                spacingRight: 0,
                height: 200,
                type: "line"
            },
            exporting: {
                enabled: false
            },
            legend: {
                enabled: false
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
                    let tip = "";
                    for (let i = 0; i < this.points.length; i++) {
                        let point = this.points[i].point;
                        let seriesName = this.points[i].series.name;
                        tip = tip + seriesName + ' : ' + point.y.toFixed(3) + ' KB/S <br/>';
                    }
                    return tip;
                }
            },
            xAxis: {
                type: 'category',
                labels: {
                    enabled: false
                },
                lineWidth: 0,
                tickLength: 0,
                gridLineWidth: 1
            },
            yAxis: {
                min: 0,
                visible: false
            },
            plotOptions: {
                line: {
                    lineWidth: 2,
                    marker: {
                        enabled: false
                    }
                }
            },
            series: [
                {
                    name: 'DeviceRX(设备下行)',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'DeviceTX(设备上行)',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'AppRX(App下行)',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'AppTX(App上行)',
                    data: (Traffic.initSeries())
                }
            ]
        };
        this.index = 0;
    }

    static initSeries() {
        let data = [];
        for (let i = 0; i < 20; i++) {
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

    refresh(trafficInfo) {
        if (trafficInfo) {
            let axisData = this.generateIndex() + (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, trafficInfo.rxTotalRate], false, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, trafficInfo.txTotalRate], false, true, true);
            this.refs.chart.getChart().series[2].addPoint([axisData, trafficInfo.rxUidRate], false, true, true);
            this.refs.chart.getChart().series[3].addPoint([axisData, trafficInfo.txUidRate], false, true, true);
            this.refs.chart.getChart().redraw(true);
        }
        this.refs.info.refresh(trafficInfo);
    }

    render() {
        return (
            <Card title="Traffic(流量)">
                <TrafficInfo ref="info"/>
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
            </Card>);
    }
}

export default Traffic;
