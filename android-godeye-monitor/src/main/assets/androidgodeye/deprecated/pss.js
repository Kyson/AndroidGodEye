// /**
//  * Created by kysonchao on 2017/9/4.
//  */
// 'use strict';
// var pssUtil = function () {
//
//     var pssChart;
//     var pssOptions;
//
//     function setup(chartContainer) {
//         pssChart = echarts.init(chartContainer, 'dark');
//
//         pssOptions = {
//             title: {
//                 text: 'PSS',
//                 left: "center",
//                 top: '2%'
//             },
//             legend: {
//                 data: ['totalPss', 'dalvikPss', 'nativePss', 'otherPss'],
//                 top: '10%'
//             },
//             tooltip: {
//                 trigger: 'axis',
//                 axisPointer: {
//                     type: 'cross',
//                     label: {
//                         backgroundColor: '#6a7985'
//                     }
//                 },
//                 formatter: function (params) {
//                     var tip = "";
//                     for (var i = 0; i < params.length; i++) {
//                         var data = params[i];
//                         tip = tip + data.seriesName + ' : ' + data.value.toFixed(3) + ' M <br/>';
//                     }
//                     return tip;
//                 }
//             },
//             animation: true,
//             dataZoom: {
//                 show: false,
//                 start: 0,
//                 end: 100
//             },
//             toolbox: {
//                 feature: {
//                     saveAsImage: {}
//                 }
//             },
//             grid: {
//                 left: '3%',
//                 right: '4%',
//                 bottom: '3%',
//                 top: '20%',
//                 containLabel: true
//             },
//             xAxis: [
//                 {
//                     type: 'category',
//                     boundaryGap: false,
//                     data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//                 }
//             ],
//             yAxis: [
//                 {
//                     type: 'value',
//                     name: "物理内存(MB)",
//                     nameLocation: 'middle',
//                     nameRotate: 90,
//                     nameGap: 35
//                 }
//             ],
//             series: [
//                 {
//                     name: 'totalPss',
//                     type: 'line',
//                     data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//                 },
//                 {
//                     name: 'dalvikPss',
//                     type: 'line',
//                     stack: 'pss',
//                     areaStyle: {normal: {}},
//                     data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//                 }
//                 ,
//                 {
//                     name: 'nativePss',
//                     type: 'line',
//                     stack: 'pss',
//                     areaStyle: {normal: {}},
//                     data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//                 },
//                 {
//                     name: 'otherPss',
//                     type: 'line',
//                     stack: 'pss',
//                     areaStyle: {normal: {}},
//                     data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//                 }
//             ]
//         };
//         pssChart.setOption(pssOptions);
//     }
//
//     function refreshPss(pssInfos) {
//         for (var i = 0; i < pssInfos.length; i++) {
//             var pssInfo = pssInfos[i];
//             var axisData = (new Date()).toLocaleTimeString();
//             pssOptions.xAxis[0].data.shift();
//             pssOptions.xAxis[0].data.push(axisData);
//             pssOptions.series[0].data.shift();
//             pssOptions.series[0].data.push(pssInfo.totalPss / 1024);
//             pssOptions.series[1].data.shift();
//             pssOptions.series[1].data.push(pssInfo.dalvikPss / 1024);
//             pssOptions.series[2].data.shift();
//             pssOptions.series[2].data.push(pssInfo.nativePss / 1024);
//             pssOptions.series[3].data.shift();
//             pssOptions.series[3].data.push(pssInfo.otherPss / 1024);
//         }
//         pssChart.setOption(pssOptions);
//     }
//
//     return {
//         setup: setup,
//         refreshChart: refreshPss
//     }
// }();
