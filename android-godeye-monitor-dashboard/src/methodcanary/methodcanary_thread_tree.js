/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

import {Tree, Popover} from 'antd'

class MethodCanaryThreadTree extends Component {

    static getMethodValueWithRange(realValue, range) {
        return realValue === 0 ? range : realValue;
    }

    static inWhichParentItem(methodInfo, parentMethodInfos) {
        for (let i = 0; i < parentMethodInfos.length; i += 1) {
            const item = parentMethodInfos[i];
            if (methodInfo.startMillis >= item.startMillis && methodInfo.endMillis <= item.endMillis) {
                return item
            }
        }
        return null;
    }

    /**
     * method out of range totally
     * @param methodInfo
     * @param startMillis
     * @param endMillis
     */
    static isMethodOutOfRange(methodInfo, startMillis, endMillis) {
        return methodInfo.startMillis > endMillis || methodInfo.endMillis < startMillis;
    }


    static buildTree(methodInfos, startMillis, endMillis) {
        let treeData = [];
        let currentStack = 0;
        let parentList = [];
        let currentList = [];
        for (let i = 0; i < methodInfos.length; i += 1) {
            const item = methodInfos[i];
            if (MethodCanaryThreadTree.isMethodOutOfRange(item, startMillis, endMillis)) {
                continue;
            }
            if (item.stack === 0) {
                treeData.push(item)
            }
            if (item.stack > currentStack) {
                currentStack = item.stack;
                parentList = currentList;
                currentList = [];
            }
            currentList.push(item);
            const parent = MethodCanaryThreadTree.inWhichParentItem(item, parentList);
            if (parent) {
                parent.children.push(item);
            }
        }
        return treeData
    }

    static cloneMethodCanaryMethodInfo(methodInfo) {
        return {
            stack: methodInfo.stack,
            startMillis: methodInfo.startMillis,
            endMillis: methodInfo.endMillis,
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
        MethodCanaryThreadTree.getNodeDetailContent = MethodCanaryThreadTree.getNodeDetailContent.bind(this);
        this.clear = this.clear.bind(this);
        this.refresh = this.refresh.bind(this);
        this.state = {
            treeData: [],
            startMillis: 0,
            endMillis: 0
        }
    }

    getMethodStartInRange(realStart) {
        return realStart > this.state.startMillis ? realStart : this.state.startMillis;
    }

    getMethodEndInRange(realEnd) {
        return realEnd < this.state.endMillis ? realEnd : this.state.endMillis;
    }

    clear() {
        this.setState({
            treeData: [],
            startMillis: 0,
            endMillis: 0
        });
    }

    refresh(startMillis, endMillis, methodInfos) {
        const cloned = MethodCanaryThreadTree.cloneMethodCanaryMethodInfos(methodInfos);
        cloned.sort((a, b) => {
            if (a.stack === b.stack) {
                return a.startMillis - b.startMillis;
            }
            return a.stack - b.stack;
        });
        const treeData = MethodCanaryThreadTree.buildTree(cloned, startMillis, endMillis);
        this.setState({treeData, startMillis, endMillis})
    }

    static getNodeDetailContent(item) {
        return (<span>
            Real cost {Util.getFormatDuration(item.endMillis - item.startMillis)}<br />
            {item.className + "." + item.methodName}<br />
            From {Util.getDetailDate(item.startMillis)} to {Util.getDetailDate(item.endMillis)}
        </span>)
    }

    getRenderNodeText(item) {
        const content = MethodCanaryThreadTree.getNodeDetailContent(item);
        return <Popover content={content}>
            <span>
                [Cost and weight]&nbsp;
                <strong>{Util.getFormatDuration((this.getMethodEndInRange(item.endMillis) - this.getMethodStartInRange(item.startMillis)))}</strong>
                &nbsp;
                <strong>{((this.getMethodEndInRange(item.endMillis) - this.getMethodStartInRange(item.startMillis)) * 100 / (this.state.endMillis - this.state.startMillis)).toFixed(1) + "%"}</strong>
                &nbsp;&nbsp;
                [Method]&nbsp;
                <strong>{item.className.substring(item.className.lastIndexOf("/") + 1) + "." + item.methodName}</strong>
            </span>
        </Popover>
    }

    renderTreeNodes = data => data.map((item) => {
        if (item.children) {
            return (
                <Tree.TreeNode title={
                    this.getRenderNodeText(item)
                }
                               selectable={false}
                               key={`${item.stack}#${item.startMillis}#${item.endMillis}`}
                               dataRef={item}>
                    {this.renderTreeNodes(item.children)}
                </Tree.TreeNode>
            );
        }
        return <Tree.TreeNode {...item} title={
            this.getRenderNodeText(item)
        } selectable={false}
                              key={`${item.stack}#${item.startMillis}#${item.endMillis}`} dataRef={item} isLeaf/>;
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