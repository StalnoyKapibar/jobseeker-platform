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
            location.href = '/admin/tags';
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
            location.href = '/admin/tags';
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}