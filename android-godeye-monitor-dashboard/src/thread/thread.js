import React, {Component} from 'react';
import '../App.css';
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import JSONPretty from '../../node_modules/react-json-pretty';

import {Card, Button} from 'antd'

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
        this.setState((prevState) => ({
            isRefreshing: !prevState.isRefreshing,
            threadInfo: this.threadInfoForCache
        }));
    }

    render() {
        let dataList = this.state.threadInfo;
        return (
            <Card title="Thread(线程)"
                  extra={<Button onClick={this.setRefreshStatus}>{this.state.isRefreshing ? "Stop" : "Start"}</Button>}>
                <ReactTable
                    sortable={true}
                    resizable={true}
                    filterable={true}
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
                            Header: "Priority",
                            accessor: "priority"
                        }, {
                            Header: "Deamon",
                            id: "deamon",
                            accessor: d => d.deamon + ""
                        }, {
                            Header: "IsAlive",
                            id: "isAlive",
                            accessor: d => d.isAlive + ""
                        }, {
                            Header: "IsInterrupted",
                            id: "isInterrupted",
                            accessor: d => d.isInterrupted + ""
                        }, {
                            Header: "RunningProcess",
                            id: "threadRunningProcess",
                            accessor: d => d.threadRunningProcess + ""
                        }
                    ]}
                    SubComponent={row => {
                        return (
                            <div style={{padding: "20px"}}>
                                <JSONPretty id="json-pretty" json={row.original.stackTraceElements}/>
                            </div>
                        );
                    }}
                    defaultPageSize={16}
                    className="-striped -highlight"/>
            </Card>
        );
    }
}

export default Thread;
