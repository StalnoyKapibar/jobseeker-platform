$(document).ready(function () {
    let $header = $("meta[name='_csrf_header']").attr("content");
    let $token = $("meta[name='_csrf']").attr("content");
    let $newsId = Number($('#newsId').val());
    let $currentProfileId = $('#profileId').val();
    let $editForm = $('#edit-user_comment');
    let $editCommentId = $('#edit_commentId');
    let $saveBtn = $('#save-comment');
    let $addBtn = $('#submit_comment');
    let $reportReasons = $('input[id^="reportRadios"]');
    let $sendReportBtn = $('#send_report');
    let $commentId = $('#commentId');
    $.ajax({
        url: "/api/comments/" + $newsId,
        type: "GET",
        success: function (data) {
            $.each(data, function (i, comment) {
                $('#user_comments').after('<div class="m-5"><div class="card-body"><div class="row">' +
                    '<div class="col-md-2"><img id="logo_' + data[i].id + '" ' +
                    'class="img img-rounded img-fluid mb-2 ml-4" alt="">' +
                    '<p class="text-secondary text-center">' + data[i].dateTime + '</p></div>' +
                    '<div class="col-md-10"><p class="pl-3"><strong>' + data[i].profile.name +
                    '</strong></p><div class="form-group basic-textarea">' +
                    '<textarea class="form-control z-depth-1 pl-3" style="background: none; border: none;" ' +
                    'id="comment_' + data[i].id + '"  rows="3" disabled>' + data[i].text + '</textarea></div>' +
                    ' </div></div></div></div>');
                let $logo = $('#logo_' + data[i].id);
                $logo.attr("src", "https://image.ibb.co/jw55Ex/def_face.jpg");
                let $comments = $('#comment_' + data[i].id);
                if ($currentProfileId == data[i].profile.id) {
                    $comments.after('<button type="button"' +
                        ' class="float-right btn text-white btn-info ml-3 mt-2"' +
                        ' id="edit-comment_' + data[i].id + '" data-toggle="modal" data-target="#editModal"> ' +
                        '<i class="far fa-edit"></i><span> Редактировать</span></button>'
                    );
                    $comments.after('<button type="button" ' +
                        'class="float-right btn text-white btn-danger ml-3 mt-2" ' +
                        'id="delete-comment_' + data[i].id + '">' +
                        ' <i class="far fa-trash-alt"></i><span> Удалить</span></button>');
                } else {
                    $comments.after('<button type="button" ' +
                        'class="float-right btn text-white btn-warning ml-3 mt-2" id = "report_to_comment_'
                        + data[i].id + '" data-toggle="modal" data-target="#reportModal"> ' +
                        '<i class="fas fa-exclamation-triangle"></i><span>Пожаловаться</span></button>');
                }
                let $editBtn = $('#edit-comment_' + data[i].id);
                $editBtn.on('click', function (event) {
                    let commentId = data[i].id;
                    $.ajax({
                        url: "/api/comments",
                        type: "GET",
                        data: {
                            id: commentId
                        },
                        success: function (comment) {
                            $editCommentId.attr("value", comment.id);
                            $editForm.val(comment.text);
                        }
                    });
                });
                let $deleteBtn = $('#delete-comment_' + data[i].id);
                $deleteBtn.on('click', function (event) {
                    let confirmation = confirm("Вы действительно хотите удалить свой комментарий?");
                    $.ajax({
                        url: "/api/comments/delete?id=" + data[i].id,
                        type: "DELETE",
                        beforeSend: function (request) {
                            request.setRequestHeader($header, $token);
                        },
                        success: function () {
                            alert("Комментарий был удален.");
                            location.reload();
                        }
                    });
                });
                let $reportBtn = $('#report_to_comment_' + data[i].id);
                $reportBtn.on('click', function () {
                    $sendReportBtn.addClass("d-none");
                    $reportReasons.each(function () {
                        $(this).prop('checked', false);
                    });
                    $commentId.prop('value', data[i].id);
                });
            });
        }
    });
    $saveBtn.on('click', function (e) {
        let id = Number($editCommentId.val());
        let now = new Date(Date.now());
        let formatted = now.getFullYear() + "-" + now.getMonth() + "-" + now.getDay() + " " +
            now.getHours() + ":" + now.getMinutes();
        $.ajax({
            url: "/api/comments/update",
            type: "PUT",
            data: "id=" + id + "&text=" + $editForm.val() + "&dateTime=" + formatted,
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            beforeSend: function (request) {
                request.setRequestHeader($header, $token);
            },
            success: function () {
                location.reload();
            }
        });
    });

    $addBtn.on('click', function (e) {
        let now = new Date(Date.now());
        let formatted = now.getFullYear() + "-" + now.getMonth() + "-" + now.getDay() + " " +
            now.getHours() + ":" + now.getMinutes();
        let $commentText = $('#user_comment').val();
        $.ajax({
            url: "/api/comments/insert",
            type: "POST",
            data: "newsId=" + $newsId + "&text=" + $commentText + "&dateTime=" + formatted,
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            beforeSend: function (request) {
                request.setRequestHeader($header, $token);
            },
            success: function () {
                location.reload();
            }
        });
    });

    showBtn();

    function showBtn() {
        $reportReasons.each(function () {
            $(this).change(function () {
                $sendReportBtn.removeClass("d-none");
            });
        });
    }

});