/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var heapUtil = function () {

    var heapChart;
    var heapOptions;

    function setup(chartContainer) {
        heapChart = echarts.init(chartContainer, 'dark');

        heapOptions = {
            // title: {
            //     text: 'HEAP',
            //     left: "center",
            //     top: '2%'
            // },
            legend: {
                data: ['Allocated', 'Free'],
                top: '2%'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                },
                formatter: function (params) {
                    var tip = "";
                    for (var i = 0; i < params.length; i++) {
                        var data = params[i];
                        tip = tip + data.seriesName + ' : ' + data.value.toFixed(3) + ' MB <br/>';
                    }
                    return tip;
                }
            },
            animation: true,
            dataZoom: {
                show: false,
                start: 0,
                end: 100
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            grid: {
                left: '6%',
                right: '5%',
                bottom: '5%',
                top: '15%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: "Heap(MB)",
                    nameLocation: 'middle',
                    nameRotate: 90,
                    nameGap: 35
                }
            ],
            series: [
                {
                    name: 'Allocated',
                    type: 'line',
                    stack: 'heap',
                    areaStyle: {normal: {}},
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'Free',
                    type: 'line',
                    stack: 'heap',
                    areaStyle: {normal: {}},
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        heapChart.setOption(heapOptions);
    }

    function refreshHeap(heapInfos) {
        for (var i = 0; i < heapInfos.length; i++) {
            var heapInfo = heapInfos[i];
            var axisData = (new Date()).toLocaleTimeString();
            heapOptions.xAxis[0].data.shift();
            heapOptions.xAxis[0].data.push(axisData);
            heapOptions.series[0].data.shift();
            heapOptions.series[0].data.push(heapInfo.allocatedKb / 1024);
            heapOptions.series[1].data.shift();
            heapOptions.series[1].data.push(heapInfo.freeMemKb / 1024);
        }
        heapChart.setOption(heapOptions);
    }

    return {
        setup: setup,
        refreshChart: refreshHeap
    }

}();
