import React, {Component} from 'react';
import '../App.css';

import JSONPretty from '../../node_modules/react-json-pretty';
import {toast} from 'react-toastify';

import {Card, Modal, Button} from 'antd'

/**
 * Crash
 */
class Crash extends Component {

    constructor(props) {
        super(props);
        this.handleClose = this.handleClose.bind(this);
        this.handleCrashDetailClick = this.handleCrashDetailClick.bind(this);
        this.handleCrashDetailClick = this.handleCrashDetailClick.bind(this);
        this.state = {
            crashInfo: {}
        };
    }

    refresh(crashInfo) {
        this.setState({crashInfo});
        toast.error("Crash!(发生崩溃)");
    }

    handleCrashDetailClick(e) {
        this.setState({show: true});
    }

    handleClose() {
        this.setState({show: false});
    }

    render() {
        let crashInfo = this.state.crashInfo;
        return (
            <Card title="Last Crash Info(最新一次崩溃)" extra={<Button onClick={this.handleCrashDetailClick}>Detail</Button>}>
                <div>
                    <p>
                        <strong>Time(崩溃时间):&nbsp;</strong>{crashInfo.timestampMillis ? new Date(crashInfo.timestampMillis).toISOString() : "**"}
                    </p>
                    <p style={{wordWrap: "break-word", wordBreak: "break-all"}}>
                        <strong>Message(异常信息):&nbsp;</strong>{crashInfo.throwableMessage ? crashInfo.throwableMessage : "**"}
                    </p>
                    <p><strong>Stacktrace(异常堆栈)</strong></p>
                    {Crash.renderStacktraceItem(crashInfo.throwableStacktrace)}
                </div>
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Crash detail" closable={true}
                       onOk={this.handleClose}>
                    <JSONPretty id="json-pretty" json={this.state.crashInfo}/>
                </Modal>
            </Card>);
    }

    static renderStacktraceItem(throwableStacktraces) {
        if (throwableStacktraces) {
            let items = [];
            for (let i = 0; i < throwableStacktraces.length; i++) {
                items.push(
                    <p style={{wordWrap: "break-word", wordBreak: "break-all", margin: 0}} key={"crash" + i}>
                        <small>{throwableStacktraces[i]}</small>
                    </p>
                );
            }
            return items;
        }
    }
}

export default Crash;
