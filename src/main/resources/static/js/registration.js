$(document).ready(function () {
    $('#contact_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live: 'enabled',
        fields: {
            user_login: {
                trigger: 'blur',
                message: 'The username is not valid',
                validators: {
                    stringLength: {
                        min: 3,
                        max: 20,
                        message: 'Логин должен состоять от 3 до 20 символов'
                    },
                    notEmpty: {
                        message: 'Введите логин'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: 'Логин может содержать только строчные и заглавные латинские буквы, цифры и нижнее подчеркивание'
                    },
                    remote: {
                        url: function () {
                            return '/api/users/login/' + $("#user_login").val();
                        },
                        type: 'GET',
                        message: 'Логин не доступен'
                    }
                }
            },
            user_password: {
                validators: {
                    stringLength: {
                        min: 8,
                        max: 20,
                        message: 'Пароль должен состоять от 8 до 20 символов'
                    },
                    notEmpty: {
                        message: 'Введите пароль'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: 'Пароль может содержать только строчные и заглавные латинские буквы, цифры и нижнее подчеркивание'
                    }
                }
            },
            confirm_password: {
                validators: {
                    identical: {
                        field: 'user_password',
                        message: 'Пароли не совпадают'
                    },
                    notEmpty: {
                        message: 'Введите пароль'
                    }
                }
            },
            email: {
                trigger: 'blur',
                validators: {
                    notEmpty: {
                        message: 'Введите Ваш Email адрес'
                    },
                    emailAddress: {
                        message: 'Введите корректный Email адрес'
                    },
                    remote: {
                        url: function () {
                            return '/api/users/email/' + $("#email").val();
                        },
                        message: 'Данный Email уже используется'
                    }
                }
            },
            role: {
                validators: {
                    notEmpty: {
                        message: 'Выберите вид аккаунта'
                    }
                }
            }
        }
    })
});

function validateAndReg() {
    var bootstrapValidator = $('#contact_form').data('bootstrapValidator');
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        addUser();
    }
}

function addUser() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var userLogin = $("#user_login").val();
    var userPass = $("#user_password").val();
    var userEmail = $("#email").val();
    var role = {'authority': $("#role").val()};

    var newuser = {
        'login': userLogin,
        'password': userPass,
        'email': userEmail,
        'authority': role
    };

    $.ajax({
        url: "/api/users/add",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        method: "POST",
        data: JSON.stringify(newuser),
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