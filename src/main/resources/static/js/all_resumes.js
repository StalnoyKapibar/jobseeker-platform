var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var page = 1;
var total_pages;

$(function () {
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

function searchResults() {
    blockScroll = true;
    $("#empCardVac").remove();
    $('#searchList').remove();
    $('#searchResult').append('<ul id="searchList" class="list-group"></ul>');
    var param = [];
    $('.listTags').each(function (key, value) {
        var tagId = $(this).find('.tagIdH').val();
        var tagName = $(this).find('.tagButton').text();
        var tag = {
            'id': tagId,
            'name': tagName
        };
        param.push(tag);
    });
    var searchBox = $('#search_box').val();
    if (param.length === 0 && searchBox.length === 0) {
        $('#searchHeader').text('Задан пустой запрос');
    } else {
        var pageCount = 0;
        $('#isAVShow').val('1');
        $.ajax({
            type: 'post',
            url: "api/vacancies/search?pageCount=" + pageCount,
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            data: JSON.stringify(param),
            success: function (data) {
                if (data.content.length === 0) {
                    if (searchBox.length === 0 && param.length > 0) {
                        $('#searchHeader').text('По запросу "' + param[0].name + '" ничего не найдено');
                    } else {
                        $('#searchHeader').text('По запросу "' + searchBox + '" ничего не найдено');
                    }
                } else {
                    if (param.length === 1) {
                        $('#searchHeader').text('Резюме по тегу :');
                    } else {
                        $('#searchHeader').text('Резюме по тегам :');
                    }
                    printResumes(data.content);
                    total_pages = data.totalPages;
                    pageCount++;
                    $('#scrollPageCount').val(pageCount);
                }
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
            }
        })
    }
}

function printResumes(data) {
    $.each(data, function (key, value) {
        var favVac = '';
        var vacancyTags = '';
        var minSalary = '';
        if (value.salaryMin) {
            minSalary = '<div class="salary"><span>Зарплата от: ' + value.salaryMin + ' руб.</span></div>';
        }
        $.each(value.tags, function (i, item) {
            vacancyTags += '<span class="badge badge-pill badge-success btnClick text-dark" style="white-space: pre"><h7>' + item.name + '   </h7></span>';

        });
        $('#searchList').append('<li class="list-group-item clearfix" data-toggle="modal"' +
            ' data-target="#vacancyModal" onclick="showVacancy(\'' + value.id + '\')">' +
            '<div class="headLine"><span>' + value.headline + '</span>' + '   ' + favVac + '</div>' +
            '<div class="vacancyTags">' + vacancyTags + '</div>' +
            '<div class="companyData"><span>Компания: ' + value.creatorProfile + '</span><br><span>Город: ' + value.city + '</span></div>' +
            '<div class="vacancyDescription"><span>' + value.shortDescription + '</span></div>' +
            minSalary +
            '<div class="pull-right">' +
            '<span class="btn btn-outline-success btn-sm btnOnVacancyPage"' +
            'onclick="window.location.href =\'/vacancy/' + value.id + '\';event.stopPropagation();">На страницу вакансии</span>' + '</div>' +
            '</li>');
    });
}

function showVacancy(id) {
    $.ajax({
        url: "/api/vacancies/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            var tags = "";
            $.each(data.tags, function (key, value) {
                tags += '<span class="badge badge-pill badge-success btnClick text-dark">' + value.name + '</span>'
            });
            $("#VMTags").html(tags);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
            $("#VMDescription").html(data.description);

            var str = "Зарплата: ";

            if (data.salaryMin != null) {
                str = str + "от " + data.salaryMin + " рублей ";
            }
            if (data.salaryMax != null) {
                str = str + "до " + data.salaryMax + " рублей";
            }
            if (str == "Зарплата: ") {
                str = "Зарплата не указана";
            }
            $("#VMSalary").text(str);

            if (data.remote) {
                $('#VMRemote').show();
            } else {
                $('#VMRemote').hide();
            }

        }
    });
}

$(window).scroll(function () {
    if ($(document).height() - $(window).height() === $(window).scrollTop()) {
        if (block = true) {
            if (page < total_pages) {
                if (blockScroll == false) {
                    getSortedVac(point);
                }
            } else if (page == total_pages) {
                if (blockScroll == false) {
                    getSortedVac(point);
                }
            }
        }
        if ($('#search_box').val() === '' && $('#isAVShow').val() === '0') {
            return;
        } else {
            var param = [];
            $('.listTags').each(function (key, value) {
                var tagId = $(this).find('.tagIdH').val();
                var tagName = $(this).find('.tagButton').text();
                var tag = {
                    'id': tagId,
                    'name': tagName
                };
                param.push(tag);
            });
            var pageCount = $('#scrollPageCount').val();
            if (pageCount < total_pages - 1) {
                searchVac(pageCount, param)
            } else if (pageCount == total_pages - 1) {
                searchVac(pageCount, param);
            }
        }
    }
});

function searchVac(pageCount, param) {
    $.ajax({
        type: 'post',
        url: "api/vacancies/search?pageCount=" + pageCount,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(param),
        success: function (data) {
            printResumes(data.content);
            pageCount++;
            $('#scrollPageCount').val(pageCount);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}