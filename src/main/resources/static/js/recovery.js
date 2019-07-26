function sendEmail() {
    $.ajax({
        url: '/api/users/recovery/' + $('#user_email').val(),
        success: function () {
            $('#success_message').slideDown({opacity:"show"},"slow");
        },
        error: function (error) {
            console.log(error);
            alert(error);
        }
    })

}

function validityEmailAndSend() {
    $.ajax({
        url: '/api/users/email/'
            + $('#user_email').val(),
        success: function (result) {
            if (result) {
                $('#ups_message').hide();
                sendEmail();
            } else {
                $('#ups_message').slideDown({opacity:"show"},"slow");
            }
        }
    })
}
