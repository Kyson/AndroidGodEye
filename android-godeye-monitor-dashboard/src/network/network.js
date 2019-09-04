/* eslint-disable react/no-string-refs */
import React, {Component} from 'react';
import '../App.css';
import JSONPretty from '../../node_modules/react-json-pretty';

import ReactHighcharts from '../../node_modules/react-highcharts'
import Util from "../libs/util";
import {Card, Modal, Table, Tabs, Badge, Button, Input, message} from 'antd'

/**
 * Network
 */
class Network extends Component {

    static isNetworkInSearch(networkInfo, searchText) {
        searchText = searchText.toLowerCase();
        if (networkInfo) {
            if (JSON.stringify(networkInfo).toString().toLowerCase().search(searchText) !== -1) {
                return true
            }
        }
        return false;
    }

    static findNetworksInSearch(networkInfos, searchText) {
        if (searchText) {
            const findNetworkInfos = [];
            networkInfos.forEach((item) => {
                if (Network.isNetworkInSearch(item, searchText)) {
                    findNetworkInfos.push(item)
                }
            });
            return findNetworkInfos;
        }
        return networkInfos;
    }

    constructor(props) {
        super(props);
        this.handleClose = this.handleClose.bind(this);
        this.handleShowDetail = this.handleShowDetail.bind(this);
        this.handleClear = this.handleClear.bind(this);
        this.renderExtra = this.renderExtra.bind(this);
        this.state = {
            searchText: null,
            show: false,
            networkInfo: null,
            networkInfos: [],
        };
    }

    handleShowDetail(networkInfo) {
        if (networkInfo) {
            this.setState({networkInfo: networkInfo, show: true});
        }
    }

    handleClose() {
        this.setState({networkInfo: null, show: false});
    }

    handleClear() {
        this.setState({
            networkInfos: []
        });
    }

    refresh(networkInfo) {
        if (networkInfo) {
            const networkInfos = this.state.networkInfos;
            const currentTime = new Date();
            networkInfo.localTime = `${currentTime.toLocaleTimeString()}.${currentTime.getMilliseconds() % 1000}`;
            networkInfos.unshift(networkInfo);
            this.setState({
                networkInfos: networkInfos,
            });
            if (!networkInfo.isSuccessful) {
                message.error("Network error.(网络请求失败)")
            }
        }
    }

    renderModelContent() {
        const networkInfo = this.state.networkInfo;
        if (networkInfo) {
            const series = [];
            if (networkInfo.networkTime) {
                let otherTime = networkInfo.totalTime;
                for (let i = 0; i < networkInfo.networkTime.length; i++) {
                    series.push({
                        name: networkInfo.networkTime[i].name,
                        data: [networkInfo.networkTime[i].time]
                    });
                    otherTime = otherTime - networkInfo.networkTime[i].time;
                }
                if (otherTime < 0) {
                    otherTime = 0;
                }
                series.push({
                    name: "otherTime",
                    data: [otherTime]
                });
            }
            const optionsForTime = {
                chart: {
                    type: 'bar',
                    height: 80,
                    margin: 0,
                    spacing: 0
                },
                title: {
                    text: null
                },
                colors: Util.getCommonColors(),
                legend: {
                    enabled: true
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                yAxis: {
                    visible: false,
                    title: {
                        text: null
                    },
                    min: 0,
                    gridLineWidth: 0
                },
                xAxis: {
                    visible: false,
                    categories: ['阶段耗时']
                },
                tooltip: {
                    formatter: function () {
                        return '<div style="text-align:center"><span style="font-size:13px;color:' +
                            'black">' +
                            this.series.name + " " + this.point.y + "ms"
                            + '</span></div>'
                    }
                },
                plotOptions: {
                    series: {
                        stacking: 'percent',
                        dataLabels: {
                            enabled: true,
                            formatter: function () {
                                return this.series.name + " " + this.series.data[0].y + "ms"
                            }
                        },
                        borderRadius: 0,
                        pointPadding: 0,
                        groupPadding: 0,
                        pointWidth: 25,
                    }
                },
                series: series
            };
            return (
                <div>
                    <strong>
                        Result:&nbsp;&nbsp;
                        <Badge
                            color={networkInfo.isSuccessful ? Util.getGreen() : Util.getRed()}
                            text={networkInfo.message}>
                        </Badge>
                        <br/>
                        TotalTime:&nbsp;&nbsp;{networkInfo.totalTime}ms
                    </strong>
                    <ReactHighcharts
                        ref="chartForTime"
                        config={optionsForTime}/>
                    <Tabs defaultActiveKey="1">
                        <Tabs.TabPane tab={`Request(${networkInfo.networkContent.networkType})`} key="1">
                            <pre>{networkInfo.networkContent.requestContent}</pre>
                        </Tabs.TabPane>
                        <Tabs.TabPane tab={`Response(${networkInfo.networkContent.networkType})`} key="2">
                            <pre>{networkInfo.networkContent.responseContent}</pre>
                        </Tabs.TabPane>
                        <Tabs.TabPane tab="ExtraInfo" key="3">
                            <JSONPretty id="json-pretty" json={networkInfo.extraInfo}/>
                        </Tabs.TabPane>
                    </Tabs>
                </div>
            )
        }
        return <div/>
    }

    renderTable() {
        const networkInfos = Network.findNetworksInSearch(this.state.networkInfos, this.state.searchText);
        const showDetail = this.handleShowDetail;
        const columns = [
            {
                title: 'LocalTime',
                dataIndex: 'localtime',
                key: 'localtime',
            },
            {
                title: 'Summary',
                dataIndex: 'summary',
                key: 'summary',
            },
            {
                title: 'Message',
                dataIndex: 'message',
                key: 'message',
                render: (text, record) => {
                    return (<div>
                        <Badge color={record.networkInfo.isSuccessful ? Util.getGreen() : Util.getRed()}/>
                        <span>{record.networkInfo.message}</span>
                    </div>);
                }
            },
            {
                title: 'TotalTime(ms)',
                dataIndex: 'totalTime',
                key: 'totalTime',
            },
            {
                title: 'Detail',
                key: 'action',
                render: (text, record) => {
                    return (<Button onClick={() => {
                        showDetail(record.networkInfo)
                    }}>Detail</Button>);
                }
            },
        ];
        const datas = [];
        for (let i = 0; i < networkInfos.length; i++) {
            const networkInfo = networkInfos[i];
            datas.push({
                key: i,
                localtime: networkInfo.localTime,
                summary: networkInfo.summary,
                message: networkInfo.message,
                totalTime: networkInfo.totalTime,
                networkInfo: networkInfo
            })
        }
        return (<Table dataSource={datas} columns={columns} size="middle"
                       pagination={{pageSize: 10}}/>);
    }

    renderExtra() {
        return (<span>
          <Input.Search
              style={{width: 200}}
              placeholder="Input search text"
              onSearch={value => this.setState({searchText: value})}
          />
            &nbsp;&nbsp;
            <Button
                onClick={this.handleClear}>Clear</Button>
        </span>)
    }

    render() {
        return (
            <Card title="Network(网络)" extra={this.renderExtra()}>
                {this.renderTable()}
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Detail" closable={true}
                       onOk={this.handleClose} width={800}>
                    {this.renderModelContent()}
                </Modal>
            </Card>);
    }
}

export default Network;
