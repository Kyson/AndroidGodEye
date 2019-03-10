import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel, Button} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import Util from '../libs/util'
import JSONPretty from '../../node_modules/react-json-pretty';
import {toast} from 'react-toastify';

/**
 * MemoryLeak
 */
class MemoryLeak extends Component {

    constructor() {
        super();
        this.leakInfos = [];
        this.state = {
            dataList: [],
            isRefreshing: true
        };
        this.setRefreshStatus = this.setRefreshStatus.bind(this);
    }

    refresh(leakInfo) {
        let hasLeakBefore = false;
        for (let i = 0; i < this.leakInfos.length; i++) {
            if (leakInfo && leakInfo.referenceKey && leakInfo.referenceKey === this.leakInfos[i].referenceKey) {
                this.leakInfos[i] = leakInfo;
                hasLeakBefore = true;
            }
        }
        if (!hasLeakBefore) {
            this.leakInfos.push(leakInfo);
            toast.error("Memory leak.(发生内存泄漏)")
        }
        if (this.state.isRefreshing) {
            this.setState({dataList: this.leakInfos});
        }
    }

    setRefreshStatus() {
        this.setState((prevState, props) => ({
            isRefreshing: !prevState.isRefreshing,
            dataList: this.leakInfos
        }));
    }

    render() {
        let dataList = this.state.dataList;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <Row>
                        <Col md={10}><h5>Memory Leak(内存泄漏)
                        </h5></Col>
                        <Col md={2}
                             style={{textAlign: 'right'}}><Button
                            onClick={this.setRefreshStatus}>{this.state.isRefreshing ? "Stop" : "Start"}</Button></Col>
                    </Row>
                </Panel.Heading>
                <Panel.Body>
                    <ReactTable
                        sortable={true}
                        resizable={true}
                        filterable={true}
                        data={dataList}
                        columns={[
                            {
                                Header: "referenceKey",
                                accessor: "referenceKey"
                            }, {
                                Header: "leakTime",
                                accessor: "leakTime"
                            }, {
                                Header: "leakObjectName",
                                accessor: "leakObjectName"
                            }, {
                                Header: "statusSummary",
                                accessor: "statusSummary"
                            }
                        ]}
                        SubComponent={row => {
                            return (
                                <div style={{padding: "20px"}}>
                                    <JSONPretty id="json-pretty" json={row.original.pathToGcRoot}/>
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

export default MemoryLeak;
