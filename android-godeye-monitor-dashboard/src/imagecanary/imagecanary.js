import React, {Component} from 'react';
import '../App.css';

import {Card, Badge, Button, Input} from 'antd'
import Util from "../libs/util";

/**
 * ImageCanary
 */
class ImageCanary extends Component {
    static BAD_DRAW_TIME = 800;
    static BAD_LOAD_TIME = 2000;

    static isIssueInSearch(imageIssueInfo, searchText) {
        searchText = searchText.toLowerCase();
        if (imageIssueInfo) {
            if (JSON.stringify(imageIssueInfo).toString().toLowerCase().search(searchText) !== -1) {
                return true
            }
        }
        return false;
    }

    static findThisImageIssues(allImageIssueInfo, searchText) {
        if (searchText) {
            const thisImageIssues = [];
            allImageIssueInfo.forEach((item) => {
                if (ImageCanary.isIssueInSearch(item, searchText)) {
                    thisImageIssues.push(item)
                }
            });
            return thisImageIssues;
        }
        return allImageIssueInfo;
    }

    constructor(props) {
        super(props);
        this.renderExtra = this.renderExtra.bind(this);
        this.renderTimelines = this.renderTimelines.bind(this);
        this.handleClear = this.handleClear.bind(this);
        this.refresh = this.refresh.bind(this);
        this.state = {
            searchText: null,
            show: false,
            allImageIssueInfo: []
        };
    }

    refresh(imageIssueInfo) {
        this.setState(function (prevState, props) {
            const allImageIssueInfo = prevState.allImageIssueInfo;
            allImageIssueInfo.unshift(imageIssueInfo);
            return {allImageIssueInfo: allImageIssueInfo};
        });
    }

    handleClear() {
        this.setState({
            allImageIssueInfo: [],
        });
    }

    static renderItem(issues, key) {
        return (
            <Card style={{margin: 4}} size="small" key={key}>
                <Badge
                    color={Util.getGreen()}/><span>{`${new Date(issues.timestamp).toLocaleString()}.${issues.timestamp % 1000}`}</span>
                <br/>
                <span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <strong>{`${issues.activityClassName}`}</strong>{`@${issues.activityHashCode}`}
                    </span>
                <div>
                    <div>&nbsp;&nbsp;&nbsp;&nbsp;{`Image Issue Type: ${issues.issueType}`}</div>
                    <div>&nbsp;&nbsp;&nbsp;&nbsp;{`Image Id: ${issues.imageViewHashCode}`}</div>
                    <div>&nbsp;&nbsp;&nbsp;&nbsp;{`Bitmap size: ${issues.bitmapWidth} * ${issues.bitmapHeight}`}</div>
                    <div>&nbsp;&nbsp;&nbsp;&nbsp;{`Image size: ${issues.imageViewWidth} * ${issues.imageViewHeight}`}</div>
                </div>
                <br/>
            </Card>
        );
    }

    renderTimelines() {
        const processedInfo = ImageCanary.findThisImageIssues(this.state.allImageIssueInfo, this.state.searchText)
        if (processedInfo) {
            let items = [];
            for (let i = 0; i < processedInfo.length; i++) {
                const issues = processedInfo[i];
                items.push(ImageCanary.renderItem(issues, i))
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
            <Card title="Image Canary(问题图片)" extra={this.renderExtra()}>
                <div style={{height: 690, overflow: 'auto'}}>
                    {this.renderTimelines()}
                </div>
            </Card>);
    }
}

export default ImageCanary;
