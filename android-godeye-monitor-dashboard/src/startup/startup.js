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
                <div className="span12">
                    <p>
                        <strong>{'Startup Type:'}&nbsp;&nbsp;&nbsp;</strong><span
                        style={{fontSize: 30}}>{info.startupType}</span>
                        <strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{'Cost time:'}&nbsp;&nbsp;&nbsp;</strong><span
                        style={{fontSize: 30}}>{info.startupTime}</span>&nbsp;&nbsp;{"ms"}
                    </p>
                </div>
            </Card>);
    }
}

export default Startup;
