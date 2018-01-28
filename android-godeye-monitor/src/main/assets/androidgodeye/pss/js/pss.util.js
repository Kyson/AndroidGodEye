/**
 * Created by kysonchao on 2017/11/29.
 */
'use strict';
var pssUtil = function () {
    var pssChart;

    function createSeriresData(pssName, pssValue) {
        return {
            name: pssName,
            y: pssValue
        }
    }

    function refreshPss(pssInfo) {
        var datas = [];
        if (pssInfo) {
            var unknownPssKb = pssInfo.totalPssKb - pssInfo.dalvikPssKb - pssInfo.nativePssKb - pssInfo.otherPssKb;
            datas.push(createSeriresData("dalvik", pssInfo.dalvikPssKb));
            datas.push(createSeriresData("native", pssInfo.nativePssKb));
            datas.push(createSeriresData("other", pssInfo.otherPssKb));
            datas.push(createSeriresData("unknown", unknownPssKb));
            var title = "Total:" + (pssInfo.totalPssKb / 1024).toFixed(2) + "M";
            pssChart.setTitle({
                text: title
            });
        } else {
            pssChart.setTitle({
                text: "**"
            });
        }
        pssChart.series[0].setData(datas);
    }

    function setup(chart_dom) {
        pssChart = Highcharts.chart(chart_dom, {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false,
                spacing: [0, 0, 0, 0]
            },
            title: {
                text: "Pss",
                align: 'center',
                verticalAlign: 'middle',
                y: 50
            },
            tooltip: {
                formatter: function () {
                    return '<div style="text-align:center"><span style="font-size:18px;color:' +
                        ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">' +
                        this.point.name + ":" +
                        (this.point.y / 1024).toFixed(2) + 'M,' +
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
                name: 'pss',
                innerSize: '80%',
                data: []
            }]
        });
    }

    return {
        setup: setup,
        refreshPss: refreshPss
    }
}();