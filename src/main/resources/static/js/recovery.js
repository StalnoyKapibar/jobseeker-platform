function sendPass() {
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

