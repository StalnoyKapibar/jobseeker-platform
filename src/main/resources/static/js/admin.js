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
    let dropDownItem = $(this);
    let idUser = dropDownItem.data("id");
    let period = dropDownItem.data("period");
    let email = dropDownItem.data("email");

    let jsl = $("#jobSeekerLock");
    jsl.modal("show");
    jsl.data("id", idUser);
    jsl.data("period", period);

    $("#modalBodyEmail").text(email);

    switch (period) {
        case 0:
            $("#modalBodyPeriod").text("бессрочно");
            break;
        case 1:
            $("#modalBodyPeriod").text("1 день");
            break;
        case 3:
            $("#modalBodyPeriod").text("3 дня");
            break;
        case 7:
            $("#modalBodyPeriod").text("7 дней");
            break;
        case 14:
            $("#modalBodyPeriod").text("14 дней");
            break;
    }
}

$(document).on('show.bs.modal', '#jobSeekerLock', function () {
    $('#btnSuccess').click(function () {
        let jsl = $("#jobSeekerLock");
        let idUser = jsl.data("id");
        let period = jsl.data("period");
        let url = '/api/users/block/' + idUser + '/' + period;
        $.get(url, null, function () {
            let prefContainer = 'div#userBlockId' + idUser;
            $(prefContainer + ' .user_block').hide();
            $(prefContainer + ' .user_unblock').show();
            jsl.modal("hide");
        });
    })
});