import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import ReactHighcharts from '../../node_modules/react-highcharts'

/**
 * Heap
 */
class Heap extends Component {

    constructor(props) {
        super(props);

        this.options = {
            chart: {
                type: 'area'
            },
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
                        tip = tip + seriesName + ' : ' + point.y.toFixed(1) + ' MB<br/>';
                    }
                    return tip;
                }
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: "Heap(MB)",
                    align: "middle",
                },
                min: 0
            },
            series: [
                {
                    name: 'Allocated',
                    stack: 'heap',
                    stacking: 'normal',
                    data: (Heap.initSeries())
                },
                {
                    name: 'Free',
                    stack: 'heap',
                    stacking: 'normal',
                    data: (Heap.initSeries())
                }
            ]
        };
    }

    static initSeries() {
        let data = [];
        for (let i = 0; i < 20; i++) {
            data.push({
                x: i, y: 0
            });
        }
        return data;
    }

    refresh(heapInfo) {
        if (heapInfo) {
            let axisData = (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, heapInfo.allocatedKb / 1024], true, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, heapInfo.freeMemKb / 1024], true, true, true);
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Heap
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

export default Heap;
