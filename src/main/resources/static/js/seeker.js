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
            $('#seeker-input-add').val(seeker.name);
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
