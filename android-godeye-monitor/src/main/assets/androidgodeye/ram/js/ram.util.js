/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
var ramUtil = function () {
    var ramChart;

    function createSeriresData(ramName, ramValue) {
        return {
            name: ramName,
            y: ramValue
        }
    }

    function refreshRam(ramInfo) {
        // lowMemThresholdText = (ramInfo.lowMemThresholdKb / 1024).toFixed(1) + "m";
        // isLowMemory = ramInfo.isLowMemory;
        var datas = [];
        if (ramInfo) {
            var allocatedKb = ramInfo.totalMemKb - ramInfo.availMemKb;
            datas.push(createSeriresData("allocated", allocatedKb));
            datas.push(createSeriresData("free", ramInfo.availMemKb));
            var title = "Total:" + (ramInfo.totalMemKb / 1024).toFixed(1) + "M";
            ramChart.setTitle({
                text: title
            });
        } else {
            ramChart.setTitle({
                text: "**"
            });
        }
        ramChart.series[0].setData(datas);
    }

    function setup(chart_dom) {
        ramChart = Highcharts.chart(chart_dom, {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false,
                spacing: [0, 0, 0, 0]
            },
            title: {
                text: "Ram",
                align: 'center',
                verticalAlign: 'middle',
                y: 50
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:18px;color:' +
                        ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">' +
                        this.point.name + ":" +
                        (this.point.y / 1024).toFixed(1) + 'M,' +
                        this.point.percentage.toFixed(1) + "%"
                        + '</span></div>'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '75%']
                }
            },
            series: [{
                type: 'pie',
                name: 'ram',
                innerSize: '80%',
                data: []
            }]
        });
    }

    return {
        setup: setup,
        refreshRam: refreshRam
    }
}();