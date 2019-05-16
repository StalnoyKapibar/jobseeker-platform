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
                validators: {
                    notEmpty: {
                        message: 'Введите Ваш Email-адрес'
                    },
                    regexp: {
                        regexp: /^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$/,
                        message: 'Введите корректный Email-адрес'
                    },
                    remote: {
                        url: function () {
                            return '/api/users/login/' + $("#user_login").val();
                        },
                        message: 'Данный Email уже используется'
                    }
                }
            },
            user_password: {
                validators: {
                    notEmpty: {
                        message: 'Введите пароль'
                    },
                    regexp: {
                        regexp: /^(?=.*[A-Z].*)(?=.*[a-z].*)(?=.*[0-9].*)[A-Za-z0-9]{8,20}$/,
                        message: 'Пароль должен содержать строчные и заглавные латинские буквы и цифры, от 8 до 20 символов'
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
    var role = {'authority': $("#role").val()};

    var newuser = {
        'login': userLogin,
        'password': userPass,
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