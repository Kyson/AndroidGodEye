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
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Startup(启动信息)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <div className="span12">
                        <p>
                            <strong>{'Startup Type:'}&nbsp;&nbsp;&nbsp;</strong><span style={{fontSize:30}}>{info.startupType}</span>
                        </p>
                        <p>
                            <strong>{'Cost time:'}&nbsp;&nbsp;&nbsp;</strong><span style={{fontSize:30}}>{info.startupTime}</span>&nbsp;&nbsp;{"ms"}
                        </p>
                    </div>
                </Panel.Body>

            </Panel>);
    }
}

export default Startup;
