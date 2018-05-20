import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel, Modal, Button} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import ReactHighcharts from '../../node_modules/react-highcharts'
import JSONPretty from '../../node_modules/react-json-pretty';

/**
 * Network
 */
class Network extends Component {

    constructor(props) {
        super(props);
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.options = {
            chart: {
                type: 'column'
            },
            title: {
                text: null
            },
            tooltip: {
                shared: true,
                formatter: function () {
                    let point = this.points[0].point;
                    if (point.networkInfo) {
                        return point.networkInfo.url + ' : ' + point.y.toFixed(1) + ' ms <br/>';
                    }
                    return "";
                }
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: "Request time(ms)",
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
                    name: 'Request time',
                    data: (Network.initSeries())
                }
            ]
        };
        this.state = {
            show: false,
            networkInfo: {}
        };
    }

    handleClick(e) {
        if (e.point.networkInfo) {
            this.setState({networkInfo: e.point.networkInfo, show: true});
        }
    }

    handleClose() {
        this.setState({show: false});
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

    refresh(networkInfo) {
        if (networkInfo) {
            let axisData = (new Date()).toLocaleTimeString();
            this.refs.chart.getChart().series[0].addPoint({
                name: axisData,
                y: (networkInfo.endTimeMillis - networkInfo.startTimeMillis),
                networkInfo: networkInfo
            }, true, true, true);
        }
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Network(网络)
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
                        <JSONPretty id="json-pretty" json={this.state.networkInfo}/>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
            </Panel>);
    }
}

export default Network;
