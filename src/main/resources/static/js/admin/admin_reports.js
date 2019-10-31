$(document).ready(function () {
    'use strict';
    feather.replace();
    let header = $("meta[name='_csrf_header']").attr("content");
    let token = $("meta[name='_csrf']").attr("content");
    let $reports = $('#reports tbody');
    let $reportHeader = $('#report_header');
    let $reportAuthor = $('#report_author');
    let $authorProfile = $('#author_profile');
    let $reportDate = $('#report_date');
    let $reportCause = $('#report_cause');
    let $reportCommentAuthor = $('#report_comment_author');
    let $commentAuthorProfile = $('#comment_author_profile');
    let $reportCommentDate = $('#report_comment_date');
    let $reportComment = $('#report_comment');
    let $reportNewsBtn = $("#report_news");
    $.ajax({
        url: "/api/report/comments/",
        type: "GET",
        success: function (data) {
            $.each(data, function (i, report) {
                $reports.append(
                    '<tr><td class="text-center align-middle">' + (i + 1) + '</td>' +
                    '<td class="text-center align-middle">' + datetimeFormatter(new Date(data[i].dateTime)) + '</td>' +
                    '<td class="text-center align-middle" style=" word-wrap: break-word;">'
                    + data[i].description + '</td>' +
                    '<td class="text-center align-middle"><button class="btn btn-primary" data-toggle="modal" ' +
                    'data-target="#report" id="report_details_' + data[i].id + '">' +
                    '<i class="fas fa-info"></i><span class="ml-2">Подробнее</span></button></td>' +
                    '<td class="text-center align-middle"><button class="btn btn-danger" ' +
                    'id="report_delete' + data[i].id + '">' +
                    '<i class="far fa-trash-alt"></i><span class="ml-2">Отклонить</span></button></td>' +
                    '</tr>'
                );
                let $details = $('#report_details_' + data[i].id);
                $details.on('click', function () {
                    $reportHeader.text("Жалоба на комментарий №" + (i + 1));
                    $reportAuthor.prop("value", data[i].author.name);
                    $authorProfile.prop("href", "/seeker/" + data[i].author.id);
                    $reportDate.prop("value", datetimeFormatter(new Date(data[i].dateTime)));
                    $reportCause.prop("value", data[i].description);
                    $reportCommentAuthor.prop("value", data[i].comment.profile.name);
                    $commentAuthorProfile.prop("href", "/seeker/" + data[i].comment.profile.id);
                    $reportCommentDate.prop("value", datetimeFormatter(new Date(data[i].comment.dateTime)));
                    $reportComment.prop("value", data[i].comment.text);
                });
                $reportNewsBtn.on('click', function () {
                    location.href = "/seeker/news/" + data[i].comment.news.id;
                });
                let $deleteBtn = $('#report_delete' + data[i].id);
                $deleteBtn.on('click', function () {
                    let confirmation = confirm("Вы действительно хотите отклонить жалобу?");
                    $.ajax({
                        url: "/api/report/comments/delete?id=" + data[i].id,
                        type: "DELETE",
                        beforeSend: function (request) {
                            request.setRequestHeader(header, token);
                        },
                        success: function () {
                            alert("Жалоба была отклонена");
                            location.reload();
                        }
                    });
                });
            });
        }
    });
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    function datetimeFormatter(date) {
        return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear() + " " +
            date.getHours() + ":" + date.getMinutes();
    }
});