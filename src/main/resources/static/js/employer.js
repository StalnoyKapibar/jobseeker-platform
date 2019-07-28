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
            $("#VMShortDescription").text(data.shortDescription);
            $("#VMDescription").html(data.description);
            $("#VMId").text(data.id);

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
            $('#map_collapse').attr("class", "collapsed collapse");

            var lat = data.coordinates.latitudeY;
            var lng = data.coordinates.longitudeX;
            showVacancyOnMap(lat, lng);
            var address = getAddressByCoords(lat,lng);
            $("#VMAddress").text(address);
        }
    });
}

$(document).ready(function () {
    $(".item").first().addClass("active");

    $(".fixed-rating").rating({displayOnly: true});

    $("#reviews").rating();

    $(".postReview").click(function () {
        var review = {};
        review["seekerProfiles_id"] = $("#review_evaluation").data("seeker-profile-id");
        review["employerProfiles_id"] = $("#review_evaluation").data("employer-profile-id");
        review["evaluation"] = $("#review_evaluation").val();
        review["reviews"] = $("#reviewsText").val();
        $.ajax({
            type: "POST",
            dataType: 'json',
            contentType: "application/json",
            url: "/api/review/new",
            data: JSON.stringify(review),
            beforeSend: function (request) {
                return request.setRequestHeader('X-CSRF-Token', $("input[name=_csrf]").val());
            },
            success: function () {
                $("#reviewsModal").modal('hide');
                location.reload();
            },
            error: function () {
                $("#reviewsModal").modal('hide');
            }
        });
    });

    $("#reviewsModal").on('show.bs.modal', function(){
        var review = {};
        review["seekerProfiles_id"] = $("#review_evaluation").data("seeker-profile-id");
        review["employerProfiles_id"] = $("#review_evaluation").data("employer-profile-id");
        $.ajax({
            type: "POST",
            dataType: 'json',
            contentType: "application/json",
            url: "/api/review/find",
            data: JSON.stringify(review),
            beforeSend: function (request) {
                return request.setRequestHeader('X-CSRF-Token', $("input[name=_csrf]").val());
            },
            success: function (data){
                $("#reviewsText").val(data.reviews);
                $("#review_evaluation").rating('update', data.evaluation);
                $(".postReview").html('Редактировать');
            }
        });
    });


    $(".custom-file-input").on("change", function() {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });


});

function blockVacancy(period) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    var id = $("#VMId").text();
    if (!confirm("Вы уверены?")) return;
    $.ajax({
        type: "POST",
        dataType: 'json',
        contentType: "application/json",
        url: "/api/vacancies/block/" + id,
        data: JSON.stringify(period),
        beforeSend: function (request) {
            return request.setRequestHeader(header, token);
        },
        success: function (data){
            alert(data);
        }
    });
}

function blockEmployerProfile(period) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    var id = $("#EPId").text();
    if (!confirm("Вы уверены?")) return;
    $.ajax({
        type: "POST",
        dataType: 'json',
        contentType: "application/json",
        url: "/api/employerprofiles/block/" + id,
        data: JSON.stringify(period),
        beforeSend: function (request) {
            return request.setRequestHeader(header, token);
        },
        success: function (data){
            alert(data);
        }
    });

}

function show_update_company_name_modal(id) {
    $.ajax({
        url: "/api/employerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (profile) {
            $('#update_name_company_input').val(profile.companyName);
            $('#profile_id_for_update_company_name').val(profile.id);

        },
        error: function (message) {
            console.log(message);
        }
    });

}

function update_company_name(id, company_name) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: 'post',
        url: "/api/employerprofiles/update?" +
            "id=" + id +
            "&companyname=" + company_name,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {
            $('#company_name').text(profile.companyName);
            $('#update_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })

}


function add_emploer_img(id) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");



    var form = $('#fileUploadForm')[0];
    var data = new FormData(form);
    data.append("id", id);

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/api/employerprofiles/update_image",
        data:data,
        processData: false,
        contentType: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (image) {

            var profile_img = document.getElementById('profile_img');
            profile_img.innerHTML = '';
            var img = document.createElement('img');
            img.setAttribute('class', 'img-rounded');
            img.setAttribute('alt', 'Photo');
            img.setAttribute('src', 'data:image/png;base64,'+image);
            profile_img.appendChild(img);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {

            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

function show_update_link_modal(id) {
    $.ajax({
        url: "/api/employerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (profile) {
            $('#update_company_website_input').val(profile.website);
            $('#profile_id_for_update_website').val(profile.id);
        },
        error: function (message) {
            console.log(message);
        }
    });
}

function update_website(id, website) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: 'post',
        url: "/api/employerprofiles/update?" +
            "id=" + id +
            "&website=" + website,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {

            $('#company_website').html('<a href = http://'+profile.website+'>'+profile.website+'</a>');
            $('#update_website_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}


function show_update_description_modal(id) {
    $.ajax({
        url: "/api/employerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (profile) {
            $('#update_description_textarea').val(profile.description);
            $('#profile_id_for_update_description').val(profile.id);
        },
        error: function (message) {
            console.log(message);
        }
    });

}

function update_description(id, descr) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: 'post',
        url: "/api/employerprofiles/update?" +
            "id=" + id +
            "&description=" + descr,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {

            $('#company_description').html('<span >'+profile.description+'</span>');
            $('#update_description_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })

}

