import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Button} from 'react-bootstrap'

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
            <div className="span12">
                <p>
                    Status:{this.state.canRefresh ? "Refreshing..." : "Stopped."} |
                    Last update time:{this.state.lastUpdateTime.toISOString()}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <Button
                        onClick={this._handleRefreshStatus}>{this.state.canRefresh ? "Stop" : "Start"}</Button>
                </p>
            </div>
        );
    }
}

export default RefreshStatus;
