import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import ReactHighcharts from '../../node_modules/react-highcharts'

/**
 * Traffic
 */
class Traffic extends Component {

    constructor(props) {
        super(props);
        this.options = {
            title: {
                text: null
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
                type: 'category'
            },
            yAxis: {
                title: {
                    text: "Traffic(KB/S)",
                    align: "middle",
                },
                min: 0
            },
            series: [
                {
                    name: 'DeviceRX',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'DeviceTX',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'AppRX',
                    data: (Traffic.initSeries())
                },
                {
                    name: 'AppTX',
                    data: (Traffic.initSeries())
                }
            ]
        };
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

    refresh(trafficInfo) {
        if (trafficInfo) {
            let axisData = (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, trafficInfo.rxTotalRate], false, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, trafficInfo.txTotalRate], false, true, true);
            this.refs.chart.getChart().series[2].addPoint([axisData, trafficInfo.rxUidRate], false, true, true);
            this.refs.chart.getChart().series[3].addPoint([axisData, trafficInfo.txUidRate], false, true, true);
            this.refs.chart.getChart().redraw(true);
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Traffic
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

export default Traffic;
