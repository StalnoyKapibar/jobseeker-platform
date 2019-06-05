var vacancy;
var flagTag = false;

$(document).ready(function () {
    $('#vacancy_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live: 'enabled',
        fields: {
            v_headline: {
                validators: {
                    notEmpty: {
                        message: 'Введите название должности'
                    },
                    regexp: {
                        regexp: /^[A-Za-z0-9А-Яа-я ()\-]{3,100}$/,
                        message: 'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов'
                    }
                }
            },
            v_city: {
                validators: {
                    notEmpty: {
                        message: 'Введите название города'
                    },
                    regexp: {
                        regexp: /^[A-Za-z0-9А-Яа-я ()\-]{3,100}$/,
                        message: 'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов'
                    }
                }
            },
            v_address: {
                validators: {
                    notEmpty: {
                        message: 'Введите адрес или выберите на карте'
                    }
                }
            },
            v_remote: {
                validators: {
                    notEmpty: {
                        message: 'Выберите место работы'
                    }
                }
            },
            v_salaryMin: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]{1,100}$/,
                        message: 'Поле может содержать цифры от одного до 10 разрядов'
                    }
                }
            },
            v_salaryMax: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]{1,100}$/,
                        message: 'Поле может содержать цифры от одного до 10 разрядов'
                    }
                }
            },
            v_shortDescription: {
                validators: {
                    notEmpty: {
                        message: 'Краткое описание должно быть заполнено'
                    }
                }
            },
            v_description: {
                validators: {
                    notEmpty: {
                        message: 'Подробное описание должно быть заполнено'
                    }
                }
            }
        }
    });

    $("#search_tags").keyup(function(e){
        e.preventDefault();
        tags_search();
    });
});

function tags_search() {
    let tags_span = $("#tagsWell").find("span");
    tags_span.hide();
    let str = $("#search_tags").val();
    if (str==""){
        tags_span.show();
    } else {
    tags_span.filter("[value ^= '"+str+"']").show();
    }
}

function validateAndPreview() {
    let bootstrapValidator = $('#vacancy_form').data('bootstrapValidator');
    bootstrapValidator.validate();
    let isValid = bootstrapValidator.isValid();
    if ($("#v_tagsWell").children().length < 2) {
        $("#v_form_group_tags").attr("class", "form-group has-feedback has-error");
        $("#v_tagsWell").css("border-color", "#a94442");
        $("#v_help_block_tags").css("display", "block");
        isValid = false;
    } else {
        $("#v_form_group_tags").attr("class", "form-group");
        $("#v_tagsWell").css("border-color", "#e3e3e3");
        $("#v_help_block_tags").css("display", "none");
    }
    if (isValid) {
        let headline = $("#v_headline").val();
        let city = $("#v_city").val();
        let remote = $("#v_remote").val() == "true";
        let shortDescription = $("#v_shortDescription").val();
        let description = $("#v_description").val();
        let salaryMin = $("#v_salaryMin").val().length > 0 ? $("#v_salaryMin").val() : null;
        let salaryMax = $("#v_salaryMax").val().length > 0 ? $("#v_salaryMax").val() : null;
        let tags = [];
        $("#v_tagsWell").children("span").each(function (index, element) {
            tags[index] = {'name': $(element).text()}
        });
        let loc = getCoordsByAddress($("#v_address").val());
        let point = {'latitudeY': loc.lat, 'longitudeX': loc.lng};

        vacancy = {
            'id' : null,
            'headline': headline,
            'city': city,
            'remote': remote,
            'shortDescription': shortDescription,
            'description': description,
            'salaryMin': salaryMin,
            'salaryMax': salaryMax,
            'tags': tags,
            'coordinates': point,
            'state' : null,
            'expiryBlock' : null
        };

        $("#VMHeadline").text(vacancy.headline);
        $("#VMCity").text(vacancy.city);
        $("#VMShortDescription").text(vacancy.shortDescription);
        $("#VMDescription").text(vacancy.description);

        let str = "Зарплата: ";
        if (vacancy.salaryMin != null) {
            str = str + "от " + vacancy.salaryMin + " рублей ";
        }
        if (vacancy.salaryMax != null) {
            str = str + "до " + vacancy.salaryMax + " рублей";
        }
        if (str == "Зарплата: ") {
            str = "Зарплата не указана";
        }
        $("#VMSalary").text(str);

        str = "";
        $.each(vacancy.tags, function (key, value) {
            str += value.name + " "
        });
        $("#VMTags").text(str);

        if (vacancy.remote) {
            $('#VMRemote').show();
        } else {
            $('#VMRemote').hide();
        }
        $('#map_collapse').attr("class", "collapsed collapse");

        $('#vacancyModal').modal('toggle');
        let lat = vacancy.coordinates.latitudeY;
        let lng = vacancy.coordinates.longitudeX;
        showVacancyOnMap(lat, lng);
        let address = getAddressByCoords(lat,lng);
        $("#VMAddress").text(address);
    }
}

function showTags() {
    if (!flagTag) {
        $.ajax({
            url: "/api/tags/",
            type: "GET",
            async: false,
            success: function (data) {
                $.each(data, function (key, value) {
                    $("#tagsWell").append("<span class='label label-success' value='"+ value.name +"' id='tagLabel_" + value.id + "' onclick='addTag(" + value.id + ",\"" + value.name + "\")'>" + value.name + "</span>");
                });
                flagTag = true;
            }
        });
    }
}

function addTag(id, name) {
    $("#v_tagsWell").append("<span class='label label-success' id='v_tagLabel_" + id + "' onclick='deleteTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    $("#tagLabel_" + id).remove();
}

function deleteTag(id, name) {
    $("#tagsWell").append("<span class='label label-success' value='"+ name +"'id='tagLabel_" + id + "' onclick='addTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    $("#v_tagLabel_" + id).remove();
}

function sendVacancy() {
    const header = $("meta[name='_csrf_header']").attr("content");
    const token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        method: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/api/vacancies/add",
        data: JSON.stringify(vacancy),
        beforeSend: function (request) {
            return request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#vacancy_container").empty();
            $("#vacancy_container").append("<div class='alert alert-success' role='alert'>Вакансия добавлена!<br/>Вы также можете <a href='/user'>посмотреть свой профиль</a></div>");
        },
        error: function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        }
    });
}
