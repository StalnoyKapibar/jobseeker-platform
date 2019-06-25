function showVacancy(id) {
    $.ajax({
        url: "/api/vacancies/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            var tags = "";
            $.each(data.tags, function (key, value) {
                tags += value.name + " "
            });
            $("#VMTags").text(tags);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
            $("#VMDescription").text(data.description);

            var str = "Зарплата: ";

            if (data.salaryMin!=null ){
                str = str + "от " + data.salaryMin + " рублей ";
            }
            if (data.salaryMax!=null ){
                str = str + "до " + data.salaryMax + " рублей";
            }
            if (str == "Зарплата: "){
                str = "Зарплата не указана";
            }
            $("#VMSalary").text(str);

            if (data.remote) {
                $('#VMRemote').show();
            }else{
                $('#VMRemote').hide();
            }

        }
    });
}

var page = 1;
var total_pages;
var block = false;
var seeker_tags;
var user_id;

$(function () {
    getSeekerId();

    if (user_id!==""){ getSeekerTags();}

    getAllVacancies();

    var sub = document.getElementById("sub-point");

    sub.onclick = (function(e) {
        e.preventDefault();
        getVacBySortPoint();
    });

});

function getSeekerId() {
    $.ajax({
        url: "/api/seeker/seeker_id",
        type: "GET",
        async: false,
        success: function (data) {
            user_id = data.toString();
        }
    });
}

function getSeekerTags() {
    $.ajax({
        url: "/api/tags/seeker/" + user_id,
        type: "GET",
        success: function (data) {
            seeker_tags=data;
        }
    })
}

function getAllVacancies() {

    get_vacancies();

    $(".list-group").scroll(function () {
        if($(".list-group").scrollTop() + $(".list-group").height() >= $(".list-group").height() && !block) {
            block = true;
            if (page <= total_pages) {
                get_vacancies();}}});
}

function get_vacancies() {
    $.ajax({
        url: "api/vacancies/page/" + page,
        type:"GET",
        success:function(vacancies) {
            total_pages = vacancies.totalPages;
            append_page(vacancies);
            block = false;
            page++;
        }
    });
}

function append_page(vacancies) {
    $.each(vacancies.content, function (i, vacancy) {
        $(".list-group").append(
            "<div class=\"list-group-item clearfix\"><a href='/vacancy/"+vacancy.id+"' style='color: #333333'>" +
            vacancy.headline +
            " </a><button type='button' class='btn btn-xs btn-default' data-toggle='modal' data-target='#vacancyModal' onclick='showVacancy("+vacancy.id+")'>" +
            " <span class='pull-xs-right'><span class=\"fa fa-angle-right\" aria-hidden=\"true\"></span></span></button>"+
            "<ul id=id"+vacancy.id+" class=\"list-inline\"></ul></div>");

        if (user_id!==""){ iterate_tags(vacancy); }

    });
}

function iterate_tags(vacancy) {
    $.each(vacancy.tags, function (i, tag) {
        var vac_id = "#id" + vacancy.id;
        var v_tag = tag.name;
        var v_tag_ = v_tag.toString().split(' ').join('').toLocaleLowerCase();
        var tag_id = "#" + vacancy.id + v_tag_;
        $(vac_id).append("<li id="+vacancy.id + v_tag_+" class='list-inline-item' style='font-size: small'>"+v_tag+"</li>");
        compare_seeker_tags(v_tag_, tag_id);
    });
}

function compare_seeker_tags(v_tag_, tag_id) {
    $.each(seeker_tags, function (i,s_tag) {
        var s_tag_ = s_tag.name.toString().split(' ').join('').toLocaleLowerCase();
        if (s_tag_.localeCompare(v_tag_)==0) {
            $(tag_id).css({"background-color":"#fff2a8"})}
    })
}

function getVacBySortPoint() {
    page = 1;
    block = false;
    var lng=0;
    var lat=0;

    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            lat = position.coords.latitude;
            lng = position.coords.longitude;
        });

    } else { alert("Geolocation API не поддерживается в вашем браузере"); }

    var point = {'latitudeY': lat, 'longitudeX': lng};

    $(".list-group").html("");

    getSortedVac(point);

    $(".list-group").scroll(function () {
        if($(".list-group").scrollTop() + $(".list-group").height() >= $(".list-group").height() && !block) {
            block = true;
            if (page <= total_pages) {
                getSortedVac(point);
            }
        }
    });
}

function getSortedVac(point) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax ({
        url: "api/vacancies/point/page/" + page,
        type: "PUT",
        async: false,
        data: JSON.stringify(point),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            },
        success: function (vacancies) {
            total_pages = vacancies.totalPages;
            append_page(vacancies);
            block = false;
            page++;
        }
    })
}





