import React, {Component} from 'react';
import '../App.css';

import {Card, Row, Col} from 'antd'

import ReactHighcharts from '../../node_modules/react-highcharts'
import Util from '../libs/util'
import BatteryDetail from "./battery_detail";


/**
 * 电池信息
 */
class BatteryInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {batteryInfo: {}}
        this.options = {
            chart: {
                height: 248,
                spacing: [0, 0, 0, 0]
            },
            exporting: {
                enabled: false
            },
            title: {
                floating: true,
                text: 'Battery',
                align: 'center',
                verticalAlign: 'middle',
                style: {
                    fontSize: 13
                },
            },
            credits: {
                enabled: false
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:13px;color:' +
                        'black' + '">' +
                        this.point.percentage.toFixed(1) + "%"
                        + '</span></div>'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: false,
                        format: '<b>{point.name}</b>'
                    },
                }
            },
            series: [{
                type: 'pie',
                name: 'battery',
                innerSize: '90%',
                data: []
            }]
        };
    }

    static createSeriresData(name, value, color) {
        return {
            name: name,
            y: value,
            color: color
        }
    }

    refresh(batteryInfo) {
        let datas = [];
        if (batteryInfo) {
            datas.push(BatteryInfo.createSeriresData("Quantity", batteryInfo.level, Util.getGreen()));
            datas.push(BatteryInfo.createSeriresData("Other", batteryInfo.scale - batteryInfo.level, Util.getLightGrey()));
            // TODO KYSON STATUS
            this.options.title.text = batteryInfo.status === 'ok' ? "Battery is Charging" : "Battery";
        } else {
            this.options.title.text = "**";
        }
        this.options.series[0].data = datas;
        this.refs.chart.getChart().update(this.options);
        this.refs.detail.refresh(batteryInfo)
    }

    render() {
        return (
            <Card>
                <Row>
                    <Col span={12}> <ReactHighcharts
                        ref="chart"
                        config={this.options}
                    /></Col>
                    <Col span={12}>
                        <BatteryDetail ref="detail"/>
                    </Col>
                </Row>
            </Card>);
    }
}

export default BatteryInfo;
