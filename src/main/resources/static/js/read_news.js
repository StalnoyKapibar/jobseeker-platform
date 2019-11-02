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
    let totalPages;
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
                    totalPages = data.totalPages;
                    $.each(data.content, function (i, comment) {
                        $('#user_comments').after('<div class="m-5 comment-block" ' +
                            'id="comment_block_' + data.content[i].id + '"><div class="card-body"><div class="row">' +
                            '<div class="col-md-2"><img id="logo_' + data.content[i].id + '" ' +
                            'class="img img-rounded img-fluid mb-2 ml-4" alt="">' +
                            '<p class="text-secondary text-center">'
                            + datetimeFormatter(new Date(data.content[i].dateTime)) + '</p></div>' +
                            '<div class="col-md-10"><p class="pl-3"><strong>' + data.content[i].profile.name + " " +
                            data.content[i].profile.surname + '</strong></p><div class="form-group basic-textarea">' +
                            '<textarea class="form-control z-depth-1 pl-3" style="background: none; border: none;" ' +
                            'id="comment_' + data.content[i].id + '"  rows="3" disabled>' + data.content[i].text + '' +
                            '</textarea></div></div></div></div></div>');
                        let $logo = $('#logo_' + data.content[i].id);
                        $logo.attr("src", "https://image.ibb.co/jw55Ex/def_face.jpg");
                        let $comments = $('#comment_' + data.content[i].id);
                        let $commentBlock = $('#comment_block_' + data.content[i].id);
                        $commentBlock.append('<div class="btn-group  w-100 d-flex flex-row">' +
                            '<div class="ml-5 btn-group-left_' + data.content[i].id + '"></div>' +
                            '<div class="mr-5  ml-auto btn-group-right_' + data.content[i].id + '"></div></div>');
                        $('.btn-group-left_' + data.content[i].id).append('<button type="button" ' +
                            'class="btn d-none text-primary mt-2 mr-3 " id = "replies_for_comment_'
                            + data.content[i].id + '"> ' +
                            '<i class="far fa-comment-dots mr-2"></i>' +
                            '<span id="count_replies_for_comment_' + data.content[i].id + '"></span></button>');
                        let commentAuthorName = data.content[i].profile.name + " " +
                            data.content[i].profile.surname;
                        let $countForRepliesForComment = $('#count_replies_for_comment_' + data.content[i].id);
                        let repliesForComment = getAllRepliesForComment(data.content[i].id);
                        console.log(JSON.stringify(repliesForComment));
                        let $showRepliesForComment = $('#replies_for_comment_' + data.content[i].id);
                        if (repliesForComment.length != 0) {
                            $('.btn-group-left_' + data.content[i].id).children(".btn").addClass("dropdown-toggle").removeClass("d-none");
                            $countForRepliesForComment.text(repliesForComment.length);
                            $commentBlock.append('<div class="m-5 w-100 border-left" ' +
                                'id="reply_block_for_' + data.content[i].id + '"></div>');
                            $showRepliesForComment.on('click', function () {
                                if ($('#reply_block_for_' + data.content[i].id).children().length > 0) {
                                    $('#reply_block_for_' + data.content[i].id).children().remove();
                                } else {
                                    for (let j = 0; j < repliesForComment.length; j++) {
                                        $('#reply_block_for_' + data.content[i].id).append('<div  class="reply-on-comment"' +
                                            'id="reply_' + repliesForComment[j].id + '"><div class="card-body">' +
                                            '<div class="row">' +
                                            '<div class="col-md-2"><img class="img img-rounded img-fluid mb-2 ml-4" ' +
                                            'alt="" src="https://image.ibb.co/jw55Ex/def_face.jpg">' +
                                            '<p class="text-secondary text-center">'
                                            + datetimeFormatter(new Date(repliesForComment[j].dateTime)) + '</p></div>' +
                                            '<div class="col-md-10"><p class="pl-3"><strong>' +
                                            repliesForComment[j].profile.name + " " +
                                            repliesForComment[j].profile.surname + '</strong></p><div ' +
                                            'class="form-group basic-textarea">' +
                                            '<div class="form-control z-depth-1 pl-3"' +
                                            ' style="background: none; border: none;" ' +
                                            'id="reply_' + repliesForComment[j].id + '"><p><span class="text-primary">'
                                            + commentAuthorName + "," + '</span>' + " " + repliesForComment[j].text + ''
                                            + '</p></div></div></div></div></div></div>' +
                                            '<div class="btn-group  w-100 d-flex flex-row">' +
                                            '<div class="ml-5 btn-group-left-for-reply_' + repliesForComment[j].id + '">' +
                                            '</div><div class="mr-5  ml-auto btn-group-right-for-reply_' +
                                            repliesForComment[j].id + '"></div></div></div>');
                                        $('.btn-group-left-for-reply_' + repliesForComment[j].id).append(
                                            '<button type="button" ' +
                                            'class="btn d-none text-primary mt-2 mr-3" id = "replies_for_reply_'
                                            + repliesForComment[j].id + '"> ' +
                                            '<i class="far fa-comment-dots mr-2"></i>' +
                                            '<span id="count_replies_for_reply_' + repliesForComment[j].id + '">' +
                                            '</span></button>');
                                        if ($currentProfileId == repliesForComment[j].profile.id) {
                                            $('.btn-group-right-for-reply_' + repliesForComment[j].id).append(
                                                '<button type="button"' +
                                                ' class="btn text-white btn-info ml-3 mt-2"' +
                                                ' id="edit-reply_' + repliesForComment[j].id + '" data-toggle="modal" ' +
                                                'data-target="#editModal"> <i class="far fa-edit mr-2"></i>' +
                                                '<span> Редактировать</span></button>');
                                            $('.btn-group-right-for-reply_' + repliesForComment[j].id).append(
                                                '<button type="button" ' +
                                                'class="btn text-white btn-danger ml-3 mt-2" ' +
                                                'id="delete-reply_' + repliesForComment[i].id + '">' +
                                                ' <i class="far fa-trash-alt mr-2"></i><span> Удалить</span></button>');
                                            let $deleteReplyBtn = $('#delete-reply_' + repliesForComment[i].id);
                                            $deleteReplyBtn.on('click', function () {
                                                let confirmation = confirm("Вы действительно хотите удалить свой ответ?");
                                                $.ajax({
                                                    url: "/api/reply/delete?id=" + repliesForComment[j].id,
                                                    type: "DELETE",
                                                    beforeSend: function (request) {
                                                        request.setRequestHeader($header, $token);
                                                    },
                                                    success: function () {
                                                        alert("Ответ был удален.");
                                                        location.reload();
                                                    }
                                                });
                                            });
                                        } else {
                                            $('.btn-group-right-for-reply_' + repliesForComment[j].id).append('<button type="button" ' +
                                                'class="btn text-white btn-warning ml-3 mt-2" id = "report_to_reply_'
                                                + repliesForComment[j].id + '" data-toggle="modal" data-target="#reportModal"> ' +
                                                '<i class="fas fa-exclamation-triangle mr-2"></i><span>Пожаловаться</span></button>');
                                            $('.btn-group-right-for-reply_' + repliesForComment[j].id).append(
                                                '<button type="button" ' +
                                                'class="btn text-white btn-primary ml-3 mt-2" id = "reply_to_reply_'
                                                + repliesForComment[j].id + '" > ' +
                                                '<i class="fas fa-reply mr-2"></i><span>Ответить</span></button>')
                                        }
                                        let replyAuthorName = repliesForComment[j].profile.name + " " +
                                            repliesForComment[j].profile.surname;
                                        let $replyToReplyBtn = $('#reply_to_reply_' + repliesForComment[j].id);
                                        $replyToReplyBtn.on('click', function () {
                                            $(this).toggleClass("btn-primary");
                                            $(this).toggleClass("text-white");
                                            if ($(this).hasClass("btn-primary")) {
                                                $commentBlock.next().remove();
                                                $(this).children().remove();
                                                $(this).append('<i class="fas ' +
                                                    'fa-reply mr-2"></i><span>Ответить</span>');
                                            } else {
                                                $(this).children().remove();
                                                $(this).append('<i class="fas fa-times mr-2"></i><span>Отмена</span>');
                                                $commentBlock.after('<div class="mx-5" ' +
                                                    'id="write_reply_for_reply' + repliesForComment[j].id + '">' +
                                                    '<div class="card-body p-0 mx-5"><div class="row"><div class="col-md-2">' +
                                                    '<img class="img img-rounded img-fluid d-block" alt="" ' +
                                                    'src="https://image.ibb.co/jw55Ex/def_face.jpg"/></div>' +
                                                    '<div class="comment_area-body col-md-10"><div class="mt-3">' +
                                                    '<div class="form-group basic-textarea rounded-corners"><span class="text-primary"' +
                                                    ' id="reply_author_name_' + repliesForComment[j].id + '"></span>' +
                                                    ' <textarea id="reply_text_for_reply_' + repliesForComment[j].id + '" ' +
                                                    'class="form-control z-depth-1 pl-3 mt-2" style="min-height: 100px;"' +
                                                    '></textarea></div></div></div></div></div>' +
                                                    '<div class="btn-group d-flex flex-row justify-content-end"><div class="mr-5">' +
                                                    '<button type="button" class="btn btn-outline-primary' +
                                                    ' ml-3" id="send_reply_to_reply_' + repliesForComment[j].id + '">' +
                                                    '<i class="far fa-comment-dots mr-2"></i><span>Отправить</span></button></div>' +
                                                    '</div></div>');
                                                $('#reply_author_name_' + repliesForComment[j].id).text(replyAuthorName + ",");
                                                let $sendReplyToReply = $('#send_reply_to_reply_' + repliesForComment[j].id);
                                                $sendReplyToReply.on('click', function () {
                                                    $.ajax({
                                                        url: "/api/reply/insert",
                                                        type: "POST",
                                                        data: "commentId=" + data.content[i].id +
                                                            "&text=" + $('#reply_text_for_reply_' + repliesForComment[j].id).val() + "&addressId=" +  repliesForComment[j].id,
                                                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                                        beforeSend: function (request) {
                                                            request.setRequestHeader($header, $token);
                                                        },
                                                        success: function () {
                                                            location.reload();
                                                        }
                                                    });
                                                });
                                            }
                                        });
                                    }

                                }
                            });
                        }
                        if ($currentProfileId == data.content[i].profile.id) {
                            $('.btn-group-right_' + data.content[i].id).append('<button type="button"' +
                                ' class="btn text-white btn-info ml-3 mt-2"' +
                                ' id="edit-comment_' + data.content[i].id + '" data-toggle="modal" ' +
                                'data-target="#editModal"> <i class="far fa-edit mr-2"></i>' +
                                '<span> Редактировать</span></button>');
                            $('.btn-group-right_' + data.content[i].id).append('<button type="button" ' +
                                'class="btn text-white btn-danger ml-3 mt-2" ' +
                                'id="delete-comment_' + data.content[i].id + '">' +
                                ' <i class="far fa-trash-alt mr-2"></i><span> Удалить</span></button>');

                        } else {
                            $('.btn-group-right_' + data.content[i].id).append('<button type="button" ' +
                                'class="btn text-white btn-warning ml-3 mt-2" id = "report_to_comment_'
                                + data.content[i].id + '" data-toggle="modal" data-target="#reportModal"> ' +
                                '<i class="fas fa-exclamation-triangle mr-2"></i><span>Пожаловаться</span></button>');
                            $('.btn-group-right_' + data.content[i].id).append('<button type="button" ' +
                                'class="btn text-white btn-primary ml-3 mt-2" id = "reply_to_comment_'
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
                            $(this).toggleClass("btn-primary");
                            $(this).toggleClass("text-white");
                            if ($(this).hasClass("btn-primary")) {
                                $commentBlock.next().remove();
                                $(this).children().remove();
                                $(this).append('<i class="fas ' +
                                    'fa-reply mr-2"></i><span>Ответить</span>');
                            } else {
                                $(this).children().remove();
                                $(this).append('<i class="fas fa-times mr-2"></i><span>Отмена</span>');
                                $commentBlock.after('<div class="mx-5" ' +
                                    'id="write_reply_for_comment' + data.content[i].id + '">' +
                                    '<div class="card-body p-0 mx-5"><div class="row"><div class="col-md-2">' +
                                    '<img class="img img-rounded img-fluid d-block" alt="" ' +
                                    'src="https://image.ibb.co/jw55Ex/def_face.jpg"/></div>' +
                                    '<div class="comment_area-body col-md-10"><div class="mt-3">' +
                                    '<div class="form-group basic-textarea rounded-corners"><span class="text-primary"' +
                                    ' id="comment_author_name_' + data.content[i].id + '"></span>' +
                                    '<textarea id="reply_text_' + data.content[i].id + '" ' +
                                    'class="form-control z-depth-1 pl-3 mt-2" style="min-height: 100px;"' +
                                    ' contenteditable="true"></textarea></div></div></div></div></div>' +
                                    '<div class="btn-group d-flex flex-row justify-content-end"><div class="mr-5">' +
                                    '<button type="button" class="btn btn-outline-primary' +
                                    ' ml-3" id="reply_to_comment' + data.content[i].id + '">' +
                                    '<i class="far fa-comment-dots mr-2"></i><span>Отправить</span></button></div>' +
                                    '</div></div>');
                                $('#comment_author_name_' + data.content[i].id).text(commentAuthorName + ",");
                            }
                            let $sendReplyToComment = $('#reply_to_comment' + data.content[i].id);
                            $sendReplyToComment.on('click', function () {
                                $.ajax({
                                    url: "/api/reply/add",
                                    type: "POST",
                                    data: "commentId=" + data.content[i].id +
                                        "&text=" + $('#reply_text_' + data.content[i].id).val(),
                                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                    beforeSend: function (request) {
                                        request.setRequestHeader($header, $token);
                                    },
                                    success: function () {
                                        location.reload();
                                    }
                                });
                            })
                        });
                    });
                    pageNumber++;
                }
            });
            /*if (pageNumber === totalPages) {
                $('.comment-block').last().removeClass("m-5").addClass("ml-5 mt-5 mr-5").css({marginBottom: "15vh"});
            }*/
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
        let $commentText = $('#user_comment').val();
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

    function datetimeFormatter(d) {
        return d.getFullYear().toString() + "-" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0"
            + (d.getMonth() + 1).toString()) + "-" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" +
            d.getDate().toString()) + " " + (d.getHours().toString().length == 2 ? d.getHours().toString() : "0"
            + d.getHours().toString()) + ":" + ((parseInt(d.getMinutes()).toString().length == 2 ?
            (parseInt(d.getMinutes())).toString() : "0" + (parseInt(d.getMinutes())).toString()));
    }

    function getAllRepliesForComment(commentId) {
        let result;
        $.ajax({
            url: "/api/reply/" + commentId,
            type: "GET",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        return result;
    }


});