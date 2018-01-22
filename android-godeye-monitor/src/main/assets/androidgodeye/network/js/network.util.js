/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var networkUtil = function () {

    var networkChart;
    var networkOptions;

    function setup(chartContainer) {
        networkChart = echarts.init(chartContainer, 'dark');
        networkOptions = {
            // title: {
            //     text: 'Network',
            //     left: "center",
            //     top: '3%'
            // },
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
                        tip = tip + data.seriesName + ' : ' + data.value.toFixed(1) + ' ms <br/>';
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
                left: '4%',
                right: '3%',
                bottom: '4%',
                top: '5%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: "RequestCostTime(ms)",
                    nameLocation: 'middle',
                    nameRotate: 90,
                    nameGap: 45
                }
            ],
            series: [
                {
                    name: 'request',
                    type: 'bar',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        networkChart.setOption(networkOptions);
    }

    function refreshNetwork(networkInfos) {
        for (var i = 0; i < networkInfos.length; i++) {
            var networkInfo = networkInfos[i];
            var axisData = networkInfo.url;
            networkOptions.xAxis[0].data.shift();
            networkOptions.xAxis[0].data.push(axisData);
            networkOptions.series[0].data.shift();
            networkOptions.series[0].data.push(networkInfo.endTimeMillis - networkInfo.startTimeMillis);
        }
        networkChart.setOption(networkOptions);
    }


    return {
        setup: setup,
        refreshNetwork: refreshNetwork
    }

}();
