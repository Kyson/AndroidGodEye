import React, {Component} from 'react';
import '../App.css';
import {Label} from 'react-bootstrap';
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
            <div style={{alignItems: "flex-start"}}>
                <h1 style={{textAlign: "left"}}>
                    {this.props.appInfo ? this.props.appInfo.appName : "**"}
                </h1>
                <div style={{alignItems: "flex-start", textAlign: "left"}}>
                    {this.renderLabel(this.props.appInfo ? this.props.appInfo.labels : [])}</div>
            </div>
        );
    }

    renderLabel(labels) {
        if (labels) {
            var items = [];
            var styles = ["default", "primary", "success", "warning", "danger"];
            for (let i = 0; i < labels.length; i++) {
                items.push(<Label bsStyle={styles[i % 5]} style={{marginRight: 5}} key={i}>{labels[i]}</Label>);
            }
            return items;
        }
    }
}

export default AppInfo;
