import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import Util from '../libs/util'

/**
 * Pageload
 */
class Pageload extends Component {


    refresh(pageloadInfo) {
        function getNewDataList(prevState, pageloadInfo) {
            prevState.dataList.push(pageloadInfo);
            return prevState.dataList;
        }

        this.setState((prevState, props) => ({
            dataList: getNewDataList(prevState, pageloadInfo)
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
                    <h5>Pageload(页面加载)
                    </h5>
                </Panel.Heading>
                <Panel.Body>
                    <ReactTable
                        data={dataList}
                        columns={[
                            {
                                Header: "PageId",
                                accessor: "pageId"
                            }, {
                                Header: "PageName",
                                accessor: "pageName"
                            }, {
                                Header: "Status",
                                accessor: "pageStatus",
                                Cell: row => (
                                    <span>
                                        <span style={{
                                            color: row.value === 'created' ? Util.getGreen()
                                                : row.value === 'destroyed' ? Util.getRed()
                                                    : Util.getGrey(),
                                            transition: 'all .3s ease'
                                        }}>
                                          &#x25cf;
                                        </span> {row.value}</span>
                                )
                            }, {
                                Header: "Time",
                                accessor: "pageStatusTime"
                            }
                        ]}

                        defaultPageSize={10}
                        className="-striped -highlight"/>
                </Panel.Body>
            </Panel>
        );
    }
}

export default Pageload;
