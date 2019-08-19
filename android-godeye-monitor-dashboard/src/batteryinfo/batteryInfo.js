import React, {Component} from 'react';
import '../App.css';

import {Card, Modal} from 'antd'

import ReactHighcharts from '../../node_modules/react-highcharts'
import Util from '../libs/util'
import JSONPretty from '../../node_modules/react-json-pretty';


/**
 * 电池信息
 */
class BatteryInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {batteryInfo: {}, show: false};
        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
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
                text: 'Battery(电池)',
                align: 'center',
                verticalAlign: 'middle',
                style: {
                    fontSize: 13
                },
                useHTML: true
            },
            credits: {
                enabled: false
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:13px;color:' +
                        'black">' +
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
                    events: {
                        click: this.handleShow
                    }
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


    handleShow() {
        this.setState({show: true});
    }

    handleClose() {
        this.setState({show: false});
    }

    refresh(batteryInfo) {
        let datas = [];
        if (batteryInfo) {
            datas.push(BatteryInfo.createSeriresData("Quantity", batteryInfo.level, Util.getGreen()));
            datas.push(BatteryInfo.createSeriresData("Other", batteryInfo.scale - batteryInfo.level, Util.getLightGrey()));
            this.options.title.text = `Battery(电池) | ${batteryInfo.status}`;
        } else {
            this.options.title.text = "Battery(电池)";
        }
        this.options.series[0].data = datas;
        this.refs.chart.getChart().update(this.options);
        this.setState({batteryInfo})
    }

    render() {
        return (
            <Card>
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Battery Detail" closable={true}
                       onOk={this.handleClose} width={800}>
                    <JSONPretty id="json-pretty" json={this.state.batteryInfo}/>
                </Modal>
            </Card>);
    }
}

export default BatteryInfo;
