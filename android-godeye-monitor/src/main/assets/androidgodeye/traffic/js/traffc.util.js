/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var trafficUtil = function () {

    var trafficChart;
    var trafficOptions;

    function setup(chartContainer) {
        trafficChart = echarts.init(chartContainer, 'dark');

        trafficOptions = {
            // title: {
            //     text: 'TRAFFIC',
            //     left: "center",
            //     top: '2%'
            // },
            legend: {
                data: ['DeviceRX', 'DeviceTX', 'AppRX', 'AppTX'],
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
                        tip = tip + data.seriesName + ' : ' + data.value.toFixed(3) + ' KB/S <br/>';
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
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: "Traffic(KB/S)",
                    nameLocation: 'middle',
                    nameRotate: 90,
                    nameGap: 35
                }
            ],
            series: [
                {
                    name: 'DeviceRX',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'DeviceTX',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'AppRX',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'AppTX',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        trafficChart.setOption(trafficOptions);
    }

    function refreshTraffic(trafficInfos) {
        for (var i = 0; i < trafficInfos.length; i++) {
            var trafficInfo = trafficInfos[i];
            var axisData = (new Date()).toLocaleTimeString();
            trafficOptions.xAxis[0].data.shift();
            trafficOptions.xAxis[0].data.push(axisData);
            trafficOptions.series[0].data.shift();
            trafficOptions.series[0].data.push(trafficInfo.rxTotalRate);
            trafficOptions.series[1].data.shift();
            trafficOptions.series[1].data.push(trafficInfo.txTotalRate);
            trafficOptions.series[2].data.shift();
            trafficOptions.series[2].data.push(trafficInfo.rxUidRate);
            trafficOptions.series[3].data.shift();
            trafficOptions.series[3].data.push(trafficInfo.txUidRate);
        }
        trafficChart.setOption(trafficOptions);
    }

    return {
        setup: setup,
        refreshTraffic: refreshTraffic
    }

}();
