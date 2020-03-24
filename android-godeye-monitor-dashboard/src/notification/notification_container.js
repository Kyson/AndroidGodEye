/* eslint-disable react/prop-types */
/* eslint-disable react/jsx-no-target-blank */
import React, { Component } from 'react';
import '../App.css';

import { Drawer, Affix, Badge, Button, notification, Divider, Typography } from 'antd'


/**
 * 应用基本信息
 */
class NotificationContainer extends Component {


    static renderLabel(messages) {
        if (messages) {
            let items = [];
            for (let i = 0; i < messages.length; i++) {
                items.push(
                    <div key={"notification" + i} type="flex" justify="left" style={{ textAlign: "left" }}>
                        <Typography.Text type="secondary">{new Date(messages[i].timeMillis).toLocaleTimeString()}</Typography.Text>
                        <br />
                        <Typography.Text>{messages[i].message}</Typography.Text>
                        <Divider />
                    </div>);
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
            visible: false,
            enable: false
        }
    }

    refresh(moduleName, payload) {
        if ("AndroidGodEyeNotification" === moduleName) {
            notification.info({
                message: new Date(payload.timeMillis).toLocaleTimeString(),
                description: payload.message,
                placement: 'bottomRight',
                duration: 3,
                bottom: 96
            });
            this.setState({ messages: [payload, ...this.state.messages] })
        } else if ("AndroidGodEyeNotificationAction" === moduleName) {
            this.setState({ enable: payload.isInstall })
        }
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
        if (this.state.enable) {
            return (
                <div style={{ textAlign: "right" }}>
                    <Affix offsetBottom={32}>
                        <Badge count={this.state.messages.length} onClick={this.showDrawer}>
                            <Button shape="circle" icon="info" size="large" type="primary" onClick={this.showDrawer} ></Button>
                        </Badge>
                    </Affix>
                    <Drawer
                        title="Notifications"
                        placement="right"
                        width="500"
                        onClose={this.onClose}
                        visible={this.state.visible}
                        closable={true}>
                        <div>{NotificationContainer.renderLabel(this.state.messages ? this.state.messages : [])}</div>
                    </Drawer>
                </div>
            );
        } else {
            return <div></div>
        }
    }
}

export default NotificationContainer;