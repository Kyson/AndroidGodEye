/**
 * Created by kysonchao on 2017/10/2.
 */
'use strict';

var interval = 2000;

var refreshStatus = false;

$(document).ready(function () {
    $.fn.dataTable.ext.errMode = 'none';
    var threadTableSelector = $('#thread_table');
    threadTableSelector.DataTable({
        "ajax": "/leakMemory",
        "paging": false,
        "bSort": false,
        "scrollY": "400px",
        "scrollCollapse": true,
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

    threadTableSelector.find('tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = $('#thread_table').DataTable().row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            row.child(formatLeakStack(row.data())).show();
            tr.addClass('shown');
        }
    });

    $('#thread_start_dump').click(function () {
        startRefreshThreadTable();
    });
    $('#thread_stop_dump').click(function () {
        stopRefreshThreadTable();
    });

    startRefreshThreadTable()
});


var refreshThreadTableId;

function startRefreshThreadTable() {
    if (!refreshStatus) {
        refreshStatus = true;
        $('#thread_refresh_status').text("refreshing...");
        refreshThreadTableId = setInterval(refreshLeakTable, interval);
    }
}

function stopRefreshThreadTable() {
    if (refreshStatus) {
        refreshStatus = false;
        $('#thread_refresh_status').text("stopped.");
        clearInterval(refreshThreadTableId);
    }
}

function refreshLeakTable() {
    $('#thread_table').DataTable().ajax.reload();
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