/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var pageloadUtil = function () {

    var pageloadChart;
    var pageloadOptions;

    function setup(chartContainer) {
        pageloadChart = echarts.init(chartContainer, 'dark');
        pageloadOptions = {
            tooltip: {
                type: 'axis'
            },
            color: ['#EB4334', '#4586F3', '#FBBD06', '#35AA53'],
            xAxis3D: {
                type: 'category',
                name: '',
                data: [''],
                axisLine: {
                    lineStyle: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            },
            yAxis3D: {
                type: 'category',
                name: '',
                data: [''],
                axisLine: {
                    lineStyle: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            },
            zAxis3D: {
                type: 'value',
                name: '',
                axisLine: {
                    lineStyle: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            },
            grid3D: {
                boxWidth: 100,
                boxDepth: 100,
                axisPointer: {
                    show: false
                },
                light: {
                    main: {
                        intensity: 1.2
                    },
                    ambient: {
                        intensity: 0.3
                    }
                }
            },
            series: []
        };
        pageloadChart.setOption(pageloadOptions);
    }


    function createItem(pageName, drawTime) {
        return {
            type: 'bar3D',
            stack: 'activity',
            name: pageName,
            barSize: 80,
            data: [0, 0, drawTime],
            label: {
                show: false,
                textStyle: {
                    fontSize: 16,
                    borderWidth: 1
                }
            },
            itemStyle: {
                opacity: 0.4
            },
            emphasis: {
                label: {
                    textStyle: {
                        fontSize: 20,
                        color: '#900'
                    }
                }
            }
        }
    }


    function refreshPageload(pageInfos) {
        pageloadOptions.series = [];
        for (var i = 0; i < pageInfos.length; i++) {
            var pageInfo = pageInfos[i];
            pageloadOptions.series.push(createItem(pageInfo.pagename, pageInfo.didloadTime));
        }
        pageloadChart.setOption(pageloadOptions);
    }

    return {
        setup: setup,
        refreshPageload: refreshPageload
    }

}();
