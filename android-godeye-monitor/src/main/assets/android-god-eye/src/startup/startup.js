import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'


/**
 * 启动信息
 */
class Startup extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        let info = this.props.startupInfo;
        if (!info) {
            info = {};
        }
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Startup(启动信息)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <div className="span12">
                        <span>{'startup type:' + info.startupType + ", cost:" + info.startupTime + "ms"}</span>
                    </div>
                </Panel.Body>

            </Panel>);
    }
}

export default Startup;
