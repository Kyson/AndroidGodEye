/* eslint-disable react/prop-types */
import React, {Component} from 'react';
import '../App.css';
import {Button} from 'antd'

/**
 * 刷新状态
 */
class RefreshStatus extends Component {

    constructor(props) {
        super(props);
        this._handleRefreshStatus = this._handleRefreshStatus.bind(this);
        this.setCanRefresh = props.setCanRefresh;
        this.state = {
            canRefresh: true,
            lastUpdateTime: new Date()
        }
    }

    _handleRefreshStatus() {
        this.setState(function (prevState, props) {
            this.setCanRefresh(!prevState.canRefresh);
            return {canRefresh: !prevState.canRefresh};
        });
    }

    refresh(time) {
        this.setState({lastUpdateTime: time});
    }

    render() {
        return (
            <div style={{paddingLeft: 28, paddingRight: 28}}>
                <span>
                    Status:{this.state.canRefresh ? "Refreshing..." : "Stopped."} |
                    Last update time:{this.state.lastUpdateTime.toISOString()}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <Button
                        onClick={this._handleRefreshStatus}>{this.state.canRefresh ? "Stop" : "Start"}</Button>
                </span>
            </div>
        );
    }
}

export default RefreshStatus;
