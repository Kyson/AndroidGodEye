import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import Util from '../libs/util'

/**
 * Thread
 */
class Thread extends Component {


    refresh(threadInfo) {
        this.setState({threadInfo});
    }

    constructor() {
        super();
        this.state = {
            threadInfo: []
        };
    }

    render() {
        let dataList = this.state.threadInfo;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Thread(线程)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactTable
                        data={dataList}
                        columns={[
                            {
                                Header: "Id",
                                accessor: "id"
                            }, {
                                Header: "Name",
                                accessor: "name"
                            }, {
                                Header: "State",
                                accessor: "state"
                            }, {
                                Header: "Deadlock",
                                accessor: "deadlock"
                            }, {
                                Header: "Priority",
                                accessor: "priority"
                            }, {
                                Header: "Deamon",
                                accessor: "deamon"
                            }, {
                                Header: "IsAlive",
                                accessor: "isAlive"
                            }, {
                                Header: "IsInterrupted",
                                accessor: "isInterrupted"
                            }
                        ]}
                        defaultPageSize={15}
                        className="-striped -highlight"/>
                </Panel.Body>
            </Panel>
        );
    }
}

export default Thread;
