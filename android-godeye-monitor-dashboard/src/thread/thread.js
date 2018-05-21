import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel, Button} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import JSONPretty from '../../node_modules/react-json-pretty';

/**
 * Thread
 */
class Thread extends Component {

    constructor() {
        super();
        this.threadInfoForCache = [];
        this.state = {
            threadInfo: [],
            isRefreshing: true
        };
        this.setRefreshStatus = this.setRefreshStatus.bind(this);
    }

    refresh(threadInfo) {
        this.threadInfoForCache = threadInfo;
        if (this.state.isRefreshing) {
            this.setState({threadInfo});
        }
    }

    setRefreshStatus() {
        this.setState((prevState, props) => ({
            isRefreshing: !prevState.isRefreshing,
            threadInfo: this.threadInfoForCache
        }));
    }

    render() {
        let dataList = this.state.threadInfo;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <Row>
                        <Col md={10}><h5>Thread(线程)
                        </h5></Col>
                        <Col md={2}
                             style={{textAlign: 'right'}}><Button
                            onClick={this.setRefreshStatus}>{this.state.isRefreshing ? "Refreshing...|Stop" : "Stopped...|Start"}</Button></Col>
                    </Row>
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
                        SubComponent={row => {
                            return (
                                <div style={{padding: "20px"}}>
                                    <JSONPretty id="json-pretty" json={row.original.stackTraceElements}/>
                                </div>
                            );
                        }}
                        defaultPageSize={15}
                        className="-striped -highlight"/>
                </Panel.Body>
            </Panel>
        );
    }
}

export default Thread;
