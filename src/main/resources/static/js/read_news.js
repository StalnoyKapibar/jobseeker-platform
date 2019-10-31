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
    let pageNumber = 0;
    $(window).scroll(function () {
        if ($(document).height() - $(window).height() === $(window).scrollTop()) {
            $.ajax({
                url: "/api/comments/" + $newsId,
                type: "GET",
                async: false,
                data: "pageNumber=" + pageNumber,
                success: function (data) {
                    if (data.content.length === 0) {
                        return
                    }
                    console.log(data);
                    $.each(data.content, function (i, comment) {
                        $('#user_comments').after('<div class="m-5" id="comment_block_' + data.content[i].id + '"><div class="card-body"><div class="row">' +
                            '<div class="col-md-2"><img id="logo_' + data.content[i].id + '" ' +
                            'class="img img-rounded img-fluid mb-2 ml-4" alt="">' +
                            '<p class="text-secondary text-center">' + datetimeFormatter(new Date(data.content[i].dateTime)) + '</p></div>' +
                            '<div class="col-md-10"><p class="pl-3"><strong>' + data.content[i].profile.name + " " +
                            data.content[i].profile.surname + '</strong></p><div class="form-group basic-textarea">' +
                            '<textarea class="form-control z-depth-1 pl-3" style="background: none; border: none;" ' +
                            'id="comment_' + data.content[i].id + '"  rows="3" disabled>' + data.content[i].text + '</textarea></div>' +
                            ' </div></div></div></div>');
                       /* $('#comment_block_' + data.content[i].id).after('<div class="m-5" class="d-none"><div class="card-body">' +
                            '<div class="row">' + '<div class="col-md-2"><img class="img img-rounded img-fluid d-block" ' +
                            'alt="" src="https://image.ibb.co/jw55Ex/def_face.jpg"/></div>' +
                            '<div class="comment_area-body col-md-10"><div class="mt-3">' +
                            '<div class="form-group basic-textarea rounded-corners">' +
                            '<div class="form-control z-depth-1 pl-3" style="height: 100px;" contenteditable="true">'
                            + data.content[i].profile.name + " " + data.content[i].profile.surname + "," + '</div></div>' +
                            ' </div></div></div></div></div>');*/
                        let $logo = $('#logo_' + data.content[i].id);
                        $logo.attr("src", "https://image.ibb.co/jw55Ex/def_face.jpg");
                        let $comments = $('#comment_' + data.content[i].id);
                        if ($currentProfileId == data.content[i].profile.id) {
                            $comments.after('<button type="button"' +
                                ' class="float-right btn text-white btn-info ml-3 mt-2"' +
                                ' id="edit-comment_' + data.content[i].id + '" data-toggle="modal" data-target="#editModal"> ' +
                                '<i class="far fa-edit mr-2"></i><span> Редактировать</span></button>'
                            );
                            $comments.after('<button type="button" ' +
                                'class="float-right btn text-white btn-danger ml-3 mt-2" ' +
                                'id="delete-comment_' + data.content[i].id + '">' +
                                ' <i class="far fa-trash-alt mr-2"></i><span> Удалить</span></button>');
                        } else {
                            $comments.after('<button type="button" ' +
                                'class="float-right btn text-white btn-warning ml-3 mt-2" id = "report_to_comment_'
                                + data.content[i].id + '" data-toggle="modal" data-target="#reportModal"> ' +
                                '<i class="fas fa-exclamation-triangle mr-2"></i><span>Пожаловаться</span></button>');
                            $comments.after('<button type="button" ' +
                                'class="float-right btn text-white btn-primary ml-3 mt-2" id = "reply_to_comment_'
                                + data.content[i].id + '" > ' +
                                '<i class="fas fa-reply mr-2"></i><span>Ответить</span></button>');
                        }
                        let $editBtn = $('#edit-comment_' + data.content[i].id);
                        $editBtn.on('click', function (event) {
                            let commentId = data.content[i].id;
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
                        let $deleteBtn = $('#delete-comment_' + data.content[i].id);
                        $deleteBtn.on('click', function (event) {
                            let confirmation = confirm("Вы действительно хотите удалить свой комментарий?");
                            $.ajax({
                                url: "/api/comments/delete?id=" + data.content[i].id,
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
                        let $reportBtn = $('#report_to_comment_' + data.content[i].id);
                        $reportBtn.on('click', function () {
                            $sendReportBtn.addClass("d-none");
                            $reportReasons.each(function () {
                                $(this).prop('checked', false);
                            });
                            $commentId.prop('value', data.content[i].id);
                        });
                        let $replyBtn = $('#reply_to_comment_' + data.content[i].id);
                        $replyBtn.on('click', function () {
                            $(this).toggleClass("d-none");
                            if ($(this).hasClass("d-none") == true) {

                            }
                        });
                    });
                    pageNumber++;
                }
            });
        }
    });

    $saveBtn.on('click', function (e) {
        let id = Number($editCommentId.val());
        if ($editForm.val()) {
            $.ajax({
                url: "/api/comments/update",
                type: "PUT",
                data: "id=" + id + "&text=" + $editForm.val(),
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                beforeSend: function (request) {
                    request.setRequestHeader($header, $token);
                },
                success: function () {
                    location.reload();
                }
            });
        }
    });

    $addBtn.on('click', function (e) {
        let $commentText = $('#user_comment').text();
        if ($commentText) {
            $.ajax({
                url: "/api/comments/insert",
                type: "POST",
                data: "newsId=" + $newsId + "&text=" + $commentText,
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                beforeSend: function (request) {
                    request.setRequestHeader($header, $token);
                },
                success: function () {
                    location.reload();
                }
            });
        }
    });

    $sendReportBtn.on('click', function (e) {
        let $id = Number($commentId.val());
        let $description;
        $reportReasons.each(function () {
            if ($(this).is(":checked")) {
                $description = ($(this).next()).text().trim();
            }
        });
        if ($id && $description) {
            $.ajax({
                url: "/api/report/comments/add",
                type: "POST",
                data: "id=" + $id + "&description=" + $description,
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                beforeSend: function (request) {
                    request.setRequestHeader($header, $token);
                },
                success: function () {
                    location.reload();
                }
            });
        } else {
            return;
        }

    });

    showBtn();

    function showBtn() {
        $reportReasons.each(function () {
            $(this).change(function () {
                $sendReportBtn.removeClass("d-none");
            });
        });
    }

    function datetimeFormatter(date) {
        return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear() + " " +
            date.getHours() + ":" + date.getMinutes();
    }


});