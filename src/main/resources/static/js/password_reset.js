let pass_check = false;
let pass_conf_check = false;
$(document).ready( function () {
    bootstrapValidate('#user_password', 'regex:^(?=.*[a-z].*)(?=.*[0-9].*)[A-Za-z0-9]{6,20}$:Пароль должен содержать латинские буквы и цифры, от 6 до 20 символов', function (isValid) {
        if (isValid) {
            $('#user_password').addClass('is-valid');
            pass_check = true;
        } else {
            pass_check = false;
        }
        passConfirm($('#user_password').val() == $('#confirm_password').val())
    })

    bootstrapValidate('#confirm_password', 'matches:#user_password:', function (isValid) {
        passConfirm(isValid);
    })

    function passConfirm(isValid) {
        if (isValid) {
            $('#confirm_password').removeClass('is-invalid');
            $('#confirm_password').addClass('is-valid');
            pass_conf_check = true;
        } else {
            $('#confirm_password').addClass('is-invalid');
            pass_conf_check = false;
        }
    }
})
function alertForm() {
    if (pass_check && pass_conf_check) {
        $('#ups_message').hide();
    }
    $('#success_message').hide();
}

function validityPassAndSend() {
    if (pass_check && pass_conf_check) {
        sendPass();
    } else {
        $('#ups_message').slideDown({opacity: "show"}, "slow");
    }
}

function sendPass() {
    $.ajax({
        url: '/api/users/password_reset/' +$('#user_email').val() +'/' + $('#user_password').val(),
        success: function () {
            $('#success_message').slideDown({opacity:"show"},"slow");
        },
        error: function (error) {
            console.log(error);
            alert(error);
        }
    })

}

