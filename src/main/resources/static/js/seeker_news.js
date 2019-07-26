var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function () {
    printSeekerNews();
});

$(window).scroll(function () {
    if ($(document).height() - $(window).height() === $(window).scrollTop()) {
        printSeekerNews();
    }
});

function printSeekerNews() {
    var seekerProfileId = $('#seekerProfileId').val();
    var newsPageCount = $('#scrollSeekerNewsPageCount').val();
    $.ajax({
        type: 'get',
        url: "/api/news/all_seeker_news?seekerProfileId=" + seekerProfileId + "&newsPageCount=" + newsPageCount,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            if (data.length === 0) {
                return;
            }
            var cardHTML = '';
            $.each(data, function (i, item) {
                var date = new Date(item.date.toString());
                var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                var month = date.getMonth() + 1;
                month = month < 10 ? '0' + month : month;
                cardHTML += '<div class="card" style="margin-top: 10px">' +
                    '<div class="card-body">' +
                    '<h4 class="card-title">' + item.headline + '</h4>' +
                    '<p class="card-text">' + item.description + '</p>' +
                    '<p class="card-text seekerNewsDate">' + 'от: ' + day + '.' + month + '.' + date.getFullYear() + '</p>' +
                    '<a href="/employer/' + item.author.id + '" class="card-link">' + item.author.companyName + '</a>' +
                    '</div>' +
                    '</div>';
            });
            newsPageCount++;
            $('#scrollSeekerNewsPageCount').val(newsPageCount);
            $('#seekerNewsLine').append(cardHTML);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}