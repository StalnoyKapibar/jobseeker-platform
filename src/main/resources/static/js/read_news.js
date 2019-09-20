$(document).ready(function () {
    var $newsId = $('#newsId').val();
    $.ajax({
        type: 'get',
        url: "/api/news/read_news?newsId=" + $newsId,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
        }
    })
});