import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Row, Col, Clearfix, Grid, Panel, Button} from 'react-bootstrap'
import ReactTable from "../../node_modules/react-table";
import '../../node_modules/react-table/react-table.css'
import Util from '../libs/util'

/**
 * Pageload
 */
class Pageload extends Component {

    constructor() {
        super();


        this.pageloadInfos = [];

        this.state = {
            dataList: [],
            isRefreshing: true
        };

        this.setRefreshStatus = this.setRefreshStatus.bind(this);
    }

    refresh(pageloadInfo) {
        this.pageloadInfos.push(pageloadInfo);
        if (this.state.isRefreshing) {
            this.setState({dataList: this.pageloadInfos});
        }
    }

    setRefreshStatus() {
        this.setState((prevState, props) => ({
            isRefreshing: !prevState.isRefreshing,
            dataList: this.pageloadInfos
        }));
    }

    render() {
        let dataList = this.state.dataList;
        return (
            <Panel style={{textAlign: "left"}}>
                <Panel.Heading>
                    <Row>
                        <Col md={10}><h5>Pageload(页面加载)
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
