import React, {Component} from 'react';
import '../App.css';

import {Card, Badge, Button, Tag, Input} from 'antd'
import Util from "../libs/util";

/**
 * Pageload
 */
class Pageload extends Component {
    static BAD_DRAW_TIME = 800;
    static BAD_LOAD_TIME = 2000;
    static BAD_LIFECYCLE_TIME = 1000;

    static isPageInSearch(pageLifecycleProcessedEvent, searchText) {
        searchText = searchText.toLowerCase();
        if (pageLifecycleProcessedEvent) {
            if (JSON.stringify(pageLifecycleProcessedEvent).toString().toLowerCase().search(searchText) !== -1) {
                return true
            }
        }
        return false;
    }

    static findThisPageLifecycleEvents(pageLifecycleProcessedEvents, searchText) {
        if (searchText) {
            const thisPageLifecycleEvents = [];
            pageLifecycleProcessedEvents.forEach((item) => {
                if (Pageload.isPageInSearch(item, searchText)) {
                    thisPageLifecycleEvents.push(item)
                }
            });
            return thisPageLifecycleEvents;
        }
        return pageLifecycleProcessedEvents;
    }

    constructor(props) {
        super(props);
        this.renderExtra = this.renderExtra.bind(this);
        this.renderTimelines = this.renderTimelines.bind(this);
        this.handleClear = this.handleClear.bind(this);
        this.renderItem = this.renderItem.bind(this);
        this.refresh = this.refresh.bind(this);
        this.state = {
            searchText: null,
            show: false,
            allPageLifecycleProcessedEvents: []
        };
    }

    refresh(pageLifecycleProcessedEvent) {
        this.setState(function (prevState, props) {
            const allPageLifecycleProcessedEvents = prevState.allPageLifecycleProcessedEvents;
            allPageLifecycleProcessedEvents.unshift(pageLifecycleProcessedEvent);
            return {allPageLifecycleProcessedEvents: allPageLifecycleProcessedEvents};
        });
    }

    handleClear() {
        this.setState({
            allPageLifecycleProcessedEvents: [],
        });
    }

    renderItem(event, key) {
        return (
            <Card style={{margin: 4}} size="small" key={key}>
                <Badge
                    color={Util.getGreen()}/><span>{`${new Date(event.startTimeMillis).toLocaleString()}.${event.startTimeMillis % 1000}`}</span>
                <br />
                <span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <strong>{`${event.pageClassName}`}</strong>{`@${event.pageHashCode}`}</span>
                <br />
                <span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <Tag color={"ACTIVITY" === event.pageType ? "cyan" : "green"}>{event.pageType}</Tag>
                    <Tag
                        color={('ON_LOAD' === event.lifecycleEvent || 'ON_DRAW' === event.lifecycleEvent) ? "red" : "orange"}>{event.lifecycleEvent}</Tag>
                    {(() => {
                        if (event.lifecycleEvent === 'ON_LOAD') {
                            if (event.processedInfo['loadTime'] > 0) {
                                return <span>Cost <strong
                                    style={(event.processedInfo['loadTime'] > Pageload.BAD_LOAD_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['loadTime']}</strong> ms</span>
                            }
                        } else if (event.lifecycleEvent === 'ON_DRAW') {
                            if (event.processedInfo['drawTime'] > 0) {
                                return <span>Cost <strong
                                    style={(event.processedInfo['drawTime'] > Pageload.BAD_DRAW_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['drawTime']}</strong> ms</span>
                            }
                        } else {
                            if ((event.endTimeMillis - event.startTimeMillis) > 0) {
                                return <span>Cost <strong
                                    style={((event.endTimeMillis - event.startTimeMillis) > Pageload.BAD_LIFECYCLE_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{(event.endTimeMillis - event.startTimeMillis)}</strong> ms</span>
                            }
                        }
                    })()}
                </span>
            </Card >
        );
    }

    renderTimelines() {
        const pageLifecycleProcessedEvents = Pageload.findThisPageLifecycleEvents(this.state.allPageLifecycleProcessedEvents, this.state.searchText)
        if (pageLifecycleProcessedEvents) {
            let items = [];
            for (let i = 0; i < pageLifecycleProcessedEvents.length; i++) {
                const event = pageLifecycleProcessedEvents[i];
                items.push(this.renderItem(event, i))
            }
            return items;
        }
    }

    renderExtra() {
        return (<span>
            <Input.Search
                style={{width: 200}}
                placeholder="Input search text"
                onSearch={value => this.setState({searchText: value})}
            />
            &nbsp;&nbsp;
            <Button
                onClick={this.handleClear}>Clear</Button>

        </span>)
    }

    render() {
        return (
            <Card title="Page Lifecycle(页面生命周期)" extra={this.renderExtra()}>
                <div style={{height: 600, overflow: 'auto'}}>
                    {this.renderTimelines()}
                </div>
            </Card>);
    }
}

export default Pageload;
