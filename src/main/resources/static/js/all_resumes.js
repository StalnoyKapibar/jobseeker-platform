var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var page = 1;
var total_pages;
var city;

$(function () {
    getCurrentLocation(function () {
        getAllResumesByPoint(point);
    });

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
                            $('.document').ready(function () {
                                if (isAdded && $('.advice_variant').length === 0) {
                                    $('#search_advice_wrapper').append('<div class="advice_variant"> Вы уже добавили "' + value.name + '" в поиск</div>');
                                }
                            });
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

$(window).scroll(function () {
    if ($(document).height() - $(window).height() === $(window).scrollTop()) {
        if (page < total_pages) {
            getSortedResumes(point);
        } else if (page === total_pages) {
            getSortedResumes(point);
        }
    }
});

function getCurrentLocation(callback) {
    navigator.geolocation.getCurrentPosition(function (position) {
        var lat = position.coords.latitude;
        var lng = position.coords.longitude;
        point = {'latitudeY': lat, 'longitudeX': lng};
        getCityByCoords(lat, lng);
        callback(point);
    }, function () {
        point = {'latitudeY': 0, 'longitudeX': 0};
        getAllResumesByPoint(point);
    });
}

function getCityByCoords(lat, lng) {
    $.ajax({
        url: "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + $("meta[name='apiKey']").attr("content"),
        type: "GET",
        async: false,
        success: function (data) {
            for (let i = 0; i < data.results[0].address_components.length; i++) {
                for (let b = 0; b < data.results[0].address_components[i].types.length; b++) {
                    if (data.results[0].address_components[i].types[b] === "locality") { //postal_town
                        city = data.results[0].address_components[i].long_name;
                        break;
                    }
                }
            }
        }
    })
}

function getAllResumesByPoint(point) {
    getSortedResumes(point);
}


function getSortedResumes(point) {
    $.ajax ({
        url: "api/resumes/city/page/" + page + "?city=" + city,
        type: "POST",
        async: false,
        data: JSON.stringify(point),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (resumes) {
            total_pages = resumes.totalPages;
            printResumes(resumes.content);
            page++;
        }
    })
}

function printResumes(data) {
    $.each(data, function (key, value) {
        let minSalary = '';
        let resumeTags = "";
        if (value.salaryMin) {
            minSalary = '<div class="salary"><span>Зарплата от: ' + value.salaryMin + ' руб.</span></div>';
        }
        $.each(value.tags, function (i, item) {
            resumeTags += '<span class="badge badge-pill badge-success btnClick text-dark" style="white-space: pre"><h7>' + item.name + '   </h7></span>';
        });
        $('#searchList').append('<li class="list-group-item clearfix" data-toggle="modal"' +
            ' data-target="#resumeModal" onclick="showResume(\'' + value.id + '\')">' +
            '<div class="headLine"><span>' + value.headline + '</span></div>' +
            '<div class="resumeTags" style="position: absolute; left: 75%; top: 5%">' + resumeTags + '</div>' +
            '<div class="companyData"><span>Сикер: ' + value.creatorProfile + '</span><br><span>Город: ' + value.city + '</span></div>' +
            '<br>' +
            minSalary +
            '<div class="pull-right">' +
            '<span class="btn btn-outline-success btn-sm btnOnResumePage"' +
            'onclick="window.location.href =\'/seeker/' + value.creatorProfile + '\';event.stopPropagation();">На страницу сикера</span>' + '</div>' +
            '</li>');
    });
}

function showResume(id) {
    $.ajax({
        url: "/api/resumes/getbyid/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            let tags = "";
            $.each(data.tags, function (key, value) {
                tags += '<span class="badge badge-pill badge-success btnClick text-dark">' + value.name + '</span>'
            });
            let jobExp = "Предыдущий опыт работы: <br>";
            if (data.jobExperiences.length === 0) {
                jobExp += '<span>Нет опыта</span> <br>';
            } else {
                $.each(data.jobExperiences, function (key, value) {
                    jobExp += '<span>Место работы: ' + value.companyName + '</span> <br>';
                    jobExp += '<span>Должность: ' + value.position + '</span> <br>';
                    jobExp += '<span>Выполняемые задачи: ' + value.responsibilities + '</span> <br>';
                    let firstDay = new Date(value.firstWorkDay);
                    jobExp += '<span>Время работы: ' + firstDay.toLocaleDateString() + '</span> - ';
                    let lastDay = new Date(value.lastWorkDay);
                    jobExp += '<span>' + lastDay.toLocaleDateString() + '</span> <br>'
                    return key > 2;
                });
            }
            $("#VMTags").html(tags);
            $("#VMJobExp").html(jobExp);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
            let str = "Зарплата: ";
            if (data.salaryMin != null) {
                str = str + "от " + data.salaryMin + " рублей ";
            }
            if (data.salaryMax != null) {
                str = str + "до " + data.salaryMax + " рублей";
            }
            if (str === "Зарплата: ") {
                str = "Зарплата не указана";
            }
            $("#VMSalary").text(str);
        }
    });
}

function searchByTags() {
    $('#getAllResumes').remove();
    $('#searchList').remove();
    $('#searchResult').append('<ul id="searchList" class="list-group"></ul>');
    let param = [];
    $('.listTags').each(function (key, value) {
        var tagId = $(this).find('.tagIdH').val();
        var tagName = $(this).find('.tagButton').text();
        var tag = {
            'id': tagId,
            'name': tagName
        };
        param.push(tag);
    });
    let searchBox = $('#search_box').val();
    if (param.length === 0 && searchBox.length === 0) {
        $('#searchHeader').text('Задан пустой запрос');
    } else {
        let pageCount = 0;
        $.ajax({
            type: 'post',
            url: "api/resumes/search?pageCount=" + pageCount,
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
                        $('#searchHeader').text('Вакансии по тегу : ');
                        $('#searchHeader').append(param[0].name);
                    } else {
                        $('#searchHeader').text('Вакансии по тегам : ');
                        for (let i=0; i<param.length; i++) {
                            if (i===param.length -1){
                                $('#searchHeader').append(param[i].name);
                            } else {
                                $('#searchHeader').append(param[i].name + ', ');
                            }
                        }
                    }
                    printResumes(data.content);
                    total_pages = data.totalPages;
                    pageCount++;
                }
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
            }
        })
    }
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
