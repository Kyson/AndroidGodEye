import React, {Component} from 'react';
import '../App.css';

import Util from '../libs/util'
import {Card, message, Statistic} from 'antd'

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
            message.error("Low fps.(掉帧严重)");
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
                <div style={{textAlign: "center"}}>
                    <Statistic title="FPS(帧率)"
                               value={(this.state.fpsInfo && this.state.fpsInfo.currentFps && this.state.fpsInfo.currentFps > 0) ? this.state.fpsInfo.currentFps : "**"}
                               suffix={"/" + ((this.state.fpsInfo && this.state.fpsInfo.systemFps && this.state.fpsInfo.systemFps > 0) ? this.state.fpsInfo.systemFps : "**")}
                               valueStyle={{fontSize: 108, color: this.fpsLevelColor[fpsLevel], padding: 30}}/>
                </div>
            </Card>);
    }
}

export default Fps;
