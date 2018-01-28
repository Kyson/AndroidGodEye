/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var pageloadUtil = function () {

    // var pageloadChart;
    // var pageloadOptions;
    //
    // function setup(chartContainer) {
    //     pageloadChart = echarts.init(chartContainer, 'dark');
    //     var ss = [];
    //     for (var i = 0; i < 10; i++) {
    //         ss.push(createItem("", 0));
    //     }
    //     pageloadOptions = {
    //         tooltip: {
    //             type: 'axis'
    //         },
    //         color: ['#EB4334', '#4586F3', '#FBBD06', '#35AA53'],
    //         xAxis3D: {
    //             show: false,
    //             type: 'category',
    //             name: '',
    //             data: [''],
    //             axisLine: {
    //                 lineStyle: {
    //                     color: 'rgba(0,0,0,0)'
    //                 }
    //             }
    //         },
    //         yAxis3D: {
    //             show: false,
    //             type: 'category',
    //             name: '',
    //             data: [''],
    //             axisLine: {
    //                 lineStyle: {
    //                     color: 'rgba(0,0,0,0)'
    //                 }
    //             }
    //         },
    //         zAxis3D: {
    //             show: true,
    //             type: 'value',
    //             name: 'drawTime(ms)',
    //             axisLine: {
    //                 lineStyle: {
    //                     color: 'rgba(0,0,0,0)'
    //                 }
    //             }
    //         },
    //         grid3D: {
    //             boxWidth: 80,
    //             boxDepth: 80,
    //             axisPointer: {
    //                 show: false
    //             },
    //             light: {
    //                 main: {
    //                     intensity: 1.2
    //                 },
    //                 ambient: {
    //                     intensity: 0.3
    //                 }
    //             }
    //         },
    //         series: ss
    //     };
    //     pageloadChart.setOption(pageloadOptions);
    // }
    //
    //
    // function createItem(pageName, drawTime) {
    //     return {
    //         type: 'bar3D',
    //         stack: 'activity',
    //         name: pageName,
    //         barSize: 80,
    //         data: [0, 0, drawTime],
    //         label: {
    //             show: false,
    //             textStyle: {
    //                 fontSize: 16,
    //                 borderWidth: 1
    //             }
    //         },
    //         itemStyle: {
    //             opacity: 0.8
    //         },
    //         shading: "lambert",
    //         emphasis: {
    //             label: {
    //                 textStyle: {
    //                     fontSize: 20,
    //                     color: '#900'
    //                 }
    //             }
    //         }
    //     }
    // }
    //
    //
    // function refreshPageload(pageInfos) {
    //     pageloadOptions.series = [];
    //     for (var i = 0; i < pageInfos.length; i++) {
    //         var pageInfo = pageInfos[i];
    //         if (!pageInfo) {//pageinfo为空
    //             continue;
    //         }
    //         //默认给个10ms
    //         var didLoadTime = 10;
    //         if (pageInfo.loadTimeInfo && pageInfo.loadTimeInfo.didDrawTime) {
    //             didLoadTime = pageInfo.loadTimeInfo.didDrawTime;
    //         }
    //         pageloadOptions.series.push(createItem(pageInfo.pageName, didLoadTime));
    //     }
    //     pageloadChart.setOption(pageloadOptions, true, true);
    // }

    var pageloadChart;


    function refreshPageload(pageloadInfos) {
        var hseries = [];
        if (pageloadInfos && pageloadInfos.length > 0) {
            for (var i = 0; i < pageloadInfos.length; i++) {
                var pageInfo = pageloadInfos[i];
                if (!pageInfo) {//pageinfo为空
                    continue;
                }
                //默认给个10ms
                var didLoadTime = 10;
                if (pageInfo.loadTimeInfo && pageInfo.loadTimeInfo.didDrawTime) {
                    didLoadTime = pageInfo.loadTimeInfo.didDrawTime;
                }
                hseries.push(createSeriresItem(pageInfo.pageName, didLoadTime));
            }
        }
        var diff = pageloadChart.series.length - hseries.length;
        if (diff > 0) {//当前的序列数大于要渲染的序列数
            for (var m = 0; m < diff; m++) {
                pageloadChart.series[m].remove(true);
            }
        } else if (diff < 0) {
            for (var j = 0; j < Math.abs(diff); j++) {
                pageloadChart.addSeries({});
            }
        }
        pageloadChart.update({
            series: hseries.reverse()
        });
    }


    function createSeriresItem(pageName, drawTime) {
        return {
            name: pageName,
            data: [drawTime],
            stack: 'activity'
        }
    }

    function setup(chart_dom) {
        var highchartsOption = {
            title: {
                text: null
            },
            exporting: {
                enabled: false
            },
            chart: {
                type: 'column',
                options3d: {
                    enabled: true,
                    alpha: 15,
                    beta: 15,
                    viewDistance: 0,
                    depth: 400
                },
                animation: false
            },
            xAxis: {
                categories: ['']
            },
            yAxis: {
                allowDecimals: false,
                title: {
                    text: 'ActivityDrawTime(ms)'
                }
            },
            tooltip: {
                headerFormat: '<b>{point.key}</b></br>',
                pointFormat: '<span style="color:{series.color}">\u25CF</span> {series.name} draw time:{point.y}ms'
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    depth: 400
                },
                animation: false
            },
            series: []
        };
        pageloadChart = Highcharts.chart(chart_dom, highchartsOption);
    }

    return {
        setup: setup,
        refreshPageload: refreshPageload
    }
}();
