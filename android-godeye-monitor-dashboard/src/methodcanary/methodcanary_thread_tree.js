/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

import {Tree} from 'antd'

class MethodCanaryThreadTree extends Component {

    static getMethodValueWithRange(realValue, range) {
        return realValue === 0 ? range : realValue;
    }

    static buildTree(originStart, originEnd, start, end, methodInfos, parent, added, treeData) {
        for (let i = 0; i < methodInfos.length; i += 1) {
            const item = methodInfos[i];
            if (!added.has(item) && (!(item.end < start || item.start > end))) {
                if (parent) {
                    if (!parent.children) {
                        parent.children = [];
                    }
                    parent.children.push(item);
                } else {
                    treeData.push(item);
                }
                added.add(item);
                this.buildTree(originStart, originEnd,
                    item.start > originStart ? item.start : originStart, item.end < originEnd ? item.end : originEnd,
                    methodInfos, item, added, treeData);
            }
        }
    }

    static cloneMethodCanaryMethodInfo(methodInfo) {
        return {
            stack: methodInfo.stack,
            start: methodInfo.start,
            end: methodInfo.end,
            className: methodInfo.className,
            methodAccessFlag: methodInfo.methodAccessFlag,
            methodName: methodInfo.methodName,
            methodDesc: methodInfo.methodDesc,
            children: []
        }
    }

    static cloneMethodCanaryMethodInfos(methodInfos) {
        const cloned = [];
        for (let i = 0; i < methodInfos.length; i += 1) {
            cloned.push(MethodCanaryThreadTree.cloneMethodCanaryMethodInfo(methodInfos[i]));
        }
        return cloned;
    }

    constructor(props) {
        super(props);
        this.renderTreeNodes = this.renderTreeNodes.bind(this);
        this.getRenderNodeText = this.getRenderNodeText.bind(this);
        this.clear = this.clear.bind(this);
        this.refresh = this.refresh.bind(this);
        this.state = {
            treeData: [],
            start: 0,
            end: 0
        }
    }

    getMethodStartInRange(realStart) {
        return realStart > this.state.start ? realStart : this.state.start;
    }

    getMethodEndInRange(realEnd) {
        return realEnd < this.state.end ? realEnd : this.state.end;
    }

    clear() {
        this.setState({
            treeData: [],
            start: 0,
            end: 0
        });
    }

    refresh(start, end, methodInfos) {
        const treeData = [];
        const cloned = MethodCanaryThreadTree.cloneMethodCanaryMethodInfos(methodInfos);
        MethodCanaryThreadTree.buildTree(start, end, start, end, cloned, null, new Set(), treeData);
        this.setState({treeData, start, end})
    }

    getRenderNodeText(item) {
        return <span>
           [COST]&nbsp;
            <strong>{Util.getFormatDuration((this.getMethodEndInRange(item.end) - this.getMethodStartInRange(item.start)))}</strong>
            &nbsp;&nbsp;&nbsp;&nbsp;[WEIGHT]&nbsp;
            <strong>{((this.getMethodEndInRange(item.end) - this.getMethodStartInRange(item.start)) * 100 / (this.state.end - this.state.start)).toFixed(1) + "%"}</strong>
            &nbsp;&nbsp;&nbsp;&nbsp;[METHOD]&nbsp;
            <strong>{item.className + "#" + item.methodName}</strong>
            &nbsp;&nbsp;&nbsp;&nbsp;[DURATION]&nbsp;
            From&nbsp;<strong>{Util.getFormatMAndSAndMS(item.start)}</strong>&nbsp;to&nbsp;
            <strong>{Util.getFormatMAndSAndMS(item.end)}</strong>
           </span>
    }

    renderTreeNodes = data => data.map((item) => {
        if (item.children) {
            return (
                <Tree.TreeNode title={
                    this.getRenderNodeText(item)
                }
                               selectable={false}
                               key={`${item.stack}#${item.start}#${item.end}`}
                               dataRef={item}>
                    {this.renderTreeNodes(item.children)}
                </Tree.TreeNode>
            );
        }
        return <Tree.TreeNode {...item} title={
            this.getRenderNodeText(item)
        } selectable={false}
                              key={`${item.stack}#${item.start}#${item.end}`} dataRef={item} isLeaf/>;
    });

    render() {
        if (this.state.treeData && this.state.treeData.length > 0) {
            return (<Tree>
                {this.renderTreeNodes(this.state.treeData)}
            </Tree>);
        } else {
            return <span>No data.</span>
        }
    }
}

export default MethodCanaryThreadTree;