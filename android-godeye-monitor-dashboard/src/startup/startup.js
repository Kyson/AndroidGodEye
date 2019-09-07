import React, {Component} from 'react';
import '../App.css';
// import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
// import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import {Card} from 'antd'

/**
 * 启动信息
 */
class Startup extends Component {

    constructor(props) {
        super(props);
        this.state = {
            startupInfo: {}
        }
    }

    refresh(startupInfo) {
        this.setState({startupInfo});
    }

    render() {
        let info = this.state.startupInfo;
        return (
            <Card title="Startup(启动信息)">
                <div style={{paddingTop: 4, paddingBottom: 4}}>
                    <span >
                        Startup Type:&nbsp;&nbsp;
                        <span style={{fontSize: 48}}>{info.startupType}</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost time:&nbsp;&nbsp;
                        <span style={{fontSize: 48}}>{info.startupTime}</span>&nbsp;&nbsp;ms
                    </span>
                </div>
            </Card>);
    }
}

export default Startup;
