/* eslint-disable react/prop-types */
/* eslint-disable react/jsx-no-target-blank */
import React, { Component } from 'react';
import '../App.css';

import { Drawer, Row, Col, Typography, Affix, Badge, Button, notification, Divider } from 'antd'


/**
 * 应用基本信息
 */
class NotificationContainer extends Component {


    static renderLabel(labels) {
        if (labels) {
            let items = [];
            for (let i = 0; i < labels.length; i++) {
                items.push(
                    <Row key={"notification" + i} type="flex" justify="center" align="middle" style={{ marginTop: 8 }}>
                        <Col span={24} style={{ textAlign: "left" }}>
                            <Typography.Text>
                                {labels[i]}
                            </Typography.Text>
                        </Col>
                        <Divider />
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
            messages: [],
            visible: false
        }
    }

    refresh(moduleName, payload) {
        if ("AndroidGodEyeNotification" === moduleName) {
            notification.info({
                message: "Warning",
                description: payload.message,
                placement: 'bottomRight',
                duration: 3,
                bottom: 96
            });
        }
        this.setState({ messages: [payload.message, ...this.state.messages] })
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
            <div style={{ textAlign: "right" }}>
                <Affix offsetBottom={32}>
                    <Badge count={this.state.messages.length} onClick={this.showDrawer}>
                        <Button shape="circle" icon="info" size="large" type="primary" onClick={this.showDrawer} ></Button>
                    </Badge>
                </Affix>
                <Drawer
                    title="Notification"
                    placement="right"
                    width="500"
                    onClose={this.onClose}
                    visible={this.state.visible}
                    closable={true}>
                    <div style={{
                        paddingTop: 8,
                        paddingBottom: 8,
                        textAlign: "center"
                    }}>{NotificationContainer.renderLabel(this.state.messages ? this.state.messages : [])}</div>
                </Drawer>
            </div>
        );
    }

}

export default NotificationContainer;