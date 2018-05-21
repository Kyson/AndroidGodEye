import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'

/**
 * Crash
 */
class Crash extends Component {

    constructor(props) {
        super(props);
        this.state = {
            crashInfo: {}
        }
    }

    refresh(crashInfo) {
        this.setState({crashInfo});
    }

    render() {
        let crashInfo = this.state.crashInfo;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Last Crash Info
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <p>
                        <strong>Time:&nbsp;</strong>{crashInfo.timestampMillis ? new Date(crashInfo.timestampMillis).toLocaleTimeString() : "**"}
                    </p>
                    <p style={{wordWrap: "break-word", wordBreak: "break-all"}}>
                        <strong>Message:&nbsp;</strong>{crashInfo.throwableMessage ? crashInfo.throwableMessage : "**"}
                    </p>
                    <p><strong>Stacktrace</strong></p>
                    {this.renderStacktraceItem(crashInfo.throwableStacktrace)}
                </Panel.Body>
            </Panel>);
    }

    renderStacktraceItem(throwableStacktraces) {
        if (throwableStacktraces) {
            let items = [];
            for (let i = 0; i < throwableStacktraces.length; i++) {
                items.push(<p style={{wordWrap: "break-word", wordBreak: "break-all", margin: 0}}>
                    <small key={"crash" + i}>{throwableStacktraces[i]}</small>
                </p>);
            }
            return items;
        }
    }
}

export default Crash;
