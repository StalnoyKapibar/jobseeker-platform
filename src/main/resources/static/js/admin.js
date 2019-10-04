$(document).ready(function () {
    $('.adminEnableUserBtn').click(unblockUserWithProfile);
    $('.user_block_operation').click(blockUserWithProfile);
});

function unblockUserWithProfile() {
    let idUser = $(this).data("id");
    let url = '/api/users/unblock/' + idUser;

    $.get(url, null, function (data) {
        $('#alert_modal').modal('show');
        let prefContainer = 'div#userBlockId' + idUser;
        $(prefContainer + ' .user_unblock').hide();
        $(prefContainer + ' .user_block').show();
    });
}

function blockUserWithProfile() {
    let idUser = $(this).data("id");
    let period = $(this).data("period");
    let email = $(this).data("email");

    $("#jobSeekerLock").modal("show");
    $("#jobSeekerLock").data("id", idUser);
    $("#jobSeekerLock").data("period", period);
    $("#modalBodyEmail").text(email);

    if (period === 0) {
        $("#modalBodyPeriod").text("бессрочно");
    } else if (period === 1) {
        $("#modalBodyPeriod").text("1 день");
    } else if (period === 3) {
        $("#modalBodyPeriod").text("3 дня");
    } else if (period === 7) {
        $("#modalBodyPeriod").text("7 дней");
    } else if (period === 14) {
        $("#modalBodyPeriod").text("14 дней");
    }
}

$(document).on('show.bs.modal', '#jobSeekerLock', function () {
    $('#btnSuccess').click(function () {
        let idUser = $("#jobSeekerLock").data("id");
        let period = $("#jobSeekerLock").data("period");
        let url = '/api/users/block/' + idUser + '/' + period;
        $.get(url, null, function () {
            let prefContainer = 'div#userBlockId' + idUser;
            $(prefContainer + ' .user_block').hide();
            $(prefContainer + ' .user_unblock').show();
            $("#jobSeekerLock").modal("hide");
        });
    })
});