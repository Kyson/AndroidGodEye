import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import ReactHighcharts from '../../node_modules/react-highcharts'

/**
 * RAM信息
 */
class Ram extends Component {

    constructor(props) {
        super(props);
        this.options = {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false,
                spacing: [0, 0, 0, 0]
            },
            title: {
                text: "Ram",
                align: 'center',
                verticalAlign: 'middle',
                y: 50
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:18px;color:' +
                        ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">' +
                        this.point.name + ":" +
                        (this.point.y / 1024).toFixed(1) + 'M,' +
                        this.point.percentage.toFixed(1) + "%"
                        + '</span></div>'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>'
                    },
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '75%']
                }
            },
            series: [{
                type: 'pie',
                name: 'ram',
                innerSize: '80%',
                data: []
            }]
        };
    }

    createSeriresData(name, value) {
        return {
            name: name,
            y: value
        }
    }

    refresh(info) {
        let datas = [];
        if (info) {
            let allocatedKb = info.totalMemKb - info.availMemKb;
            datas.push(this.createSeriresData("allocated", allocatedKb));
            datas.push(this.createSeriresData("free", info.availMemKb));
            let title = "Total:" + (info.totalMemKb / 1024).toFixed(1) + "M";
            this.options.title.text = title;
        } else {
            this.options.title.text = "**";
        }
        this.options.series[0].data = datas;
        this.refs.chart.getChart().update(this.options);
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Ram(运行时内存)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactHighcharts
                        ref="chart"
                        config={this.options}
                    />
                </Panel.Body>
            </Panel>);
    }
}

export default Ram;
