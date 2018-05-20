import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import Util from '../libs/util'
import JSONPretty from '../../node_modules/react-json-pretty';

/**
 * MemoryLeak
 */
class MemoryLeak extends Component {

    refresh(leakInfo) {
        function getNewDataList(prevState, dataList) {
            prevState.dataList.push(dataList);
            return prevState.dataList;
        }

        this.setState((prevState, props) => ({
            dataList: getNewDataList(prevState, leakInfo)
        }));
    }

    constructor() {
        super();
        this.state = {
            dataList: []
        };
    }

    render() {
        let dataList = this.state.dataList;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <h5>Memory Leak(内存泄漏)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactTable
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
                                    <JSONPretty id="json-pretty" json={row.original.leakStack}/>
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
