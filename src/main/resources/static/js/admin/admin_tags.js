$(document).ready(function(){
    $("body").on("click",".btn-danger", function(){
        $(this).parents("tr").hide();
    });

    $("body").on("click",".btn-primary", function(){
        $(this).hide();
    });
});


function deleteTag(userId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url: "/api/tags/" + userId,
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
        type: "DELETE",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}

function changeVerifiedTag(userId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        url: "/api/tags/change_verified/" + userId,
        dataType: "json",
        type: "PUT",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data, status, jqXHR) {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}