$(document).ready(function () {
    let $header = $("meta[name='_csrf_header']").attr("content");
    let $token = $("meta[name='_csrf']").attr("content");
    let $newsId = $('#newsId').val();
    let $currentProfileId = $('#profileId').val();
    let $editForm = $('#edit-user_comment');
   let $editCommentId = $('#edit_commentId');
    let $saveBtn = $('#save-comment');
    let currentNews;
    $.ajax({
        url: "/api/comments/" + $newsId,
        type: "GET",
        success: function (data) {
            currentNews= data[0].news;
            $.each(data, function (i, comment) {
                $('#user_comments').after('<div class="m-5"><div class="card-body"><div class="row">' +
                    '<div class="col-md-2"><img src="https://image.ibb.co/jw55Ex/def_face.jpg" class="img img-rounded img-fluid mb-2 ml-4" alt="">' +
                    '<p class="text-secondary text-center">' + data[i].dateTime + '</p></div>' +
                    '<div class="col-md-10"><p class="pl-3"><strong>' + data[i].profile.name +
                    '</strong></p><div class="form-group basic-textarea"><textarea class="form-control z-depth-1 pl-3" style="background: none; border: none;" id="comment_' + data[i].id + '"  rows="3" disabled>' + data[i].text + '</textarea></div>' +
                    ' </div></div></div></div>');
                let $comments = $('textarea[id^="comment_"]');
                if ($currentProfileId == data[i].profile.id) {
                    $comments.after('<button type="button" class="float-right btn text-white btn-warning ml-3 mt-2" id="edit-comment_' + data[i].id + '" data-toggle="modal" data-target="#exampleModal"> ' +
                        '<i class="far fa-edit"></i><span> Редактировать</span></button>'
                    );
                    $comments.after(`<button type="button" class="float-right btn text-white btn-danger ml-3 mt-2" id="delete-comment_' + ${data[i].id}+'"> <i class="far fa-trash-alt"></i><span> Удалить</span></button>`);
                } else {
                    $comments.after('<button type="button" class="float-right btn text-white btn-danger ml-3 mt-2"> ' +
                        '<i class="fas fa-exclamation-triangle"></i><span>Пожаловаться</span></button>');
                }
                let $editBtn = $('#edit-comment_' + data[i].id);
                $editBtn.on('click', function (event) {
                    let commentId = data[i].id;
                    $.ajax({
                        url: "/api/comments",
                        type: "GET",
                        data:{
                            id: commentId
                        },
                        success: function (comment) {
                            $editCommentId.attr("value", comment.id);
                            $editForm.val(comment.text);
                        }
                    });
                });

            });
        }
    });
 $saveBtn.on('click', function () {
        console.log(currentNews);
     let now = new Date(Date.now());
     let formatted = now.getFullYear() + "-" + now.getMonth() + "-" + now.getDay() + " " + now.getHours() + ":" + now.getMinutes();
     let $comment = {
         'id': $editCommentId.val(),
         'text': $editForm.val(),
         'dateTime': formatted,
         'news':currentNews
     };
     $.ajax({
        //url: "/api/comments/update?id=" + $editCommentId.val() + "&text=" + $editForm.val() + "&dateTime=" + formatted,
        url: "/api/comments/update",
         type: "PUT",
        data: JSON.stringify($comment),
         dataType: "json",
         contentType: "application/json",
        beforeSend: function (request) {
             request.setRequestHeader($header, $token);
         },
         success: function () {
             location.reload();
         }
     });
 })
});