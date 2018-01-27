/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var pageloadUtil = function () {

    var pageloadChart;
    var pageloadOptions;

    function setup(chartContainer) {
        pageloadChart = echarts.init(chartContainer, 'dark');
        var ss = [];
        for (var i = 0; i < 10; i++) {
            ss.push(createItem("", 0));
        }
        pageloadOptions = {
            tooltip: {
                type: 'axis'
            },
            color: ['#EB4334', '#4586F3', '#FBBD06', '#35AA53'],
            xAxis3D: {
                type: 'category',
                name: '123131',
                data: ['345'],
                axisLine: {
                    lineStyle: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            },
            yAxis3D: {
                type: 'category',
                name: 's2424x24d',
                data: ['3453'],
                axisLine: {
                    lineStyle: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            },
            zAxis3D: {
                type: 'value',
                name: 'd341314',
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
            series: ss
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

    function getLastArray(count, array) {
        if (array && array.length > 0) {
            return array.slice(array.length - count)
        }
        return [];
    }


    function refreshPageload(pageInfos) {
        var seriesSize = pageloadOptions.series.length;
        for (var j = 0; j < seriesSize; j++) {
            pageloadOptions.series.shift();
        }
        var last10Array = getLastArray(pageInfos, 10);
        for (var i = 0; i < seriesSize; i++) {
            var pageInfo = last10Array[i];
            //默认给个10ms
            var didLoadTime = 10;
            if(!pageInfo){
                pageloadOptions.series.push(createItem("", 0));
                continue;
            }
            //TODO KYSON IMPL
            if (pageInfo.loadTimeInfo && pageInfo.loadTimeInfo.didDrawTime) {
                didLoadTime = pageInfo.loadTimeInfo.didDrawTime;
            }
        }
        pageloadChart.setOption(pageloadOptions);
    }

    return {
        setup: setup,
        refreshPageload: refreshPageload
    }

}();
