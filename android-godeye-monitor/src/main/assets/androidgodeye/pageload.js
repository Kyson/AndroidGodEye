/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var pageloadUtil = function () {

    var pageloadChart;
    var pageloadOptions;
    var pageloadDetailDatas;

    function setup(chartContainer) {
        pageloadChart = echarts.init(chartContainer, 'dark');
        pageloadDetailDatas = ["", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""];
        pageloadOptions = {
            title: {
                text: 'PAGE LOAD',
                left: "center",
                top: '2%'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                },
                formatter: function (params) {
                    var draw = params[1];
                    var line1 = draw.name + '<br/>';
                    var line2 = draw.seriesName + ' : ' + draw.value + ' ms <br/>';
                    var req = params[3];
                    var line3 = req.seriesName + ' : ' + req.value + ' ms <br/>';
                    var redraw = params[5];
                    var line4 = redraw.seriesName + ' : ' + redraw.value + ' ms <br/>';
                    return line1 + line2 + line3 + line4;
                }
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '15%',
                top: '10%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: -30
                },
                data: ["", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""]
            }],
            yAxis: [{
                type: 'value',
                name: "加载时间(ms)",
                nameLocation: 'middle',
                nameRotate: 90,
                nameGap: 35
            }],
            series: [
                {
                    name: '绘制隐藏',
                    type: 'bar',
                    barGap: 0,
                    stack: 'draw',
                    itemStyle: {
                        normal: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        },
                        emphasis: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '绘制',
                    type: 'bar',
                    barGap: 0,
                    stack: 'draw',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '请求隐藏',
                    type: 'bar',
                    stack: 'request',
                    barGap: 0,
                    itemStyle: {
                        normal: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        },
                        emphasis: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '请求',
                    type: 'bar',
                    barGap: 0,
                    stack: 'request',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
                ,
                {
                    name: '重绘隐藏',
                    type: 'bar',
                    barGap: 0,
                    stack: 'redraw',
                    itemStyle: {
                        normal: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        },
                        emphasis: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                },
                {
                    name: '重绘',
                    type: 'bar',
                    barGap: 0,
                    stack: 'redraw',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        pageloadChart.setOption(pageloadOptions);

        pageloadChart.on('click', function (params) {
            var pageloadDetail = pageloadDetailDatas[params.dataIndex];
            $("#pageloaddetail").html(pageloadDetail);
            $("#pageloadAlert").show();
        });

        $("#closepageloadAlert").click(function () {
            $("#pageloadAlert").hide();
        });
        $("#pageloadAlert").hide();
    }

    function refreshPageload(pageloadInfos) {
        for (var i = 0; i < pageloadInfos.length; i++) {
            var pageloadInfo = pageloadInfos[i];
            pageloadOptions.xAxis[0].data.shift();
            pageloadOptions.xAxis[0].data.push(pageloadInfo.pageName);

            // series0 绘制开始时间点
            pageloadOptions.series[0].data.shift();
            pageloadOptions.series[0].data.push(pageloadInfo.drawStartTime);
            // series1 绘制结束时间点
            pageloadOptions.series[1].data.shift();
            pageloadOptions.series[1].data.push(pageloadInfo.drawEndTime - pageloadInfo.drawStartTime);
            // series2 请求开始时间点
            pageloadOptions.series[2].data.shift();
            pageloadOptions.series[2].data.push(pageloadInfo.requestStartTime);
            // series3 请求结束时间点
            pageloadOptions.series[3].data.shift();
            pageloadOptions.series[3].data.push(pageloadInfo.requestEndTime - pageloadInfo.requestStartTime);
            // series4 重绘开始时间点
            pageloadOptions.series[4].data.shift();
            pageloadOptions.series[4].data.push(pageloadInfo.redrawStartTime);
            // series5 重绘结束时间点
            pageloadOptions.series[5].data.shift();
            pageloadOptions.series[5].data.push(pageloadInfo.redrawEndTime - pageloadInfo.redrawStartTime);

            pageloadDetailDatas.shift();
            pageloadDetailDatas.push(pageloadInfo.pageloadDetail.replace(/\r?\n/g, "</br>"));
        }
        pageloadChart.setOption(pageloadOptions);
    }


    return {
        setup: setup,
        refreshPageload: refreshPageload
    }

}();
