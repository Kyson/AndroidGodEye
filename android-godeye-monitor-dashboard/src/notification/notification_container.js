/* eslint-disable react/prop-types */
/* eslint-disable react/jsx-no-target-blank */
import React, { Component } from 'react';
import '../App.css';

import { Drawer, Row, Col, Typography, Affix, Badge, Button, Icon } from 'antd'
import { DownloadOutlined } from '@ant-design/icons';

import Notification from "./notification";


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
                        <Col span={24} style={{ textAlign: "right" }}>
                            <Typography.Text>
                                {labels[i]}
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
            messages: [],
            visible: false
        }
    }

    refresh(moduleName, payload) {
        Notification.handleMessage(moduleName, payload)
        this.setState({ messages: [...this.state.messages, payload.message] })
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
            <div style={{ textAlign: "right", margin: 32 }}>
                <Affix offsetBottom={32}>
                    <Badge count={1} onClick={this.showDrawer}>
                        <Button shape="circle" icon={<DownloadOutlined />} />
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
            </div >
        );
    }

}

export default NotificationContainer;

