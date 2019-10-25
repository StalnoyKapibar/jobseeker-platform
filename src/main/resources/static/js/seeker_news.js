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
                var date = new Date(item.news.date.toString());
                var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                var month = date.getMonth() + 1;
                month = month < 10 ? '0' + month : month;
                // tags
                let newsTags = "";
                $.each(item.news.tags, (i, tag) =>  {
                    newsTags += '<span class="badge badge-pill badge-success btnClick text-dark">' +
                        '<h7>' + tag.name + '</h7></span>'
                });
                var description;
                var status = item.news.status;
                description = item.news.description;
                let bgcolor = "";
                if (item.newsStatus == "NEW") bgcolor = "#F39C12";
                cardHTML += '' +
                    '<div class="card newsCard empCard" id="newsCard_' + item.news.id + '" style="margin-top: 10px">' +
                        '<div class="card-body" style="background-color: ' + bgcolor + ' ">' +
                            '<h4 class="card-title seekerNewsHeadLine">' + item.news.headline + '</h4>' +
                            '<p class="card-text newsDescription" id="description_' + item.news.id + '">' +
                                '<span id="newsDescription_' + item.news.id + '">' + description + '</span>' +
                            '</p>' +
                            '<p class="card-text seekerNewsDate">' +
                                'от: ' + day + '.' + month + '.' + date.getFullYear() +
                            '</p>' +
                            '<a href="/seeker/news/' + item.news.id + '" class="card-link">' +
                                "Читать полностью" +
                            '</a>' +
                            '<a href="/employer/' + item.news.author.id + '" class="card-link">' +
                                item.news.author.companyName +
                            '</a>' +
                            // tags
                            '<div class="newsTags">' + newsTags + '</div>' +
                        '</div>' +
                        // Див с классом "newsAction" сделан для примера работы функционала карточки новости
                        '<div class="card-footer newsAction">' +
                            '<div class="views" id="views_' + item.news.id + '">' +
                                '<i class="far fa-eye"></i><span></span>' + item.news.numberOfViews +
                            '</div>' +
                            '<div class="like" id="like_' + item.news.id + '">' +
                                '<span id="newsLike_' + item.news.id + '" onclick="like(' + item.news.id + ')">' +
                                '<i class="far fa-heart"></i>623</span>' +
                            '</div>' +
                            '<div class="comments" id="comments_' + item.news.id + '">' +
                                '<span id="viewComments_' + item.news.id + '" ' +
                                            'onclick="printComments(' + item.news.id + ')">' +
                                    '<i class="far fa-comments"></i>23' +
                                '</span>' +
                            '</div>' +
                        '</div>' +
                    '</div>'
                ;
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

function getFullDescription(newsId) {
    var description = $('#readMoreButton_' + newsId).val();
    $('#newsDescription_' + newsId).remove();
    $('#description_' + newsId).append('<span id="newsDescription_' + newsId + '">' + description +
        '<span><button id="readLessButton_' + newsId + '" value="' + description + '" ' +
        'onclick="getShortDescription(' + newsId + ')" class="btn btn-link">... Свернуть' +
        '</button></span></span>');
}

function getShortDescription(newsId) {
    var description = $('#readLessButton_' + newsId).val();
    $('#newsDescription_' + newsId).remove();
    $('#description_' + newsId).append('<span id="newsDescription_' + newsId + '">' +
        description.substr(0, 150) +
        '<span><button id="readMoreButton_' + newsId + '" value="' + description + '" ' +
        'onclick="getFullDescription(' + newsId + ')" class="btn btn-link">... Читать полностью' +
        '</button></span></span>');
}

//**********************************************************************************************************************
// Пример как должна работать информационная строка в карточке новости , включая счетчик просмотров , лайки и коментарии

function like(id) {
    $('#newsLike_' + id).remove();
    $('#like_' + id).append('<span id="newsLike_' + id + '" onclick="dislike(' + id + ')">' +
        '<i class="fas fa-heart"></i>624</span>');
    $('#like_' + id).attr('class', 'dislike');
}

function dislike(id) {
    $('#newsLike_' + id).remove();
    $('#like_' + id).append('<span id="newsLike_' + id + '" onclick="like(' + id + ')">' +
        '<i class="far fa-heart"></i>623</span>');
    $('#like_' + id).attr('class', 'like');
}

function printComments(id) {
    $('#newsCard_' + id).append('<div class="card-body" id="newsComments_' + id + '">' +
        '<div>asdasdasdasdasdasdasd</div><hr><div>cvxdvsdsa</div><hr><div>efcvedgchbnwjdnxiwndxjnwhdx</div></div>');
    $('#viewComments_' + id).remove();
    $('#comments_' + id).append('<span id="viewComments_' + id + '" onclick="hideComments(' + id + ')">' +
        '<i class="fas fa-comments"></i>23</span>');
    $('#comments_' + id).attr('class', 'comments_viewing');

}

function hideComments(id) {
    $('#viewComments_' + id).remove();
    $('#newsComments_' + id).remove();
    $('#comments_' + id).append('<span id="viewComments_' + id + '" onclick="printComments(' + id + ')">' +
        '<i class="far fa-comments"></i>23</span>');
    $('#comments_' + id).attr('class', 'comments');
}

//**********************************************************************************************************************

