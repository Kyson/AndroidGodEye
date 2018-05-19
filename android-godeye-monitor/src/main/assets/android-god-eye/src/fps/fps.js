import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

import Highcharts from '../../node_modules/highcharts/highstock';
import HighchartsReact from '../../node_modules/highcharts-react-official'

/**
 * Fps
 */
class Fps extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Pss(共享比例物理内存)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <h2>{this.props.fpsInfo ? (this.props.fpsInfo.currentFps + "/" + this.props.fpsInfo.systemFps) : "**/**"}</h2>
                </Panel.Body>
            </Panel>);
    }
}

export default Fps;
