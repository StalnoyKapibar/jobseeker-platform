$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var $newsId = $('#newsId').val();
    $.ajax({
        url: "http://localhost:7070/api/comments/news",
        type: "GET",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data:{
            newsId: $newsId
        },
        success: function (data) {
            $.each(data, function (i, comment) {
                $('#user_comments').after('<div class="m-5"><div class="card-body"><div class="row">' +
                    '<div class="col-md-2"><img src="https://image.ibb.co/jw55Ex/def_face.jpg" class="img img-rounded img-fluid mb-2" alt=""><p class="text-secondary text-center">15 Minutes Ago</p></div>' +
                    '<div class="col-md-10"><p><strong>'+ comment.profile.name + '</strong></p><p>' + comment.text +'</p><p><a class="float-right btn text-white btn-danger"> <i class="fas fa-exclamation-triangle"></i> Пожаловаться</a></p>' +
                    ' </div></div></div></div>');
            });
        }

    });
});