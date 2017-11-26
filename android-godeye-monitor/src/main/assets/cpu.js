/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var cpuUtil = function () {

    var cpuChart;
    var cpuOptions;

    function setup(chartContainer) {
        cpuChart = echarts.init(chartContainer, 'dark');

        cpuOptions = {
            title: {
                text: 'CPU',
                left: "center",
                top: '2%'
            },
            legend: {
                data: ['总体使用率', 'APP使用率', '用户进程使用率', '系统进程使用率'],
                top: '10%'
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
                        tip = tip + data.seriesName + ' : ' + data.value.toFixed(3) + ' % <br/>';
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
                left: '3%',
                right: '4%',
                bottom: '3%',
                top: '20%',
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
                    name: "CPU占用(百分比)",
                    nameLocation: 'middle',
                    nameRotate: 90,
                    nameGap: 35,
                    min: 0,
                    max: 100
                }
            ],
            series: [
                {
                    name: '总体使用率',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: 'APP使用率',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '用户进程使用率',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '系统进程使用率',
                    type: 'line',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        cpuChart.setOption(cpuOptions);
    }

    function refreshCpu(cpuInfos) {
        for (var i = 0; i < cpuInfos.length; i++) {
            var cpuInfo = cpuInfos[i];
            var axisData = (new Date()).toLocaleTimeString();
            cpuOptions.xAxis[0].data.shift();
            cpuOptions.xAxis[0].data.push(axisData);
            cpuOptions.series[0].data.shift();
            cpuOptions.series[0].data.push(cpuInfo.totalUseRatio * 100);
            cpuOptions.series[1].data.shift();
            cpuOptions.series[1].data.push(cpuInfo.appCpuRatio * 100);
            cpuOptions.series[2].data.shift();
            cpuOptions.series[2].data.push(cpuInfo.userCpuRatio * 100);
            cpuOptions.series[3].data.shift();
            cpuOptions.series[3].data.push(cpuInfo.sysCpuRatio * 100);
        }
        cpuChart.setOption(cpuOptions);
    }

    return {
        setup: setup,
        refreshChart: refreshCpu
    }

}();
