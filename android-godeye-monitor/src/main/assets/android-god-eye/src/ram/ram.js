import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';


/**
 * RAM信息
 */
class Ram extends Component {

    constructor(props) {
        super(props);
        this.chart = {};
    }

    componentDidMount() {
        let options = {
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
                        format: '<b>{point.name}</b>',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
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
        this.chart = Highcharts.chart('container', options);
    }


    static createSeriresData(ramName, ramValue) {
        return {
            name: ramName,
            y: ramValue
        }
    }

    refreshRam(ramInfo) {
        let datas = [];
        if (ramInfo) {
            let allocatedKb = ramInfo.totalMemKb - ramInfo.availMemKb;
            datas.push(Ram.createSeriresData("allocated", allocatedKb));
            datas.push(Ram.createSeriresData("free", ramInfo.availMemKb));
            let title = "Total:" + (ramInfo.totalMemKb / 1024).toFixed(1) + "M";
            this.chart.setTitle({
                text: title
            });
        } else {
            this.chart.setTitle({
                text: "**"
            });
        }
        this.chart.series[0].setData(datas);
    }


    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Ram(运行时内存)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <div id="container" style={{height: 200, width: "100%"}}>
                    </div>
                </Panel.Body>
            </Panel>);
    }
}

export default Ram;
