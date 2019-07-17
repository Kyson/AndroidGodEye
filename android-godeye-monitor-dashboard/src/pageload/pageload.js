import React, {Component} from 'react';
import '../App.css';

import JSONPretty from '../../node_modules/react-json-pretty';

import Highcharts from '../../node_modules/highcharts/highcharts';
import exporting from '../../node_modules/highcharts/modules/exporting';
import {Card, Modal, Timeline, Button} from 'antd'
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
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.state = {
            show: false,
            thisPageLifecycleProcessedEvents: {},
            pageLifecycleProcessedEvents: []
        };
    }

    componentDidMount() {

    }

    componentDidUpdate() {

    }

    static isSamePage(pageInfo1, pageInfo2) {
        if (pageInfo1 && pageInfo2) {
            return pageInfo1.pageType === pageInfo2.pageType
                && pageInfo1.pageClassName === pageInfo2.pageClassName
                && pageInfo1.pageHashCode === pageInfo2.pageHashCode
        }
        return false;
    }

    static findThisPageLifecycleEvents(pageLifecycleProcessedEvents, pageLifecycleProcessedEvent) {
        const thisPageLifecycleEvents = [];
        pageLifecycleProcessedEvents.forEach((item) => {
            if (Pageload.isSamePage(item.pageInfo, pageLifecycleProcessedEvent.pageInfo)) {
                thisPageLifecycleEvents.push(item)
            }
        });
        return thisPageLifecycleEvents;
    }

    refresh(pageLifecycleProcessedEvent) {
        const events = this.state.pageLifecycleProcessedEvents;
        events.push(pageLifecycleProcessedEvent);
        this.setState({
            pageLifecycleProcessedEvents: events
        });
    }

    handleClick(event) {
        this.setState({
            show: true,
            thisPageLifecycleProcessedEvents: Pageload.findThisPageLifecycleEvents(this.state.pageLifecycleProcessedEvents, event)
        });
    }

    handleClose() {
        this.setState({show: false});
    }

    renderTimelines(pageLifecycleProcessedEvents) {
        if (pageLifecycleProcessedEvents) {
            let items = [];
            for (let i = 0; i < pageLifecycleProcessedEvents.length; i++) {
                const event = pageLifecycleProcessedEvents[i];
                if (event.pageLifecycleEventWithTime.lifecycleEvent === 'ON_LOAD') {
                    items.push(<div><Timeline.Item color={Util.getRed()}>
                        <p>{`${new Date(event.pageLifecycleEventWithTime.eventTimeMillis).toLocaleString()} | ${event.pageInfo.pageType}: ${event.pageLifecycleEventWithTime.lifecycleEvent}`}</p>
                        <p>
                            <strong>{`${event.pageInfo.pageClassName}`}</strong>{`@${event.pageInfo.pageHashCode}`}<Button
                            type="link"
                            onClick={() => {
                                this.handleClick(event);
                            }
                            }>Follow This
                            Page</Button>
                        </p>
                        <p>Load cost <strong
                            style={(event.processedInfo['loadTime'] > Pageload.BAD_LOAD_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['loadTime']}</strong> ms
                        </p>
                    </Timeline.Item></div>);
                } else if (event.pageLifecycleEventWithTime.lifecycleEvent === 'ON_DRAW') {
                    items.push(<div><Timeline.Item color={Util.getOrange()}>
                        <p>{`${new Date(event.pageLifecycleEventWithTime.eventTimeMillis).toLocaleString()} | ${event.pageInfo.pageType}: ${event.pageLifecycleEventWithTime.lifecycleEvent}`}</p>
                        <p>
                            <strong>{`${event.pageInfo.pageClassName}`}</strong>{`@${event.pageInfo.pageHashCode}`}<Button
                            type="link"
                            onClick={() => {
                                this.handleClick(event);
                            }
                            }>Follow This
                            Page</Button>
                        </p>
                        <p>Draw cost <strong
                            style={(event.processedInfo['drawTime'] > Pageload.BAD_DRAW_TIME) ? {color: Util.getRed()} : {color: Util.getGreen()}}>{event.processedInfo['drawTime']}</strong> ms
                        </p>
                    </Timeline.Item></div>);
                } else {
                    items.push(<div><Timeline.Item color={Util.getGreen()}>
                        <p>{`${new Date(event.pageLifecycleEventWithTime.eventTimeMillis).toLocaleString()} | ${event.pageInfo.pageType}: ${event.pageLifecycleEventWithTime.lifecycleEvent}`}</p>
                        <p>
                            <strong>{`${event.pageInfo.pageClassName}`}</strong>{`@${event.pageInfo.pageHashCode}`}<Button
                            type="link"
                            onClick={() => {
                                this.handleClick(event);
                            }
                            }>Follow This
                            Page</Button>
                        </p>
                    </Timeline.Item></div>);
                }
            }
            return items;
        }
    }

    render() {
        return (
            <Card title="Page Lifecycle(页面生命周期)">
                <div style={{height: 200}}>
                    <ScrollableFeed changeDetectionFilter={(previousProps, newProps) => {
                        const prevChildren = previousProps.children;
                        const newChildren = newProps.children;
                        return prevChildren.length !== newChildren.length;
                    }}>
                        {this.renderTimelines(this.state.pageLifecycleProcessedEvents)}

                    </ScrollableFeed>
                </div>
                <Modal visible={this.state.show} onCancel={this.handleClose} title="Page Lifecycle Event Flow"
                       closable={true}
                       onOk={this.handleClose}>
                    <JSONPretty id="json-pretty" json={this.state.thisPageLifecycleProcessedEvents}
                                style={{fontSize: 8}}/>
                </Modal>
            </Card>);
    }
}

export default Pageload;
