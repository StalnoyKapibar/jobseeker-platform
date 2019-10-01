var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var page = 1;
var total_pages;
var city;
var availableTags;
var var2 = [];
var availableCities;
var isFilter;
var fio;

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
        if (isFilter) {
            if (page < total_pages) {
                doFilter();
            }
        } else {
            if (page < total_pages) {
                getSortedResumes(point);
            } else if (page === total_pages) {
                getSortedResumes(point);
            }
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
            printResumes(resumes);
            page++;
        }
    })
}

function printResumes(data) {
    $.each(data.content, function (key, value) {
        let minSalary = '';
        let resumeTags = "";

        if (value.salaryMin) {
            minSalary = '<div class="salary"><span>Зарплата от: ' + value.salaryMin + ' руб.</span></div>';
        }

        $.each(value.tags, function (i, item) {
            resumeTags += '<span class="badge badge-pill badge-success btnClick text-dark" style="white-space: pre"><h7>' + item.name + '   </h7></span>';
        });

        $.each(data.seeker, function (i, item) {
            if (item.id === value.creatorProfile) {
                fio = item.fullName;
                return false;
            }
        });

        $('#searchList').append('<li class="list-group-item clearfix" data-toggle="modal"' +
            'data-target="#resumeModal" onclick="showResume(\'' + value.id + '\')">' +
            '<div class="headLine"><span>' + value.headline + '</span></div>' +
            '<div class="resumeTags" style="position: absolute; left: 75%; top: 5%">' + resumeTags + '</div>' +
            '<div class="companyData"><span>Сикер: ' + fio + '</span><br><span>Город: ' + value.city + '</span></div>' +
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

            let chat='<button onclick="openChatByResume('+data.id+')">Связаться с сикером</button>'+
                '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>';
            $("#resumeModalFooter").html(chat);

        }
    });
}
function sendMailToSeeker(resumeId) {
    let dataID = resumeId;
    $.ajax({
        url: "api/resumes/sendmail?dataID=" + dataID,
        type: "POST",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            alert('ok');
        }
    });
}

function openChatByResume(resumeId) {
    sendMailToSeeker(resumeId);
    window.location.replace("http://localhost:7070/chat/resume/"+resumeId);
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
        document.getElementById("filter").style.visibility = "hidden";
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
                    printResumes(data);
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
    $('#searchButtons').append('<span class="listTags" id="tagItem-' + id + '">' +
        '<span style="margin:0 5px 5px 0;" class="badge badge-pill badge-success tagButton" id="searchButton-' +
        id + '" onclick="deleteButton(\'' + id + '\')">' + name + '</span>' +
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

function openFilter() {
    let openHeight = "500px";
    let closeHeight = "0px";

    let filter = $("#filter");


    if (filter.height() == parseInt(closeHeight)) {
        isFilter = true;
        document.getElementById("filter").style.visibility = "visible";
        filter.height(openHeight);
    } else {
        isFilter = false;
        document.getElementById("filter").style.visibility = "hidden";
        filter.height(closeHeight);
    }
}
function doFilterInit() {
    page = 1;
    doFilter();
}
function doFilter() {

    let result;
    $('#getAllResumes').remove();
    $('#searchList').remove();
    $('#searchResult').append('<ul id="searchList" class="list-group"></ul>');

    var salFrom = $('#weightFrom').val();
    var salTo = $('#weightTo').val();
    var city = $('#City').val();
    var tagFls = $('#tagFls').val();

    let filter = {};

    if(salFrom.length !== 0) {
        filter["salFrom"] = salFrom;
    }
    if(salTo.length !== 0) {
        filter["salTo"] = salTo;
    }

    if(city.length !==0 ) {
        filter["city"] = city;
    }

    let all_span_tags = document.getElementsByClassName("span_for_tag");
    let all_span_tags_id =[];
    let index;
    for(index = 0; index < all_span_tags.length; index++){
        all_span_tags_id[index] = all_span_tags[index].id;

    }
    if(all_span_tags.length != 0) {
        if(tagFls.length !==0 ) {
            if(isExistTag(tagFls)) {
                let tmpid = findTagIdByValue(tagFls)
                all_span_tags_id[all_span_tags_id.length] = tmpid;
                filter["tagFls"] = all_span_tags_id;
            } else {
                deleteMessage();
                createMessage(tagFls);
                filter["tagFls"] = all_span_tags_id;
            }
        } else {
            filter["tagFls"] = all_span_tags_id;
        }
    } else {
        if(tagFls.length !==0 ) {
            if(isExistTag(tagFls)) {
                let tmpid = findTagIdByValue(tagFls)
                all_span_tags_id[all_span_tags_id.length] = tmpid;
                filter["tagFls"] = all_span_tags_id;
            }
        }
    }

    $.ajax({
        url: "api/resumes/getfilter?page=" + page,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        async: false,
        dataType: "json",
        data: JSON.stringify(filter),
        success: function (data) {
            printResumes(data);
            total_pages = data.totalPages;
            page++;
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    $.ajax({
        url: '/api/tags/',
        type: "GET",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        async: false,
        success: function (data) {
            availableTags = data;
            console.log(availableTags);

            let i;
            for(i = 0; i < availableTags.length; i++) {
                var2.push(availableTags[i].name);
            }
            console.log(var2);
        }
    });
});

$( function() {

    $( "#tagFls" ).autocomplete({
        source: var2

    });
} );

function doClear() {
    let spans = document.getElementsByClassName("span_for_tag");
    if(spans.length !=0) {
        for (; spans.length !=0 ; ) {
            spans[0].remove();
        }
    }
    $('#weightFrom').val('');
    $('#weightTo').val('');
    $('#City').val('');
    $('#tagFls').val('');
    doFilterInit();
}

document.addEventListener("DOMContentLoaded", function() {
    $.ajax({
        url: 'api/cities/',
        type: "GET",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        async: false,
        success: function (data) {
            availableCities = data;
            console.log(availableCities);

        }
    });
});

$( function() {

    $( "#City" ).autocomplete({
        source: unique(availableCities)

    });
} );

function unique(arr) {
    let result = [];

    for (let str of arr) {
        if (!result.includes(str)) {
            result.push(str);
        }
    }
    return result;
}

function tagToFilter() {

    let tag = $('#tagFls').val();
    if(!isExistTag(tag)) {
        deleteMessage();
        createMessage(tag);
        $('#tagFls').val('');
    } else {
        deleteMessage();
        var theDiv = document.getElementById("form_for_tags");
        let span = document.createElement("span");
        span.className = "span_for_tag";
        span.id = findTagIdByValue(tag);
        span.innerHTML = tag;

        theDiv.appendChild(span);
        $('#tagFls').val('');
    }
}

function isExistTag (tagName) {
    let i;
    for(i = 0; i < var2.length; i++) {
        if (tagName === var2[i]) {
            return true;
        }
    }
    return false;
}

function findTagIdByValue(tagName) {
    let i;
    for(i = 0; i < availableTags.length; i++) {
        if (tagName === availableTags[i].name) {
            return availableTags[i].id;
        }
    }
    return false;
}

function createMessage(message) {
    let theDiv = document.getElementById("wrong_message");
    let mg = document.createElement("p");
    mg.className = "wrong_message";
    mg.innerHTML = "К сожалению по запросу " + message + " ничего не найдено";
    theDiv.appendChild(mg);
}

function deleteMessage() {
    let message = document.getElementsByClassName("wrong_message");
    if(message.length !=0) {
        message[0].remove();
    }
}
