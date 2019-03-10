import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel, Modal, Button} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import ReactHighcharts from '../../node_modules/react-highcharts'
import JSONPretty from '../../node_modules/react-json-pretty';
import {toast} from 'react-toastify';

/**
 * Block
 */
class Block extends Component {

    constructor(props) {
        super(props);
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.options = {
            credits: {
                enabled: false
            },
            chart: {
                type: 'column'
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
                        tip = tip + seriesName + ' : ' + point.y.toFixed(1) + ' ms <br/>';
                    }
                    return tip;
                }
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: "Block Time(ms)",
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
                }
            },
            series: [
                {
                    name: 'Block Time',
                    data: (Block.initSeries())
                }
            ]
        };
        this.state = {
            show: false,
            blockInfo: {}
        };
        this.index = 0;
    }

    handleClick(e) {
        this.setState({blockInfo: e.point.blockInfo, show: true});
    }

    handleClose() {
        this.setState({show: false});
    }

    static initSeries() {
        let data = [];
        for (let i = 0; i < 10; i++) {
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

    refresh(blockInfo) {
        if (blockInfo) {
            let axisData = this.generateIndex() + (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint({
                name: axisData,
                y: blockInfo.blockTime,
                blockInfo: blockInfo
            }, false, true, true);
            this.refs.chart.getChart().redraw(true);
            if (blockInfo.blockTime >= 2000) {
                toast.error("Block!(发生卡顿)");
            }
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Block(卡顿)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactHighcharts
                        ref="chart"
                        config={this.options}
                    />
                </Panel.Body>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header>
                        <Modal.Title>Block detail</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <JSONPretty id="json-pretty"
                                    json={this.state.blockInfo.blockBaseinfo ? this.state.blockInfo.blockBaseinfo : "No detail found, maybe it is a short block."}/>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
            </Panel>);
    }
}

export default Block;
