import React, {Component} from 'react';
import '../App.css';

import JSONPretty from '../../node_modules/react-json-pretty';

import {Card, Modal, Button, List} from 'antd'

/**
 * Crash
 */
class Crash extends Component {

    constructor(props) {
        super(props);
        this.handleShowLastCrashInfoDetail = this.handleShowLastCrashInfoDetail.bind(this);
        this.handleCloseLastCrashInfo = this.handleCloseLastCrashInfo.bind(this);
        this.handleShowCrashList = this.handleShowCrashList.bind(this);
        this.handleCloseCrashList = this.handleCloseCrashList.bind(this);
        Crash.renderCrashList = Crash.renderCrashList.bind(this);
        this.state = {
            moreCrashInfos: [],
            lastCrashInfo: {},
            showLastCrashInfo: false,
            showCrashList: false
        };
    }

    refresh(crashInfos) {
        const lastCrashInfo = crashInfos.shift();
        this.setState({moreCrashInfos: crashInfos, lastCrashInfo: lastCrashInfo});
    }

    handleShowLastCrashInfoDetail() {
        this.setState({showLastCrashInfo: true});
    }

    handleCloseLastCrashInfo() {
        this.setState({showLastCrashInfo: false});
    }

    handleShowCrashList() {
        this.setState({showCrashList: true});
    }

    handleCloseCrashList() {
        this.setState({showCrashList: false});
    }

    static renderCrashList(moreCrashInfos) {
        return (
            <List
                size="large"
                bordered
                dataSource={moreCrashInfos}
                renderItem={item => <List.Item>
                    Time(崩溃时间):&nbsp;
                    <strong>{item.timestampMillis ? new Date(item.timestampMillis).toISOString() : "**"}</strong><br/>
                    Message(异常信息):&nbsp;
                    <strong>{item.throwableMessage ? item.throwableMessage : "**"}</strong><br/><br/>
                    <JSONPretty id="json-pretty" json={item}/>
                </List.Item>}
            />
        );
    }

    render() {
        let lastCrashInfo = this.state.lastCrashInfo;
        let moreCrashInfos = this.state.moreCrashInfos;
        return (
            <Card title="Crash Info(崩溃记录)"
                  extra={<div><Button onClick={this.handleShowLastCrashInfoDetail}>Show Detail</Button>
                      &nbsp;&nbsp;
                      <Button onClick={this.handleShowCrashList}>More Crashes</Button></div>}>
                <div>
                    <p>
                        Time(崩溃时间):&nbsp;
                        <strong>{lastCrashInfo.timestampMillis ? new Date(lastCrashInfo.timestampMillis).toISOString() : "**"}</strong>
                    </p>
                    <p style={{wordWrap: "break-word", wordBreak: "break-all"}}>
                        Message(异常信息):&nbsp;
                        <strong>{lastCrashInfo.throwableMessage ? lastCrashInfo.throwableMessage : "**"}</strong>
                    </p>
                </div>
                <Modal visible={this.state.showLastCrashInfo} onCancel={this.handleCloseLastCrashInfo}
                       title="Crash Detail"
                       closable={true}
                       onOk={this.handleCloseLastCrashInfo} width={1000} footer={null}>
                    <JSONPretty id="json-pretty" json={this.state.lastCrashInfo}/>
                </Modal>
                <Modal visible={this.state.showCrashList} onCancel={this.handleCloseCrashList}
                       title="Crash List"
                       closable={true}
                       onOk={this.handleCloseCrashList} width={1000} footer={null}>
                    {Crash.renderCrashList(moreCrashInfos)}
                </Modal>
            </Card>);
    }
}

export default Crash;
