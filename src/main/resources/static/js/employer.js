function showVacancy(id) {
    $("a#VMedit_butt").attr("href", "/edit_vacancy/" + id)
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
    /* $(".item").first().addClass("active");

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
     });*/
    /*

        $("#reviewsModal").on('show.bs.modal', function () {
            var review = {};
            review["seekerProfiles_id"] = $(this).find("input[name='seekerProfileId']").val();
            review["employerProfiles_id"] = $("#employerProfileId").val();
            $.ajax({
                type: "POST",
                dataType: 'json',
                contentType: "application/json",
                url: "/api/review/find",
                data: JSON.stringify(review),
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                success: function (data) {
                    $("#reviewsText").val(data.reviews);
                    $("#review_evaluation").rating('update', data.evaluation);
                    $(".postReview").html('Редактировать');
                }
            });
        });
    */

    var employerProfileId = $('#employerProfileId').val();
    var seekerProfileId = $('#seekerProfileId').val();

    $.ajax({
        type: 'get',
        url: "/api/review/get_all?employerProfileId=" + employerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $.each(data, function (i, item) {
                var ratingStars = '';
                for (var j = 1; j <= item.evaluation; j++) {
                    ratingStars += '<i class="fas fa-star"></i>';
                }
                if (item.evaluation < 5) {
                    for (var j = 1; j <= 5 - item.evaluation; j++) {
                        ratingStars += '<i class="far fa-star"></i>';
                    }
                }
                $('#ratingReview_' + item.id).append(ratingStars);
            });
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });

    getReviewVotes(seekerProfileId, employerProfileId);
    reviewEditBasedOnVotes();
});

function sortByNestedText(parent, childSelector, keySelector) {
    let items = parent.children(childSelector).sort(function (a, b) {
        let vA = $(keySelector, a).text();
        let vB = $(keySelector, b).text();
        return (vA > vB) ? -1 : (vA < vB) ? 1 : 0;
    });
    parent.append(items);
}

function sortByRating() {
    let parent = $('#reviews');
    let childSelector = "div";
    let items = parent.children(childSelector).sort(function (a, b) {
        let vA= a.getElementsByClassName("fas fa-star").length;
        let vB= b.getElementsByClassName("fas fa-star").length;
        return (vA > vB) ? -1 : (vA < vB) ? 1 : 0;
    });
    parent.append(items);
}

function sortByLikes(){
    sortByNestedText($('#reviews'), "div", "span.likes");
}

function sortByDislikes(){
    sortByNestedText($('#reviews'), "div", "span.dislikes");
}

function hideNegative() {
    let parent = $('#reviews');
    let childSelector = "div";
    let items = parent.children(childSelector);

    for (let i=0; i<items.length; i++) {
        items[i].style.display = 'block';
        let rating = items[i].getElementsByClassName("fas fa-star").length;
        if (rating <= 2) {
            items[i].style.display = 'none';
        }
    }
}

function hidePositive() {
    let parent = $('#reviews');
    let childSelector = "div";
    let items = parent.children(childSelector);

    for (let i=0; i<items.length; i++) {
        items[i].style.display = 'block';
        let rating = items[i].getElementsByClassName("fas fa-star").length;
        if (rating >= 3) {
            items[i].style.display = 'none';
        }
    }
}

function showAll() {
    let parent = $('#reviews');
    let childSelector = "div";
    let items = parent.children(childSelector);

    for (let i=0; i<items.length; i++) {
        items[i].style.display = 'block';
    }
}

function reviewEditBasedOnVotes() {
    var x = document.getElementsByClassName("editCardReview");
    for (i = 0; i < x.length; i++) {
        let blockId = x[i].id;
        blockId = blockId.substring(blockId.indexOf('_') + 1);
        let likes = parseInt(document.getElementById('reviewLikeCount_' + blockId).innerHTML);
        let dislikes = parseInt(document.getElementById('reviewDislikeCount_' + blockId).innerHTML);
        let sum = likes + dislikes;
        if (sum >= 5) {
            x[i].style.display = "none";
        } else {
            x[i].style.display = "block"
        }
    }
}

