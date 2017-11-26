/**
 * Created by kysonchao on 2017/10/2.
 */
'use strict';

var REFRESH_INTERVAL = 2000;

var refreshStatus = false;

$(document).ready(function () {
    $.fn.dataTable.ext.errMode = 'none';
    var leakMemoryTableSelector = $('#leakMemoryTable');
    leakMemoryTableSelector.DataTable({
        "ajax": "leakMemory",
        "paging": false,
        "bSort": false,
        "columns": [
            {
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            {"data": "referenceKey"},
            {"data": "leakTime"},
            {"data": "leakObjectName"},
            {"data": "status"}
        ],
        "order": [[1, 'asc']]
    });

    leakMemoryTableSelector.find('tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = $('#leakMemoryTable').DataTable().row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            row.child(formatLeakStack(row.data())).show();
            tr.addClass('shown');
        }
    });

    $('#startLeakBtn').click(function () {
        startRefreshLeakTable();
    });
    $('#stopLeakBtn').click(function () {
        stopRefreshLeakTable();
    });

    startRefreshLeakTable()
});


var refreshLeakTableId;

function startRefreshLeakTable() {
    if (!refreshStatus) {
        refreshStatus = true;
        $('#leakRefreshStatus').text("refreshing...");
        refreshLeakTableId = setInterval(refreshLeakTable, REFRESH_INTERVAL);
    }
}

function stopRefreshLeakTable() {
    if (refreshStatus) {
        refreshStatus = false;
        $('#leakRefreshStatus').text("stopped.");
        clearInterval(refreshLeakTableId);
    }
}

function refreshLeakTable() {
    $('#leakMemoryTable').DataTable().ajax.reload();
}


function formatLeakStack(d) {
    var leakStack = "";
    for (var j = 0; j < d.pathToGcRoot.length; j++) {
        var path = d.pathToGcRoot[j];
        leakStack += path + "</br>"
    }
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr><td>' + leakStack + '</td></tr>' +
        '</table>';
}

// setInterval(refreshMemoryLeak, REFRESH_INTERVAL);
// setInterval(refreshMemoryLeakStatus, REFRESH_INTERVAL);
// function refreshMemoryLeakStatus() {
//     $.ajax({
//         url: "leakMemoryStatus",
//         success: function (result) {
//             result = JSON.parse(result);
//             if (result.code === 1) {
//                 var status = result.data;
//                 $("#leakmemory_status").text(status);
//                 $("#leakmemory_status").css("color", "red");
//             } else {
//                 $("#leakmemory_status").text("泄漏检测中...");
//                 $("#leakmemory_status").css("color", "white");
//             }
//         },
//         error: function () {
//             $("#leakmemory_status").text("泄漏检测中...");
//             $("#leakmemory_status").css("color", "white");
//         }
//     });
// }
//
//
// function refreshMemoryLeak() {
//     checkMemoryLeak(drawMemoryLeakList, doNothing);
// }
//
//
// function checkMemoryLeak(successCallback, failCallback) {
//     $.ajax({
//         url: "leakMemory",
//         success: function (result) {
//             result = JSON.parse(result);
//             if (result.code === 1) {
//                 successCallback(result.data);
//             } else {
//                 failCallback();
//             }
//         },
//         error: function () {
//             failCallback();
//         }
//     });
// }
//
// function doNothing() {
//
// }
//
// function drawMemoryLeakList(leakMemoryItems) {
//     for (var i = 0; i < leakMemoryItems.length; i++) {
//         var leakMemoryItem = leakMemoryItems[i];
//         var timenow = (new Date()).toLocaleTimeString();
//         var title = "[" + timenow + "] " + leakMemoryItem.leakObjectName;
//         var contentText;
//         for (var j = 0; j < leakMemoryItem.pathToGcRoot.length; j++) {
//             var path = leakMemoryItem.pathToGcRoot[j];
//             contentText += path + "</br>"
//         }
//         addItem(title, contentText);
//     }
// }
//
// function addItem(title, contentText) {
//     $("#leakmemory_list").prepend("<a href='##' class='list-group-item'><h6 class='list-group-item-heading'>" + title + "</h6><p class='list-group-item-text'>" + contentText + "</p></a>");
// }