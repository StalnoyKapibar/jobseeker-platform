var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var page=1;
var total_pages;

$(function () {
    searchResults();
    $('#search_box').bind("input keyup click", function () {
        if (this.value == '') {
            $("#search_advice_wrapper").remove();
            $(".search_area").append('<div id="search_advice_wrapper"></div>')
        }
        if (this.value.length > 0) {
            var param = this.value;
            $.ajax({
                type: 'post',
                url: "api/tags/search",
                contentType: 'application/json; charset=utf-8',
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                data: JSON.stringify(param),
                success: function (data) {
                    $("#search_advice_wrapper").html("").show();
                    if (data.length == 0) {
                        $('#search_advice_wrapper').append('<div class="advice_variant"> По запросу "' + param + '" ничего не найдено</div>');
                    } else {
                        $.each(data, function (key, value) {
                            var isAdded = false;
                            $('.listTags').each(function () {
                                var tagName = $(this).find('.tagButton').text();
                                if (value.name === tagName) {
                                    isAdded = true;
                                }
                            });
                            if (!isAdded) {
                                $('#search_advice_wrapper').append('<div class="advice_variant" onclick="addTag(\'' + value.id + '\',\'' + value.name + '\')">' + value.name + '</div>');
                            }
                        });
                        if ($('.advice_variant').length===0){
                            $('#search_advice_wrapper').append('<div class="advice_variant"> По запросу "' + param + '" ничего не найдено</div>');
                        }
                    }
                },
                error: function (error) {
                    console.log(error);
                    alert(error.toString());
                }
            })
        }
    });

    $('html').on('click', function () {
        $('#search_advice_wrapper').hide();
    });
});

$(window).scroll(function () {
    if ($(document).height() - $(window).height() === $(window).scrollTop()) {
        if (page < total_pages) {
            searchResults();
        } else if (page == total_pages) {
            searchResults();
        }
    }
});

function searchResults() {
    $('#searchResult').append('<ul id="searchList" class="list-group"></ul>');
    $.ajax ({
        url: "api/resumes/"+page,
        type: "GET",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            total_pages=data.totalPages;
            $.each(data.content, function (key, value) {
                $('#searchList').append('<li class="list-group-item clearfix" ' +
                    '<div class="headLine"><span>' + value.id + '</span>'+
                    '<div class="vacancyDescription"><span>' + value.place + '</span></div>' +
                    '<div class="pull-right">' +
                    '<span class="btn btn-outline-success btn-sm btnOnVacancyPage"' +
                    'onclick="window.location.href =\'/seeker/' + value.creatorProfile + '\'">На страницу сикера</span>'+'</div>' +
                    '</li>');
            });
            page++;
        }
    });
}

function addTag(id, name) {
    $('#searchButtons').append('<span class="listTags" id="tagItem-' + id + '"><span style="margin:0 5px 5px 0;" class="badge badge-pill badge-success tagButton" id="searchButton-' + id + '" onclick="deleteButton(\'' + id + '\')">' + name + '</span>' +
        '<input class="tagIdH" id="tagId-' + id + '" type="hidden" value="' + id + '"></span>');
    $('#search_box').val('');
    $('#search_advice_wrapper').fadeOut(350).html('');
    if ($('.listTags').length >= 5) {
        $('#search_box').attr('readonly', true);
    }
}

function deleteButton(id) {
    $('#tagItem-' + id).remove();
    if ($('.listTags').length < 5) {
        $('#search_box').attr('readonly', false);
    }
}
