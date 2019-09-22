$(document).ready(function () {
   $('.adminEnableUserBtn').click(unblockUserWithProfile);
   $('.user_block_operation').click(blockUserWithProfile);
});

function unblockUserWithProfile(){
    let idUser = $(this).data("id");
    let url = '/api/users/unblock/'+ idUser;

    $.get(url, null, function(data){
        $('#alert_modal').modal('show');
        let prefContainer = 'div#userBlockId'+ idUser;
        $(prefContainer +' .user_unblock').hide();
        $(prefContainer +' .user_block').show();
    });
}

function blockUserWithProfile() {
    let idUser = $(this).data("id");
    let period = $(this).data("period");

    if (!confirm("Вы уверены?")) return;
    let url = '/api/users/block/'+ idUser +'/' + period;
    $.get(url, null, function(data){
        let prefContainer = 'div#userBlockId'+ idUser;
        $(prefContainer +' .user_block').hide();
        $(prefContainer +' .user_unblock').show();

    });
}
