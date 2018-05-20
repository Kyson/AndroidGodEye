import React, {Component} from 'react';
import '../App.css';
import {Label, Row, Col, Panel, Badge} from 'react-bootstrap';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import Util from '../libs/util'

/**
 * 应用基本信息
 */
class AppInfo extends Component {

    constructor(props) {
        super(props);
        this.renderLabel = this.renderLabel.bind(this);
        this.state = {
            appInfo: {}
        }
    }

    refresh(appInfo) {
        this.setState({appInfo});
    }

    render() {
        return (
            <div>
                <h1>
                    {this.state.appInfo ? this.state.appInfo.appName : "**"}
                </h1>
                <Row style={{padding: 15}}>{this.renderLabel(this.state.appInfo ? this.state.appInfo.labels : [])}</Row>
            </div>
        );
    }

    renderLabel(labels) {
        if (labels) {
            let items = [];
            let styles = Util.getCommonColors();
            let styleCount = styles.length;
            for (let i = 0; i < labels.length; i++) {
                items.push(<Badge style={{marginRight: 5, backgroundColor: styles[i % styleCount]}}
                                  key={i}>{labels[i]}</Badge>);
            }
            return items;
        }
    }
}

export default AppInfo;
