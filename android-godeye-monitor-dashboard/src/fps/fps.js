import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import Util from '../libs/util'
import {toast} from 'react-toastify';


/**
 * Fps
 */
class Fps extends Component {

    constructor(props) {
        super(props);
        this.state = {
            fpsInfo: {},
            level: 0
        };
        this.fpsLevelColor = [Util.getGrey(), Util.getRed(), Util.getOrange(), Util.getGreen()];
    }

    refresh(fpsInfo) {
        const level = Fps._parseFpsLevel(fpsInfo);
        if (level === 1) {
            toast.error("Low fps.(掉帧严重)");
        }
        this.setState({
            fpsInfo: fpsInfo,
            level: level
        });
    }

    static _parseFpsLevel(fpsInfo) {
        if (fpsInfo && fpsInfo.currentFps && fpsInfo.systemFps) {
            if (fpsInfo.currentFps >= fpsInfo.systemFps * 5 / 6) {
                return 3;
            } else if (fpsInfo.currentFps >= fpsInfo.systemFps / 2) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    render() {
        let fpsLevel = this.state.level;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>FPS(帧率)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <h1 style={{
                        textAlign: "center",
                        color: this.fpsLevelColor[fpsLevel]
                    }}>{(this.state.fpsInfo && this.state.fpsInfo.currentFps) ? this.state.fpsInfo.currentFps : "**"}</h1>
                    <p ><span
                        style={{fontSize: 8}}>SystemRefreshRate(系统帧率):&nbsp;&nbsp;</span><strong>{(this.state.fpsInfo && this.state.fpsInfo.systemFps) ? this.state.fpsInfo.systemFps : "**"}</strong>
                    </p>
                </Panel.Body>
            </Panel>);
    }
}

export default Fps;
