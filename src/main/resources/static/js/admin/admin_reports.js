$(document).ready(function () {
    'use strict';
    feather.replace();
    let $reports = $('#reports tbody');
    $.ajax({
        url: "/api/report/comments/",
        type: "GET",
        success: function (data) {
            console.log(data);
            $.each(data, function (i, report) {
                $reports.append(
                    '<tr><td class="text-center align-middle">' + (i + 1) + '</td>' +
                    '<td class="text-center align-middle">' + data[i].dateTime + '</td>' +
                    '<td class="text-center align-middle" style=" word-wrap: break-word;">'
                    + data[i].description + '</td>' +
                    '<td class="text-center align-middle"><button class="btn btn-primary">' +
                    '<i class="far fa-envelope-open"></i><span class="ml-2">Открыть</span></button></td>' +
                    '<td class="text-center align-middle"><button class="btn btn-danger">' +
                    '<i class="far fa-trash-alt"></i><span class="ml-2">Удалить</span></button></td>' +
                    '</tr>'
                );
            });
        }
    });
});