function getReviewVotes(seekerProfileId, employerProfileId) {
    $.ajax({
        type: 'get',
        url: "/api/reviewVote/get_all?seekerProfileId=" + seekerProfileId + "&employerProfileId=" + employerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $.each(data, function (i, item) {
                if (item.like === true) {
                    $('#reviewLike_' + item.reviewId).addClass('voted');
                }
                if (item.dislike === true) {
                    $('#reviewDislike_' + item.reviewId).addClass('voted');
                }

            });
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}

var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

function addModalReview() {
    var reviewDescription = $('#RMDescription').val();
    var date = new Date();
    var evaluation = $('#RMRatingValue').val();
    var seekerProfileId = $('#seekerProfileId').val();
    var employerProfileId = $('#employerProfileId').val();

    var review = {
        'reviews': reviewDescription,
        'dateReviews': date,
        'evaluation': evaluation
    };

    $.ajax({
        type: 'post',
        url: "/api/review/add?seekerProfileId=" + seekerProfileId + "&employerProfileId=" + employerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(review),
        success: function () {
            location.href = '/employer/' + employerProfileId
        },
        error: function () {
            $("#addNewReview").modal('hide').fadeOut(350);
            alert('Вы уже оставляли отзыв');
        }
    })
}

function editModalReview() {
    var reviewId = $('#RMEditId').val();
    var reviewDescription = $('#RMEditDescription').val();
    var date = $('#RMEditDate').val();
    var evaluation = $('#RMEditRatingValue').val();
    var employerProfileId = $('#employerProfileId').val();
    var seekerProfileId = $('#seekerProfileId').val();
    var like = $('#reviewLikeCount_' + reviewId).text();
    var dislike = $('#reviewDislikeCount_' + reviewId).text();

    var review = {
        'id': reviewId,
        'reviews': reviewDescription,
        'dateReviews': date,
        'evaluation': evaluation,
        'like': like,
        'dislike': dislike
    };

    $.ajax({
        type: 'post',
        url: "/api/review/update?seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(review),
        success: function () {
            location.href = '/employer/' + employerProfileId
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function getNewReviewRating(rating) {
    $('.RMStars').remove();
    var ratingStars = '';
    var count = 1;
    for (var j = 1; j <= rating; j++) {
        ratingStars += '<i title="' + j + '" style="margin-right: 4px" id="' + j + '" class="fas fa-star RMStars" onclick="getNewReviewRating(' + j + ')"></i>';
        count++;
    }
    if (rating < 5) {
        for (var j = 1; j <= 5 - rating; j++) {
            ratingStars += '<i title="' + count + '" style="margin-right: 4px" id="' + count + '" class="far fa-star RMStars" onclick="getNewReviewRating(' + count + ')"></i>';
            count++;
        }
    }
    $('#RMRating').append(ratingStars);
    $('#RMRatingValue').val(rating);
}

function getNewEditReviewRating(rating) {
    $('.RMEditStars').remove();
    var ratingStars = '';
    var count = 1;
    for (var j = 1; j <= rating; j++) {
        ratingStars += '<i title="' + j + '" style="margin-right: 4px" class="fas fa-star RMEditStars" onclick="getNewEditReviewRating(' + j + ')"></i>';
        count++;
    }
    if (rating < 5) {
        for (var j = 1; j <= 5 - rating; j++) {
            ratingStars += '<i title="' + count + '" style="margin-right: 4px" class="far fa-star RMEditStars" onclick="getNewEditReviewRating(' + count + ')"></i>';
            count++;
        }
    }
    $('#RMEditRating').append(ratingStars);
    $('#RMEditRatingValue').val(rating);
}

function resetRating() {
    $('.RMStars').remove();
    var ratingStars = '';
    for (var j = 1; j <= 5; j++) {
        ratingStars += '<i title="' + j + '" style="margin-right: 4px" id="' + j + '" class="far fa-star RMStars" onclick="getNewReviewRating(' + j + ')"></i>';
    }
    $('#RMRating').append(ratingStars);
    $('#RMRatingValue').val('0');
}

function resetEditRating() {
    $('.RMEditStars').remove();
    var ratingStars = '';
    for (var j = 1; j <= 5; j++) {
        ratingStars += '<i title="' + j + '" style="margin-right: 4px" class="far fa-star RMEditStars" onclick="getNewEditReviewRating(' + j + ')"></i>';
    }
    $('#RMEditRating').append(ratingStars);
    $('#RMEditRatingValue').val('0');
}

function showReview(reviewId) {
    $.ajax({
        type: 'get',
        url: "/api/review/get?reviewId=" + reviewId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#reviewsText").text(data.reviews);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function showEditReview(reviewId) {
    $.ajax({
        type: 'get',
        url: "/api/review/get?reviewId=" + reviewId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#RMEditId').val(data.id);
            $('#RMEditDate').val(data.dateReviews);
            $("#RMEditDescription").text(data.reviews);
            $('.RMEditStars').remove();
            var ratingStars = '';
            var count = 1;
            for (var j = 1; j <= data.evaluation; j++) {
                ratingStars += '<i title="' + j + '" style="margin-right: 4px" class="fas fa-star RMEditStars" onclick="getNewEditReviewRating(' + j + ')"></i>';
                count++;
            }
            if (data.evaluation < 5) {
                for (var j = 1; j <= 5 - data.evaluation; j++) {
                    ratingStars += '<i title="' + count + '" style="margin-right: 4px" class="far fa-star RMEditStars" onclick="getNewEditReviewRating(' + count + ')"></i>';
                    count++;
                }
            }
            $('#RMEditRating').append(ratingStars);
            $('#RMEditRatingValue').val(data.evaluation);
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function deleteReview() {
    var reviewId = $('#RMEditId').val();
    var employerProfileId = $('#employerProfileId').val();

    $.ajax({
        type: 'get',
        url: "/api/review/delete?reviewId=" + reviewId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = '/employer/' + employerProfileId
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}


function toLike(reviewId) {
    var seekerProfileId = $('#seekerProfileId').val();
    var employerProfileId = $('#employerProfileId').val();

    if ($('#reviewLike_' + reviewId).hasClass('voted')) {
        removeLike(reviewId, seekerProfileId);
    } else if ($('#reviewDislike_' + reviewId).hasClass('voted')) {
        updateLike(reviewId, seekerProfileId);
    } else {
        addLike(reviewId, seekerProfileId, employerProfileId);
    }
}

function removeLike(reviewId, seekerProfileId) {
    $.ajax({
        type: 'get',
        url: "/api/reviewVote/delete_like?reviewId=" + reviewId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#reviewLike_' + reviewId).removeClass('voted');
            $('#reviewLikeCount_' + reviewId).text(data.like);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function updateLike(reviewId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/reviewVote/update_like?reviewId=" + reviewId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#reviewDislike_' + reviewId).removeClass('voted');
            $('#reviewLike_' + reviewId).addClass('voted');
            $('#reviewLikeCount_' + reviewId).text(data.like);
            $('#reviewDislikeCount_' + reviewId).text(data.dislike);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function addLike(reviewId, seekerProfileId, employerProfileId) {
    var reviewVote = {
        'reviewId': reviewId,
        'seekerProfileId': seekerProfileId,
        'employerProfileId': employerProfileId,
        'isLike': true,
        'isDislike': false
    };
    $.ajax({
        type: 'post',
        url: "/api/reviewVote/add_like",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(reviewVote),
        success: function (data) {
            $('#reviewLike_' + reviewId).addClass('voted');
            $('#reviewLikeCount_' + reviewId).text(data.like);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function toDislike(reviewId) {
    var seekerProfileId = $('#seekerProfileId').val();
    var employerProfileId = $('#employerProfileId').val();

    if ($('#reviewDislike_' + reviewId).hasClass('voted')) {
        removeDislike(reviewId, seekerProfileId);
    } else if ($('#reviewLike_' + reviewId).hasClass('voted')) {
        updateDislike(reviewId, seekerProfileId);
    } else {
        addDislike(reviewId, seekerProfileId, employerProfileId);
    }
}

function removeDislike(reviewId, seekerProfileId) {
    $.ajax({
        type: 'get',
        url: "/api/reviewVote/delete_dislike?reviewId=" + reviewId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#reviewDislike_' + reviewId).removeClass('voted');
            $('#reviewDislikeCount_' + reviewId).text(data.dislike);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function updateDislike(reviewId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/reviewVote/update_dislike?reviewId=" + reviewId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#reviewLike_' + reviewId).removeClass('voted');
            $('#reviewDislike_' + reviewId).addClass('voted');
            $('#reviewLikeCount_' + reviewId).text(data.like);
            $('#reviewDislikeCount_' + reviewId).text(data.dislike);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function addDislike(reviewId, seekerProfileId, employerProfileId) {
    var reviewVote = {
        'reviewId': reviewId,
        'seekerProfileId': seekerProfileId,
        'employerProfileId': employerProfileId,
        'isLike': false,
        'isDislike': true
    };
    $.ajax({
        type: 'post',
        url: "/api/reviewVote/add_dislike",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(reviewVote),
        success: function (data) {
            $('#reviewDislike_' + reviewId).addClass('voted');
            $('#reviewDislikeCount_' + reviewId).text(data.dislike);
            reviewEditBasedOnVotes();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function inFavorite(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/inFavoriteVacancy?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#inFavorite' + vacancyId).remove();
            $('#stars-' + vacancyId).append('<i id="outFavorite' + vacancyId + '" class="fas fa-star" onclick="outFavorite(' + vacancyId + ',' + seekerProfileId + ');event.stopPropagation();" title="убрать из избранных"></i>');

        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function outFavorite(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/outFavoriteVacancy?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#outFavorite' + vacancyId).remove();
            $('#stars-' + vacancyId).append('<i id="inFavorite' + vacancyId + '" class="far fa-star" onclick="inFavorite(' + vacancyId + ',' + seekerProfileId + ');event.stopPropagation();" title="в избранное"></i>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function blockVacancy(period) {
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
