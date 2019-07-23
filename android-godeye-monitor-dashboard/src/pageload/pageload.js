import React, {Component} from 'react';
import '../App.css';

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import {Card, Badge, Button, Tag} from 'antd'
import Util from "../libs/util";
import ScrollableFeed from 'react-scrollable-feed';

exporting(Highcharts);

/**
 * Pageload
 */
class Pageload extends Component {
    static BAD_DRAW_TIME = 800;
    static BAD_LOAD_TIME = 2000;

    constructor(props) {
        super(props);
        this.handleFollow = this.handleFollow.bind(this);
        this.handleUnfollow = this.handleUnfollow.bind(this);
        this.allPageLifecycleProcessedEvents = [];
        this.state = {
            show: false,
            followPageLifecycleProcessedEvent: null,
            renderPageLifecycleProcessedEvents: []
        };
    }

    static isSamePageClass(pageInfo1, pageInfo2) {
        if (pageInfo1 && pageInfo2) {
            return pageInfo1.pageType === pageInfo2.pageType
                && pageInfo1.pageClassName === pageInfo2.pageClassName
        }
        return false;
    }

    static findThisPageLifecycleEvents(pageLifecycleProcessedEvents, pageLifecycleProcessedEvent) {
        if (pageLifecycleProcessedEvent) {
            const thisPageLifecycleEvents = [];
            pageLifecycleProcessedEvents.forEach((item) => {
                if (Pageload.isSamePageClass(item.pageInfo, pageLifecycleProcessedEvent.pageInfo)) {
                    thisPageLifecycleEvents.push(item)
                }
            });
            return thisPageLifecycleEvents;
        }
        return pageLifecycleProcessedEvents;
    }

    refresh(pageLifecycleProcessedEvent) {
        this.allPageLifecycleProcessedEvents.push(pageLifecycleProcessedEvent);
        this.setState({
            renderPageLifecycleProcessedEvents: Pageload.findThisPageLifecycleEvents(this.allPageLifecycleProcessedEvents, this.state.followPageLifecycleProcessedEvent)
        });
    }

    handleFollow(pageLifecycleProcessedEvent) {
        this.setState({
            followPageLifecycleProcessedEvent: pageLifecycleProcessedEvent,
            renderPageLifecycleProcessedEvents: Pageload.findThisPageLifecycleEvents(this.allPageLifecycleProcessedEvents, pageLifecycleProcessedEvent)
        });
    }

    handleUnfollow() {
        console.log("handleUnfollow");
        this.setState({
            followPageLifecycleProcessedEvent: null,
            renderPageLifecycleProcessedEvents: Pageload.findThisPageLifecycleEvents(this.allPageLifecycleProcessedEvents, null)
        });
    }

    renderTimelines(pageLifecycleProcessedEvents) {
        if (pageLifecycleProcessedEvents) {
            let items = [];
            for (let i = 0; i < pageLifecycleProcessedEvents.length; i++) {
                const event = pageLifecycleProcessedEvents[i];
                if (event.lifecycleEvent === 'ON_LOAD') {
                    items.push(<Card style={{margin: 4}} size="small">
                        <Badge
                            color={Util.getGreen()}/><span>{`${new Date(event.eventTimeMillis).toLocaleString()}`}</span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <strong>{`${event.pageClassName}`}</strong>{`@${event.pageHashCode}`}<Button
                                type="link"
                                onClick={() => {
                                    this.handleFollow(event);
                                }
                                }>Follow This Class of Page</Button>
                         </span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <Tag color="cyan">{event.pageType}</Tag>
                            <Tag color="geekblue">{event.lifecycleEvent}</Tag>
                            Load cost <strong
                                style={(event.processedInfo['loadTime'] > Pageload.BAD_LOAD_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['loadTime']}</strong> ms
                        </span>
                    </Card>);
                } else if (event.lifecycleEvent === 'ON_DRAW') {
                    items.push(<Card style={{margin: 4}} size="small">
                        <Badge
                            color={Util.getGreen()}/><span>{`${new Date(event.eventTimeMillis).toLocaleString()}`}</span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <strong>{`${event.pageClassName}`}</strong>{`@${event.pageHashCode}`}<Button
                                type="link"
                                onClick={() => {
                                    this.handleFollow(event);
                                }
                                }>Follow This Class of Page</Button>
                        </span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <Tag color="cyan">{event.pageType}</Tag>
                            <Tag color="geekblue">{event.lifecycleEvent}</Tag>
                            Draw cost <strong
                                style={(event.processedInfo['drawTime'] > Pageload.BAD_DRAW_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['drawTime']}</strong> ms
                        </span>
                    </Card>);
                } else {
                    items.push(<Card style={{margin: 4}} size="small">
                        <Badge
                            color={Util.getGreen()}/><span>{`${new Date(event.eventTimeMillis).toLocaleString()}`}</span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <strong>{`${event.pageClassName}`}</strong>{`@${event.pageHashCode}`}<Button
                                type="link"
                                onClick={() => {
                                    this.handleFollow(event);
                                }
                                }>Follow This Class of Page</Button>
                        </span>
                        <br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <Tag color="cyan">{event.pageType}</Tag>
                            <Tag color="geekblue">{event.lifecycleEvent}</Tag>
                        </span>
                    </Card>);
                }
            }
            return items;
        }
    }

    renderTip(followClass) {
        if (followClass) {
            return (<div><span>Following [{followClass}]&nbsp;&nbsp;</span>
                <Button onClick={this.handleUnfollow}>Unfollow</Button></div>)
        }
        return <div></div>
    }

    render() {
        let followClass;
        if (this.state.followPageLifecycleProcessedEvent) {
            followClass = this.state.followPageLifecycleProcessedEvent.pageClassName
        }
        return (
            <Card title="Page Lifecycle(页面生命周期)" extra={this.renderTip(followClass)}>
                <div style={{height: 670}}>
                    <ScrollableFeed changeDetectionFilter={(previousProps, newProps) => {
                        const prevChildren = previousProps.children;
                        const newChildren = newProps.children;
                        return prevChildren.length !== newChildren.length;
                    }}>
                        {this.renderTimelines(this.state.renderPageLifecycleProcessedEvents)}
                    </ScrollableFeed>
                </div>
            </Card>);
    }
}

export default Pageload;
