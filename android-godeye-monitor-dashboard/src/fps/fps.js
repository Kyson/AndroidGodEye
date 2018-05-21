import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

/**
 * Fps
 */
class Fps extends Component {

    constructor(props) {
        super(props);
        this.state = {
            fpsInfo: {}
        }
    }

    refresh(fpsInfo) {
        this.setState({fpsInfo});
    }

    render() {
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>FPS(帧率)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <h1 style={{textAlign: "center"}}>{(this.state.fpsInfo && this.state.fpsInfo.currentFps && this.state.fpsInfo.systemFps) ? (this.state.fpsInfo.currentFps + "/" + this.state.fpsInfo.systemFps) : "**/**"}</h1>
                </Panel.Body>
            </Panel>);
    }
}

export default Fps;
