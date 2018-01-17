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
        "ajax": "/thread",
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
            {"data": "id"},
            {"data": "name"},
            {"data": "state"},
            {"data": "deadlock"},
            {"data": "priority"},
            {"data": "deamon"},
            {"data": "isAlive"},
            {"data": "isInterrupted"}
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
            row.child(formatStackTraces(row.data())).show();
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
        refreshThreadTableId = setInterval(refreshThreadTable, interval);
    }
}

function stopRefreshThreadTable() {
    if (refreshStatus) {
        refreshStatus = false;
        $('#thread_refresh_status').text("stopped.");
        clearInterval(refreshThreadTableId);
    }
}

function refreshThreadTable() {
    $('#thread_table').DataTable().ajax.reload();
}


function formatStackTraces(d) {
    var stacktraces = "";
    for (var j = 0; j < d.stackTraceElements.length; j++) {
        var stacktrace = d.stackTraceElements[j];
        stacktraces += stacktrace + "</br>"
    }
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr><td>' + stacktraces + '</td></tr>' +
        '</table>';
}