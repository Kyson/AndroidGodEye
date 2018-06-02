import React, {Component} from 'react';
import '../App.css';
import '../../node_modules/bootstrap/dist/css/bootstrap-theme.min.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Button} from 'react-bootstrap'
import PageVisibility from 'react-page-visibility';

/**
 * 刷新状态
 */
class RefreshStatus extends Component {

    constructor(props) {
        super(props);
        this._handleRefreshStatus = this._handleRefreshStatus.bind(this);
        this._appVisibilityChange = this._appVisibilityChange.bind(this);
        this.setCanRefresh = props.setCanRefresh;
        this.state = {
            canRefresh: true
        }
    }

    _handleRefreshStatus() {
        this.setState(function (prevState, props) {
            this.setCanRefresh(!prevState.canRefresh);
            return {canRefresh: !prevState.canRefresh};
        });
    }

    _appVisibilityChange(isVisible) {
        this.setState({canRefresh: isVisible});
        this.setCanRefresh(isVisible);
        console.log("PageVisibility:" + this.state.canRefresh);
    }

    render() {
        return (
            <PageVisibility onChange={this._appVisibilityChange}>
                <div className="span12">
                    <p>
                        Status:&nbsp;&nbsp;&nbsp;{this.state.canRefresh ? "Refreshing..." : "Stopped."} | 进入后台将自动停止刷新&nbsp;&nbsp;&nbsp;&nbsp;<Button
                        onClick={this._handleRefreshStatus}>{this.state.canRefresh ? "Stop" : "Start"}</Button>
                    </p>
                </div>
            </PageVisibility>
        );
    }
}

export default RefreshStatus;
