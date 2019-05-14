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

$(document).ready(function () {
    $(".item").first().addClass("active");

    $(".fixed-rating").rating({displayOnly: true});

    $("#reviews").rating();

    $(".postReview").click(function () {
        var review = {};
        review["seekerProfiles_id"] = $("#review_evaluation").data("id-seeker");
        review["employerProfiles_id"] = $("#review_evaluation").data("id-employer");
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
        review["seekerProfiles_id"] = $("#review_evaluation").data("id-seeker");
        review["employerProfiles_id"] = $("#review_evaluation").data("id-employer");
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
});