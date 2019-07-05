$(document).ready(function () {

    feather.replace();
    bootstrapValidate('#user_email', 'email:Введите корректный Email-адрес', function (isValid) {
        if (isValid) {
            exists();
        } else {
            $('#exists_warning').html('');
        }
    })
    bootstrapValidate('#user_password', 'regex:^(?=.*[a-z].*)(?=.*[0-9].*)[A-Za-z0-9]{6,20}$:Пароль должен содержать латинские буквы и цифры, от 6 до 20 символов', function (isValid) {
        if (isValid) {
            $('#user_password').addClass('is-valid');
        } else {

        }
    })
    bootstrapValidate('#confirm_password', 'matches:#user_password:Пароли не совпадают', function (isValid) {
        if (isValid) {
            $('#confirm_password').addClass('is-valid');
        }
    })
    bootstrapValidate('#role', 'required:Нужно выбрать роль', function (isValid) {
        if (isValid) {
            $('#role').addClass('is-valid');
        }
    })
})

// }
// (function () {
//     'use strict';
//     window.addEventListener('load', function () {
//         var form = document.getElementById('contact_form');
//         form.addEventListener('submit', function (event) {
//             if (form.checkValidity() === false) {
//                 alert("Форма заполнена не верно");
//                 event.preventDefault();
//                 event.stopPropagation();
//             } else {
//                 addUser();
//             }
//             form.classList.add('was-validated');
//         }, false);
//     }, false);
// })();

function addUser() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var userEmail = $("#user_email").val();
    var userPass = $("#user_password").val();
    var role = {'authority': $("#role").val()};
    var checkBox = document.getElementById("myCheck");

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
                $('#contact_form')[0].reset();
                $('#contact_form').data('bootstrapValidator').resetForm();
            },
        error: function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        }
    });
}

function exists() {
    $.ajax({
        url: '/api/users/email/'
            + $('#user_email').val(),
        success: function (result) {
            if (result) {
                $('#user_email').addClass("is-invalid");
                $('#exists_warning').html("Email-адрес уже существует");
            } else {
                $('#user_email').addClass("is-valid");
            }
        }
    })
}
