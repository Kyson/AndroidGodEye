import React, {Component} from 'react';
import '../App.css';

/**
 * Heap
 */
class HeapInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            heapInfo: {}
        };
    }

    refresh(heapInfo) {
        if (heapInfo) {
            this.setState({
                heapInfo: {
                    allocatedMB: (heapInfo.allocatedKb / 1024).toFixed(1),
                    freeMemMB: (heapInfo.freeMemKb / 1024).toFixed(1),
                    maxMemMB: (heapInfo.maxMemKb / 1024).toFixed(1)
                }
            })
        } else {
            this.setState({
                heapInfo: {
                    allocatedMB: "*",
                    freeMemMB: "*",
                    maxMemMB: "*"
                }
            })
        }
    }

    render() {
        return (
            <span style={{fontSize: 15}}>Allocated:&nbsp;
                <span style={{fontSize: 25}}>{this.state.heapInfo.allocatedMB}</span>
                &nbsp;&nbsp;&nbsp;Max:&nbsp;
                <span style={{fontSize: 25}}>{this.state.heapInfo.maxMemMB}</span>
                &nbsp;&nbsp;MB
            </span>
        )
    }
}

export default HeapInfo;
