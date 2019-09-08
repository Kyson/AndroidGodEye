/* eslint-disable react/prop-types */
/* eslint-disable react/jsx-no-target-blank */
import React, {Component} from 'react';
import '../App.css';

import {Drawer, Button, Row, Col, Typography} from 'antd'

/**
 * 应用基本信息
 */
class AppInfo extends Component {


    static renderLabel(labels) {
        if (labels) {
            let items = [];
            for (let i = 0; i < labels.length; i++) {
                items.push(
                    <Row key={"appInfo" + i} type="flex" justify="center" align="middle" style={{marginTop: 8}}>
                        <Col span={9} style={{textAlign: "right"}}>
                            <Typography.Text>
                                {labels[i].name}
                            </Typography.Text>
                        </Col>
                        <Col span={1}>
                            <Typography.Text>
                                :
                            </Typography.Text>
                        </Col>
                        <Col span={14} style={{textAlign: "left"}}>
                            <Typography.Text underline={!!labels[i].url}>
                                {labels[i].url ? (
                                    <a href={labels[i].url} target="_blank">{labels[i].value}</a>) : (labels[i].value)}
                            </Typography.Text>
                        </Col>
                    </Row>);
            }
            return items;
        }
    }

    constructor(props) {
        super(props);
        this.showDrawer = this.showDrawer.bind(this);
        this.onClose = this.onClose.bind(this);
        this.state = {
            appInfo: {},
            visible: false
        }
    }

    componentDidMount() {
        this.onWsOpenCallback = () => {
            this.props.globalWs.sendMessage('{"moduleName": "appInfo"}');
            this.intervalId = setInterval(() => {
                this.props.globalWs.sendMessage('{"moduleName": "appInfo"}');
            }, 5000);
        };
        this.props.globalWs.registerCallback(this.onWsOpenCallback);
    }

    componentWillUnmount() {
        this.props.globalWs.unregisterCallback(this.onWsOpenCallback);
        clearInterval(this.intervalId);
    }

    refresh(appInfo) {
        this.setState({appInfo});
    }

    showDrawer() {
        this.setState({
            visible: true,
        });
    }

    onClose() {
        this.setState({
            visible: false,
        });
    }

    render() {
        return (
            <Row>
                <Col span={2}>
                    <Button ghost icon="menu" size="default" onClick={this.showDrawer} style={{margin: 16}}>App
                        Info</Button>
                </Col>
                <Col span={20} style={{textAlign: "center", margin: 16}}>
                    <span style={{color: 'white', fontSize: 36}}>
                        {this.state.appInfo ? this.state.appInfo.appName : "**"}
                    </span>
                </Col>
                <Drawer
                    title="App Info"
                    placement="left"
                    width="500"
                    onClose={this.onClose}
                    visible={this.state.visible}
                    closable={true}>
                    <div style={{
                        paddingTop: 8,
                        paddingBottom: 8,
                        textAlign: "center"
                    }}>{AppInfo.renderLabel(this.state.appInfo ? this.state.appInfo.labels : [])}</div>
                </Drawer>
            </Row>
        );
    }

}

export default AppInfo;
