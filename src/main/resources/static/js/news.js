$(document).ready(function () {
    $('#NMDescription').summernote({
        height: 200,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,
        popover: {
            air: [
                ['color', ['color']],
                ['font', ['bold', 'underline', 'clear']]
            ]
        } // set maximum height of editor
    });
    $(".panel-heading").css('background-color', 'white');

    printEmployerNews();
});

function printEmployerNews() {
    var newsPageCount = $('#scrollEmployerNewsPageCount').val();
    $.ajax({
        type: 'get',
        url: "/api/news/?newsPageCount=" + newsPageCount,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            if (data.length === 0) {
                return;
            }
            var trHTML = '';
            $.each(data, function (i, item) {
                var headline = '';
                if (item.headline.length > 30) {
                    headline = item.headline.substr(0, 29) + ' ...';
                } else headline = item.headline;
                trHTML += '<tr class="newsTrBody">' +
                    '<td>' + headline + '</td>' +
                    '<td>' +
                    '<button id = "newsDescription-' + item.id + '" style = "text-decoration: none" type = "button"' +
                    'class = "btn btn-link" onclick = "newsDescription(' + item.id + ')"' +
                    'value="' + item.description + '" data-toggle="modal"data-target="#viewDescriptionModal">Описание </button>' +
                    '</td>' +
                    '<td>' + getFormatedDate(new Date(item.date)) + '</td>' +
                    '<td>' +
                    '<button style="width: 120px" onclick="editNews(' + item.id + ')" type="button"' +
                    'class="btn btn-primary" data-toggle="modal" data-target="#editNewsModal">Edit news </button>' +
                    '<button style="width: 120px;margin-left: 5px" onclick="deleteNews(' + item.id + ')" type="button"' +
                    'class="btn btn-primary"> Delete news </button>' +
                    '</td>' +
                    '</tr>';
            });
            newsPageCount++;
            $('#scrollEmployerNewsPageCount').val(newsPageCount);
            $('#newsBodyTable').append(trHTML);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

$(window).scroll(function () {
    if ($(document).height() - $(window).height() === $(window).scrollTop()) {
        printEmployerNews();
    }
});

var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

function deleteNews(newsId) {
    $.ajax({
        type: 'get',
        url: "/api/news/delete/" + newsId,
        success: function () {
            $('.newsTrBody').remove();
            $('#scrollEmployerNewsPageCount').val('0');
            printEmployerNews();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })

}

function editNews(newsId) {
    $.ajax({
        type: 'get',
        url: "/api/news/" + newsId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#NMId").val(data.id);
            $("#NMHeadline").val(data.headline);
            $(".note-editable").text(data.description);
            $("#NMAuthorId").val(data.author);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function editModalNews() {
    var newsId = $("#NMId").val();
    var newsHeadline = $("#NMHeadline").val();
    var newsDescription = $(".note-editable").text();

    $.ajax({
        type: 'post',
        url: "/api/news/editNews?newsId=" + newsId + "&newsHeadline=" + newsHeadline + "&newsDescription=" + newsDescription,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = '/employer/get_news';
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

//DD-MM-YYYY HH:mm
function getFormatedDate(dateObject){
    return ('0' + dateObject.getDate()).slice(-2) + '/'
        + ('0' + (dateObject.getMonth()+1)).slice(-2) + '/'
        + dateObject.getFullYear()+' '+('0' + dateObject.getHours()).slice(-2)+':'
        +('0' + dateObject.getMinutes()).slice(-2);
}

function newsDescription(newsId) {
    $("#NMViewDescription").text($('#newsDescription-' + newsId).val());
}