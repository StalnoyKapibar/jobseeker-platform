function showVacancy(id) {
    $("a#VMedit_butt").attr("href","/edit_vacancy/"+id)
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
            var address = getAddressByCoords(lat, lng);
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

    $("#reviewsModal").on('show.bs.modal', function () {
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
            success: function (data) {
                $("#reviewsText").val(data.reviews);
                $("#review_evaluation").rating('update', data.evaluation);
                $(".postReview").html('Редактировать');
            }
        });
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
        success: function (data) {
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
        success: function (data) {
            alert(data);
        }
    });
}
