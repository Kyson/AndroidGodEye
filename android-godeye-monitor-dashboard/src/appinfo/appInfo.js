import React, {Component} from 'react';
import '../App.css';
import {Label, Row, Col, Panel, Badge} from 'react-bootstrap';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';

/**
 * 应用基本信息
 */
class AppInfo extends Component {

    constructor(props) {
        super(props);
        this.renderLabel = this.renderLabel.bind(this);
    }

    render() {
        return (
            <div>
                <h1>
                    {this.props.appInfo ? this.props.appInfo.appName : "**"}
                </h1>
                <Row style={{padding: 15}}>{this.renderLabel(this.props.appInfo ? this.props.appInfo.labels : [])}</Row>
            </div>
        );
    }

    renderLabel(labels) {
        if (labels) {
            var items = [];
            var styles = ["#EB4334", "#4586F3", "#FBBD06", "#35AA53", "#999999"];
            for (let i = 0; i < labels.length; i++) {
                items.push(<Badge style={{marginRight: 5, backgroundColor: styles[i % 5]}} key={i}>{labels[i]}</Badge>);
            }
            return items;
        }
    }
}

export default AppInfo;
