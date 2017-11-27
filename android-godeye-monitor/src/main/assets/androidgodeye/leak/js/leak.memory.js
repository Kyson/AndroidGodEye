/**
 * Created by kysonchao on 2017/10/2.
 */
'use strict';

var interval = 2000;

var refreshStatus = false;

$(document).ready(function () {
    $.fn.dataTable.ext.errMode = 'none';
    var leakMemoryTableSelector = $('#leak_table');
    leakMemoryTableSelector.DataTable({
        "ajax": "/leakMemory",
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
            {"data": "statusSummary"}
        ],
        "order": [[1, 'asc']]
    });

    leakMemoryTableSelector.find('tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = $('#leak_table').DataTable().row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            row.child(formatLeakStack(row.data())).show();
            tr.addClass('shown');
        }
    });

    $('#leak_start_leak').click(function () {
        startRefreshLeakTable();
    });
    $('#leak_stop_leak').click(function () {
        stopRefreshLeakTable();
    });

    startRefreshLeakTable()
});


var refreshLeakTableId;

function startRefreshLeakTable() {
    if (!refreshStatus) {
        refreshStatus = true;
        $('#leak_refresh_status').text("refreshing...");
        refreshLeakTableId = setInterval(refreshLeakTable, interval);
    }
}

function stopRefreshLeakTable() {
    if (refreshStatus) {
        refreshStatus = false;
        $('#leak_refresh_status').text("stopped.");
        clearInterval(refreshLeakTableId);
    }
}

function refreshLeakTable() {
    $('#leak_table').DataTable().ajax.reload();
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