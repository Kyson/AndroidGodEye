import React, {Component} from 'react';
import '../App.css';

import {Tag} from 'antd'

import Util from '../libs/util'

/**
 * 应用基本信息
 */
class AppInfo extends Component {

    constructor(props) {
        super(props);
        AppInfo.renderLabel = AppInfo.renderLabel.bind(this);
        this.state = {
            appInfo: {}
        }
    }

    componentDidMount() {
        this.onWsOpenCallback = () => {
            this.props.globalWs.sendMessage('{"moduleName": "appInfo"}');
        };
        this.props.globalWs.registerCallback(this.onWsOpenCallback);
    }

    componentWillUnmount() {
        this.props.globalWs.unregisterCallback(this.onWsOpenCallback);
    }

    refresh(appInfo) {
        this.setState({appInfo});
    }

    render() {
        return (
            <div>
                <h1 style={{color: 'white', textAlign: "center", marginTop: 16}}>
                    {this.state.appInfo ? this.state.appInfo.appName : "**"}
                </h1>
                <div style={{
                    paddingTop: 10,
                    paddingBottom: 10,
                    textAlign: "center"
                }}>{AppInfo.renderLabel(this.state.appInfo ? this.state.appInfo.labels : [])}</div>
            </div>
        );
    }

    static renderLabel(labels) {
        if (labels) {
            let items = [];
            let styles = Util.getCommonColors();
            let styleCount = styles.length;
            for (let i = 0; i < labels.length; i++) {
                items.push(<Tag style={{marginRight: 5, color: styles[i % styleCount]}}
                                key={"appinfo" + i}>{labels[i]}</Tag>);
            }
            return items;
        }
    }
}

export default AppInfo;
