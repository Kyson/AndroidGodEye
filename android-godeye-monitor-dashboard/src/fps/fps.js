import React, {Component} from 'react';
import '../App.css';

import Util from '../libs/util'
import {toast} from 'react-toastify';
import {Card} from 'antd'

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
        if (fpsInfo && fpsInfo.currentFps && fpsInfo.systemFps && fpsInfo.currentFps > 0 && fpsInfo.systemFps > 0) {
            if (fpsInfo.currentFps >= fpsInfo.systemFps * 5 / 6) {
                return 3;
            } else if (fpsInfo.currentFps >= fpsInfo.systemFps / 3) {
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
            <Card>
                <Card.Meta
                    title="FPS(帧率)"
                    description={"SystemRefreshRate(系统帧率):" + ((this.state.fpsInfo && this.state.fpsInfo.systemFps && this.state.fpsInfo.systemFps > 0) ? this.state.fpsInfo.systemFps : "**")}
                />
                <div style={{textAlign: "center"}}>
                <span style={{
                    padding: 16,
                    fontSize: 48,
                    color: this.fpsLevelColor[fpsLevel]
                }}>{(this.state.fpsInfo && this.state.fpsInfo.currentFps && this.state.fpsInfo.currentFps > 0) ? this.state.fpsInfo.currentFps : "**"}</span>
                </div>
            </Card>);
    }
}

export default Fps;
