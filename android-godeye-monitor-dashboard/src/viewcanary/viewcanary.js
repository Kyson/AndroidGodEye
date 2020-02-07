import React, {Component} from 'react';
import '../App.css';

import {Card, Badge, Button, Input, Popover} from 'antd'
import Util from "../libs/util";

/**
 * ViewCanary
 */
class ViewCanary extends Component {

    static isIssueInSearch(viewIssueInfo, searchText) {
        searchText = searchText.toLowerCase();
        if (viewIssueInfo) {
            if (JSON.stringify(viewIssueInfo).toString().toLowerCase().search(searchText) !== -1) {
                return true
            }
        }
        return false;
    }

    static findThisViewIssues(allViewIssueInfo, searchText) {
        if (searchText) {
            const thisViewIssues = [];
            allViewIssueInfo.forEach((item) => {
                if (ViewCanary.isIssueInSearch(item, searchText)) {
                    thisViewIssues.push(item)
                }
            });
            return thisViewIssues;
        }
        return allViewIssueInfo;
    }

    constructor(props) {
        super(props);
        this.renderExtra = this.renderExtra.bind(this);
        this.renderTimelines = this.renderTimelines.bind(this);
        this.handleClear = this.handleClear.bind(this);
        this.renderItem = this.renderItem.bind(this);
        this.refresh = this.refresh.bind(this);
        this.getBgColor = this.getBgColor.bind(this);
        this.state = {
            searchText: null,
            show: false,
            allViewIssueInfo: []
        };
    }

    refresh(viewIssueInfo) {
        this.setState(function (prevState, props) {
            const allViewIssueInfo = prevState.allViewIssueInfo;
            allViewIssueInfo.unshift(viewIssueInfo);
            return {allViewIssueInfo: allViewIssueInfo};
        });
    }

    handleClear() {
        this.setState({
            allViewIssueInfo: [],
        });
    }

    getBgColor(times, isText) {
        if (isText && times === 0) {
            return 'black'
        }
        if (times === 1) {
            return '#959BD3'
        } else if (times === 2) {
            return '#D0FFD0'
        } else if (times === 3) {
            return '#FFC0C0'
        } else if (times >= 4) {
            return '#FF8080'
        } else {
            return 'transparent'
        }
    }

    renderItem(issues, key) {
        let screenHeight = issues.screenHeight
        let screenWidth = issues.screenWidth
        let hw = screenHeight / screenWidth
        let popHeight = 300 * hw
        let popWidth = 300
        let ratio = screenWidth / popWidth

        let viewsOnPop = []
        issues.overDrawAreas.forEach(e => {
            if (e.rect.top > screenHeight) {
                return
            }
            viewsOnPop.push(
                <div style={{
                backgroundColor: this.getBgColor(e.overDrawTimes),
                position:'absolute',
                zIndex: e.overDrawTimes,
                left: e.rect.left / ratio,
                top: e.rect.top / ratio,
                width: (e.rect.right - e.rect.left) / ratio,
                height: (e.rect.bottom - e.rect.top) / ratio}}></div>
            )
        })
        issues.views.forEach(e => {
            if (e.rect.top > screenHeight) {
                return
            }
            viewsOnPop.push(
                <div style={{
                position:'absolute',
                zIndex: 50,
                left: e.rect.left / ratio,
                top: e.rect.top / ratio,
                borderStyle:'solid',
                borderWidth: e.hasBackground ? 1 : 0,
                width: (e.rect.right - e.rect.left) / ratio,
                height: (e.rect.bottom - e.rect.top) / ratio}}><div style= {{textAlignVertical: 'center',position: 'relative', overflow: 'hidden', color: this.getBgColor(e.textOverDrawTimes, true), fontSize: e.textSize? e.textSize / (ratio) : 10}}>{e.text ? e.text : ''}</div></div>
            )
        })
        let depthOnPop = []
        issues.views.forEach(e => {
            if (e.rect.top > screenHeight) {
                return
            }
            depthOnPop.push(
                <div style={{
                position:'absolute',
                zIndex: 50,
                backgroundColor: e.depth > 9 ? '#FF8080' : 'transparent',
                left: e.rect.left / ratio,
                top: e.rect.top / ratio,
                borderStyle:'solid',
                borderWidth: e.hasBackground ? 1 : 0,
                width: (e.rect.right - e.rect.left) / ratio,
                height: (e.rect.bottom - e.rect.top) / ratio}}><div style= {{textAlignVertical: 'center',position: 'relative', overflow: 'hidden', fontSize: e.textSize? e.textSize / (ratio) : 10}}>{e.text ? e.text : ''}</div></div>
            )
        })
        return (
            <Card style={{margin: 4}} size="small" key={key}>
                <Badge
                    color={Util.getGreen()}/><span>{`${new Date(issues.timestamp).toLocaleString()}.${issues.timestamp % 1000}`}</span>
                <br/>
                <span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <strong>{`${issues.activityName}`}</strong></span>
                    <div style= {{ marginLeft: 10, marginTop: 5, display:'flex'}}>
                    <Popover placement="rightTop" content={<div style={{height: popHeight, position: 'relative', overflow: 'hidden', width: popWidth, backgroundColor:'white'}}>{viewsOnPop}</div>} trigger="click">
                      <Button style={{fontSize: 10, height: 22}} >Check Overdraw</Button>
                    </Popover>
                    <Popover placement="rightTop" content={<div style={{height: popHeight, position: 'relative', overflow: 'hidden', width: popWidth, backgroundColor:'white'}}>{depthOnPop}</div>} trigger="click">
                      <Button style={{marginLeft: 10, fontSize: 10, height: 22}} >Check View Depth</Button>
                    </Popover>
                    </div>
            </Card>
        );
    }

    renderTimelines() {
        const processedInfo = ViewCanary.findThisViewIssues(this.state.allViewIssueInfo, this.state.searchText)
        if (processedInfo) {
            let items = [];
            for (let i = 0; i < processedInfo.length; i++) {
                const issues = processedInfo[i];
                items.push(this.renderItem(issues, i))
            }
            return items;
        }
    }

    renderExtra() {
        return (<span>
          <Input.Search
              style={{width: 200}}
              placeholder="Input search text"
              onSearch={value => this.setState({searchText: value})}
          />
            &nbsp;&nbsp;
            <Button onClick={this.handleClear}>Clear</Button>
        </span>)
    }

    render() {
        return (
            <Card title="View Canary(问题视图)" extra={this.renderExtra()}>
                <div style={{height: 600, overflow: 'auto'}}>
                    {this.renderTimelines()}
                </div>
            </Card>);
    }
}

export default ViewCanary;
