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
        methodInfos.sort((a, b) => {
            if (a.stack === b.stack) {
                return a.start - b.start;
            }
            return a.stack - b.stack;
        });
        this.buildTree(start, end, start, end, methodInfos, null, new Set(), treeData);
        this.setState({treeData})
    }


    buildTree(originStart, originEnd, start, end, methodInfos, parent, added, treeData) {
        for (let i = 0; i < methodInfos.length; i += 1) {
            const item = methodInfos[i];
            if (!added.has(item) && ((start >= item.start && start <= item.end) || (end >= item.start && end <= item.end))) {
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

    renderTreeNodes(data) {
        data.map(item => {
            if (item.children) {
                const index = item.className.lastIndexOf("/");
                const className = item.className.substring(index + 1, item.className.length);
                return (
                    <Tree.TreeNode title={className + "#" + item.methodName} key={className + "#" + item.methodName}
                                   dataRef={item}>
                        {this.renderTreeNodes(item.children)}
                    </Tree.TreeNode>);
            }
            const index = item.className.lastIndexOf("/");
            const className = item.className.substring(index + 1, item.className.length);
            return (<Tree.TreeNode {...item} title={className + "#" + item.methodName}
                                   key={className + "#" + item.methodName} dataRef={item}/>);
        });
    }

    renderTreeNodes2 = data => data.map((item) => {
        if (item.children) {
            return (
                <Tree.TreeNode title={item.className} key={item.className} dataRef={item}>
                    {this.renderTreeNodes2(item.children)}
                </Tree.TreeNode>
            );
        }
        return <Tree.TreeNode {...item} title={item.className} key={item.className} dataRef={item} />;
    });


    render() {
        return (<div>
                <p>ceshi</p>
                <Tree>
                    <Tree.TreeNode title="ddd">
                        <Tree.TreeNode title="ddd">
                            <Tree.TreeNode title="ddd">

                            </Tree.TreeNode>
                        </Tree.TreeNode>
                    </Tree.TreeNode>
                    <Tree.TreeNode title="ddd">

                    </Tree.TreeNode>
                </Tree>
                <Tree>
                    {this.renderTreeNodes2(this.state.treeData)}
                </Tree></div>
        );
    }
}

export default MethodCanaryThreadTree;