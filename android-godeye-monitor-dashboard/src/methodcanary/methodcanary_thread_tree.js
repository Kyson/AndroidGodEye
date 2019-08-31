/* eslint-disable react/no-string-refs */
/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import Util from "../libs/util";

import {Card, Button, Row, Col, Tree} from 'antd'

class MethodCanaryThreadTree extends Component {

    constructor(props) {
        super(props);
        this.renderTreeNodes = this.renderTreeNodes.bind(this);
        this.buildTree = this.buildTree.bind(this);
        this.refresh = this.refresh.bind(this);
        this.state = {
            treeData: []
        }
    }

    refresh(start, end, methodInfos) {
        const treeData = [];
        this.buildTree(start, end, start, end, methodInfos, null, new Set(), treeData);
        this.setState({treeData})
    }


    buildTree(originStart, originEnd, start, end, methodInfos, parent, added, treeData) {
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

    renderTreeNodes = data => data.map((item) => {
        if (item.children) {
            return (
                <Tree.TreeNode title={item.className + "#" + item.methodName}
                               key={`${item.stack}#${item.start}#${item.end}`}
                               dataRef={item}>
                    {this.renderTreeNodes(item.children)}
                </Tree.TreeNode>
            );
        }
        return <Tree.TreeNode {...item} title={item.className + "#" + item.methodName}
                              key={`${item.stack}#${item.start}#${item.end}`} dataRef={item}/>;
    });

    render() {
        return (<div>
                <Tree>
                    {this.renderTreeNodes(this.state.treeData)}
                </Tree></div>
        );
    }
}

export default MethodCanaryThreadTree;