$(document).ready(function () {
    bootstrapValidate('#user_email', 'email:Введите корректный Email-адрес');
})

function sendEmail() {
    $.ajax({
        url: '/api/users/recovery/' + $('#user_email').val(),
        success: function (result) {
            if (result) {
                $('#success_message').slideDown({opacity: "show"}, "slow");
            } else {
                $('#token_exists_message').slideDown({opacity: "show"}, "slow");
            }
        },
        error: function (error) {
            console.log(error);
            alert(error);
        }
    })
}

function validityEmailAndSend() {
    $('#ups_message').hide();
    $('#token_exists_message').hide();
    $.ajax({
        url: '/api/users/email/'
            + $('#user_email').val(),
        success: function (result) {
            if (result) {
                sendEmail();
            } else {
                $('#ups_message').slideDown({opacity: "show"}, "slow");
            }
        }
    })
}
