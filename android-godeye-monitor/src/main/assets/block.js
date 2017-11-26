/**
 * Created by kysonchao on 2017/9/4.
 */
'use strict';
var blockUtil = function () {

    var blockChart;
    var blockOptions;
    var blockDetailInfos;

    function setup(chartContainer) {
        blockChart = echarts.init(chartContainer, 'dark');
        blockDetailInfos = ["", "", "", "", "", "", "", "", "", "", "", "", "", "", ""];
        blockOptions = {
            title: {
                text: 'Block',
                left: "center",
                top: '3%'
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
                left: '3%',
                right: '4%',
                bottom: '3%',
                top: '15%',
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
                    name: "卡顿(ms)",
                    nameLocation: 'middle',
                    nameRotate: 90,
                    nameGap: 35
                }
            ],
            series: [
                {
                    name: 'block',
                    type: 'bar',
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                }
            ]
        };
        blockChart.setOption(blockOptions);
        blockChart.on('click', function (params) {
            var blockDetail = blockDetailInfos[params.dataIndex];
            if (blockDetail) {
                $("#blockdetail").html(jsonFormat.syntaxHighlight(JSON.parse(blockDetail)));
                $("#blockDetailAlert").show();
            } else {
                $("#blockdetail").html("无卡顿详情");
                $("#blockDetailAlert").show();
            }
        });

        $("#closeBlockDetailAlert").click(function () {
            $("#blockDetailAlert").hide();
        });
        $("#blockDetailAlert").hide();
    }

    function refreshBlock(blockInfos) {
        for (var i = 0; i < blockInfos.length; i++) {
            var blockInfo = blockInfos[i];
            var axisData = (new Date()).toLocaleTimeString();
            blockOptions.xAxis[0].data.shift();
            blockOptions.xAxis[0].data.push(axisData);
            blockOptions.series[0].data.shift();
            blockOptions.series[0].data.push(blockInfo.blockTime);

            blockDetailInfos.shift();
            blockDetailInfos.push(blockInfo.blockBaseinfo);
        }
        blockChart.setOption(blockOptions);
    }


    return {
        setup: setup,
        refreshBlock: refreshBlock
    }

}();
