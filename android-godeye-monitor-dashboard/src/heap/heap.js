import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import ReactHighcharts from '../../node_modules/react-highcharts'
import {toast} from 'react-toastify';

exporting(Highcharts);

/**
 * Heap
 */
class Heap extends Component {

    constructor(props) {
        super(props);

        this.options = {
            credits: {
                enabled: false
            },
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
                }, {
                    name: 'Max',
                    type: 'line',
                    data: (Heap.initSeries())
                }
            ]
        };
        this.index = 0;
    }


    generateIndex() {
        this.index = this.index + 1;
        return this.index;
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
            let axisData = this.generateIndex() + (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint([axisData, heapInfo.allocatedKb / 1024], false, true, true);
            this.refs.chart.getChart().series[1].addPoint([axisData, heapInfo.freeMemKb / 1024], false, true, true);
            this.refs.chart.getChart().series[2].addPoint([axisData, heapInfo.maxMemKb / 1024], false, true, true);
            this.refs.chart.getChart().redraw(true);
            if (heapInfo.allocatedKb > (heapInfo.maxMemKb * 0.9)) {
                toast.error("Heap memory is running out.(堆内存即将耗尽)")
            }
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Heap(堆内存)
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
