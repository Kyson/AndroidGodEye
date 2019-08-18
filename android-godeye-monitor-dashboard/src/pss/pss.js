import React, {Component} from 'react';
import '../App.css';

import ReactHighcharts from '../../node_modules/react-highcharts'
import {Card} from 'antd'

/**
 * PSS信息
 */
class Pss extends Component {

    constructor(props) {
        super(props);
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
                text: 'Pss',
                align: 'center',
                verticalAlign: 'middle',
                style: {
                    fontSize: 13
                }
            },
            credits: {
                enabled: false
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:13px;color:' +
                        'black' + '">' +
                        this.point.name + ":" +
                        (this.point.y / 1024).toFixed(2) + 'M,' +
                        this.point.percentage.toFixed(1) + "%"
                        + '</span></div>'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: false,
                        format: '<b>{point.name}</b>',
                        style: {
                            color: 'black'
                        }
                    },
                }
            },
            series: [{
                type: 'pie',
                name: 'pss',
                innerSize: '90%',
                data: []
            }]
        };
    }

    static createSeriresData(name, value) {
        return {
            name: name,
            y: value
        }
    }

    refresh(info) {
        let datas = [];
        if (info) {
            let unknownPssKb = info.totalPssKb - info.dalvikPssKb - info.nativePssKb - info.otherPssKb;
            datas.push(Pss.createSeriresData("dalvik", info.dalvikPssKb));
            datas.push(Pss.createSeriresData("native", info.nativePssKb));
            datas.push(Pss.createSeriresData("other", info.otherPssKb));
            datas.push(Pss.createSeriresData("unknown", unknownPssKb));
            this.options.title.text = "Pss(共享比例物理内存)<br/>" + (info.totalPssKb / 1024).toFixed(2) + "M";
        } else {
            this.options.title.text = "**";
        }
        this.options.series[0].data = datas;
        this.refs.chart.getChart().update(this.options);
    }

    render() {
        return (
            <Card>
                <ReactHighcharts
                    ref="chart"
                    config={this.options}
                />
            </Card>);
    }
}

export default Pss;
