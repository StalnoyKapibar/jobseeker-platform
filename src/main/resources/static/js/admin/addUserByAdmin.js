var email_check = false;
var pass_check = false;
var pass_conf_check = false;
var role_check = false;

$(document).ready(function () {
    feather.replace();
    bootstrapValidate('#user_email', 'email:Введите корректный Email-адрес', function (isValid) {
        if (isValid) {
            exists();
        } else {
            $('#exists_warning').html('');
            email_check = false;
        }
    })

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

    bootstrapValidate('#role', 'required:Нужно выбрать роль', function (isValid) {
        if (isValid) {
            $('#role').addClass('is-valid');
            role_check = true;
        } else {
            role_check = false;
        }
    })
})

function alertForm() {
    if (email_check && pass_check && pass_conf_check && role_check) {
        $('#ups_message').hide();
    }
    $('#success_message').hide();
}

function notifyUser() {
    if (document.getElementById('notify').checked) {
        $('#notify').addClass("is-valid");
    } else {
        $('#notify').removeClass("is-valid");
    }
}

function validityForm() {
    if (email_check && pass_check && pass_conf_check && role_check) {
        addUser();
    } else {
        $('#ups_message').slideDown({opacity: "show"}, "slow");
    }
}


function addUser() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var userEmail = $("#user_email").val();
    var userPass = $("#user_password").val();
    var role = {'authority': $("#role").val()};
    var checkBox = document.getElementById("notify");

    var newUser = {
        'email': userEmail,
        'password': userPass,
        'authority': role
    };

    $.ajax({
        url: "/api/users/addUserByAdmin/" + checkBox.checked,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        method: "POST",
        data: JSON.stringify(newUser),
        success:
            function (data) {
                console.log(data);
                $('#email_ver').text(userEmail);
                $('#success_message').slideDown({opacity: "show"}, "slow");
                $('#ups_message').hide();
                $('#contact_form')[0].reset();
                $('#user_email').removeClass('is-valid');
                $('#user_password').removeClass('is-valid');
                $('#confirm_password').removeClass('is-valid');
                $('#notify').removeClass('is-valid');
                $('#role').removeClass('is-valid');
            },
        error: function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        }
    })
}

function exists() {
    $.ajax({
        url: '/api/users/email/'
            + $('#user_email').val(),
        success: function (result) {
            if (result) {
                $('#user_email').addClass("is-invalid");
                $('#exists_warning').html("Email-адрес уже существует");
                email_check = false;
            } else {
                $('#user_email').addClass("is-valid");
                email_check = true;
            }
        }
    })
}
