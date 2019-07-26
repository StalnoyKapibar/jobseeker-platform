var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function showPortfolio(id) {
    $.ajax({
        url: "/api/portfolios/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            $("#PMHeadline").text(data.projectName);
            $("#PMlink").text(data.link);
            $("#PMlink").attr("href", data.link);
            $("#PMDescription").text(data.description);
        }
    });
}

function showInviteModal(id) {
    $.ajax({
        url: "/api/seekerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (seeker) {
            $('#seekerProfile-input-add').val(seeker.name);
            $('#inviteFriendModal').modal();
        },
        error: function (message) {
            console.log(message);
        }
    });
}

function inviteFriend(id, friend) {
    $.ajax({
        url: "/api/users/inviteFriend/" + id + "/" + friend,
        type: "GET",
        async: false,
        success: function () {
            alert("Ваше приглашение было отправленно");
            $('#inviteFriendModal').modal('hide');
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
            $('#inFavorite').remove();
            $('#btnFavorite').append('<button id="outFavorite" class="btn btn-primary" onclick="outFavorite(' + vacancyId + ',' + seekerProfileId + ')">Убрать из избранное' +
                '</button>');
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
            $('#outFavorite').remove();
            $('#btnFavorite').append('<button id="inFavorite" class="btn btn-primary" onclick="inFavorite(' + vacancyId + ',' + seekerProfileId + ')">В избранное' +
                '</button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function toSubscribe(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/toSubscribe?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#toSubscribe').remove();
            $('#btnSubscribe').append('<button type="button" id="unSubscribe" class="btn btn-outline-primary"' +
                ' onclick="unSubscribe(' + vacancyId + ',' + seekerProfileId + ')">Отписаться<i class="fas fa-envelope"></i></button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function unSubscribe(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/unSubscribe?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#unSubscribe').remove();
            $('#btnSubscribe').append('<button type="button" id="toSubscribe" class="btn btn-outline-primary"' +
                ' onclick="toSubscribe(' + vacancyId + ',' + seekerProfileId + ')">Подписаться<i class="fas fa-envelope"></i></button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function deleteSubscription(employerProfileId, seekerProfileId) {
    $.ajax({
        type: 'get',
        url: "/api/seekerprofiles/deleteSubscription?employerProfileId=" + employerProfileId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = '/seeker/get_subscriptions/' + seekerProfileId;
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}




