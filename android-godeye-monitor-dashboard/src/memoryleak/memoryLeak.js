import React, { Component } from 'react';
import '../App.css';

import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import JSONPretty from '../../node_modules/react-json-pretty';

import { Card, Button, Modal } from 'antd'

/**
 * MemoryLeak
 */
class MemoryLeak extends Component {

    constructor() {
        super();
        this.leakInfos = [];
        this.state = {
            dataList: [],
            isRefreshing: true,
            show: false,
            detail: null
        };
        this.setRefreshStatus = this.setRefreshStatus.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClear = this.handleClear.bind(this);
    }

    refresh(leakInfo) {
        this.leakInfos.push({
            "Id": leakInfo.leakSignature,
            "Time": new Date(leakInfo.createdTimeMillis).toLocaleTimeString(),
            "Class": leakInfo.leakObject,
            "Size": leakInfo.leakSizeByte,
            "LeakTrace": leakInfo.leakTrace,
            "Detail": leakInfo.detail
        });
        if (this.state.isRefreshing) {
            this.setState({ dataList: this.leakInfos });
        }
    }

    setRefreshStatus() {
        this.setState((prevState) => ({
            isRefreshing: !prevState.isRefreshing,
            dataList: this.leakInfos
        }));
    }

    handleClear() {
        this.leakInfos = []
        this.setState({
            dataList: [],
        });
    }

    handleShow(detail) {
        this.setState({ show: true, detail: detail });
    }

    handleClose() {
        this.setState({ show: false, detail: null });
    }

    render() {
        let dataList = this.state.dataList;
        return (
            <Card title="Memory Leak(内存泄漏)" extra={<div><Button
                onClick={this.setRefreshStatus}>{this.state.isRefreshing ? "Stop" : "Start"}</Button>
                &nbsp;&nbsp;
                <Button onClick={this.handleClear}>Clear</Button></div>}>
                <ReactTable
                    sortable={true}
                    resizable={true}
                    filterable={true}
                    data={dataList}
                    columns={[
                        {
                            Header: "Id",
                            accessor: "Id"
                        }, {
                            Header: "Time",
                            accessor: "Time"
                        }, {
                            Header: "Class",
                            accessor: "Class"
                        }, {
                            Header: "Size(Byte)",
                            accessor: "Size"
                        }
                    ]}
                    SubComponent={row => {
                        return (
                            <div style={{ padding: "20px" }}>
                                <Button type="primary" style={{ marginBottom: 12 }} onClick={() => { this.handleShow(row.original.Detail) }}>Detail</Button>
                                <JSONPretty id="json-pretty" json={row.original.LeakTrace} />
                            </div>
                        );
                    }}
                    defaultPageSize={8}
                    className="-striped -highlight" />
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Leak Detail" closable={true}
                    onOk={this.handleClose} width={1000} footer={null}>
                    <JSONPretty id="json-pretty" json={this.state.detail} />
                </Modal>
            </Card>
        );
    }
}

export default MemoryLeak;
