import React, {Component} from 'react';
import '../App.css';

import JSONPretty from '../../node_modules/react-json-pretty';

import {Card, Modal, Button, List, Collapse} from 'antd'

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
        this.renderLastCrashInfoModal = this.renderLastCrashInfoModal.bind(this);
        this.renderCrashInfoListModal = this.renderCrashInfoListModal.bind(this);
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
                    {Crash.renderCrashDetailItem(item, false)}
                </List.Item>}
            />
        );
    }

    static renderCrashDetailItem(crashInfo, expand) {
        let renderItems = [];
        let allKeys = [];
        for (let crashInfoKey in crashInfo) {
            if (crashInfoKey !== "Crash time" && crashInfoKey !== "crash_message") {
                renderItems.push(
                    <Collapse.Panel header={crashInfoKey} key={crashInfoKey}>
                        <JSONPretty id="json-pretty" json={crashInfo[crashInfoKey]}/>
                    </Collapse.Panel>
                );
                allKeys.push(crashInfoKey);
            }
        }
        return (
            <div style={{width: "100%"}}>
                <Collapse defaultActiveKey={expand ? ["1"] : []}>
                    <Collapse.Panel
                        header={"Time(崩溃时间):" + crashInfo["Crash time"] + "    ||    Message(异常信息):" + crashInfo["crash_message"]}
                        key={"1"}>
                        <Collapse defaultActiveKey={"java stacktrace"}>
                            {renderItems}
                        </Collapse>
                    </Collapse.Panel>
                </Collapse>
            </div>
        );
    }

    renderLastCrashInfoModal() {
        if (this.state.showLastCrashInfo) {
            return (<Modal visible={this.state.showLastCrashInfo} onCancel={this.handleCloseLastCrashInfo}
                           title="Crash Detail"
                           closable={true}
                           onOk={this.handleCloseLastCrashInfo} width={1000} footer={null}>
                {Crash.renderCrashDetailItem(this.state.lastCrashInfo, true)}
            </Modal>)
        } else {
            return <div/>
        }
    }

    renderCrashInfoListModal(moreCrashInfos) {
        if (this.state.showCrashList) {
            return (<Modal visible={this.state.showCrashList} onCancel={this.handleCloseCrashList}
                           title="Crash List"
                           closable={true}
                           onOk={this.handleCloseCrashList} width={1000} footer={null}>
                {Crash.renderCrashList(moreCrashInfos)}
            </Modal>)
        } else {
            return <div/>
        }
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
                        <strong>{lastCrashInfo["Crash time"]}</strong>
                    </p>
                    <p style={{wordWrap: "break-word", wordBreak: "break-all"}}>
                        Message(异常信息):&nbsp;
                        <strong>{lastCrashInfo["crash_message"]}</strong>
                    </p>
                </div>
                {this.renderLastCrashInfoModal()}
                {this.renderCrashInfoListModal(moreCrashInfos)}
            </Card>);
    }
}

export default Crash;
