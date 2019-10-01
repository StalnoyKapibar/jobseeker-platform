let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let resume;
let jobExperiences = [];
let flagTag = false;
let tagId = 0;
let headline_check = false;
let address_check = false;
let salary_min_check = true;
let salary_max_check = true;
let company_check = true;
let position_check = true;
let responsibilities_check = true;
let firstDay_check = true;
let lastDay_check = true;

$(document).ready(function () {
    bootstrapValidate('#v_headline', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_headline').addClass('is-valid');
                headline_check = true;
            } else {
                headline_check = false;
            }
        });

    bootstrapValidate('#v_address', 'required:', function (isValid) {
        if (isValid) {
            $('#v_address').removeClass('is-invalid');
            $('#v_address').addClass('is-valid');
            address_check = true;
        } else {
            address_check = false;
        }
    });

    bootstrapValidate('#v_address_modal', 'required:', function (isValid) {
        if (isValid) {
            $('#v_address_modal').removeClass('is-invalid');
            $('#v_address_modal').addClass('is-valid');
            $('#v_address').removeClass('is-invalid');
            $('#v_address').addClass('is-valid');
            address_check = true;
        } else {
            $('#v_address_modal').removeClass('is-valid');
            $('#v_address_modal').addClass('is-invalid');
        }
    });

    bootstrapValidate('#v_salaryMin', 'regex:^[0-9]{4,7}$:Поле может содержать цифры от 4 до 7 разрядов' +
        ' или остаться пустым',
        function (isValid) {
            if (isValid) {
                $('#v_salaryMin').addClass('is-valid');
                salary_min_check = true;
            } else {
                let check_salary = $('#v_salaryMin').val();
                salary_min_check = check_salary.length < 1;
            }
        });

    bootstrapValidate('#v_salaryMax', 'regex:^[0-9]{4,7}$:Поле может содержать цифры от 4 до 7 разрядов' +
        ' или остаться пустым',
        function (isValid) {
            if (isValid) {
                $('#v_salaryMax').addClass('is-valid');
                salary_max_check = true;
            } else {
                let check_salary = $('#v_salaryMax').val();
                salary_max_check = check_salary.length < 1;
            }
        });

    $("#search_tags").keyup(function (e) {
        e.preventDefault();
        tags_search();
    });
});

function validateAndPreview() {
    let isValid = headline_check && address_check && salary_min_check && salary_max_check &&
        company_check && firstDay_check && lastDay_check && position_check && responsibilities_check;

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
        let companyName;
        let firstWorkDay;
        let lastWorkDay;
        let position;
        let responsibilities;
        let headline = $("#v_headline").val();
        let salaryMin = $("#v_salaryMin").val().length > 0 ? $("#v_salaryMin").val() : null;
        let salaryMax = $("#v_salaryMax").val().length > 0 ? $("#v_salaryMax").val() : null;
        let tags = [];
        $("#v_tagsWell").children("span").each(function (index, element) {
            tags[index] = {'name': $(element).text()}
        });
        let loc = getCoordsByAddress($("#v_address").val());
        let point = {'latitudeY': loc.lat, 'longitudeX': loc.lng};
        let city = {
            'id': null,
            'name': getCityByCoords(loc.lat, loc.lng),
            'centerPoint': point,
            'cityDistances': null
        };
        companyName = $("#v_company").val();
        firstWorkDay = $("#v_firstDay").val();
        lastWorkDay = $("#v_lastDay").val();
        position = $("#v_position").val();
        responsibilities = $("#v_responsibilities").val();
        jobExperiences[0] = {
            'companyName': companyName,
            'firstWorkDay': firstWorkDay,
            'lastWorkDay': lastWorkDay,
            'position': position,
            'responsibilities': responsibilities
        };
        resume = {
            'id': null,
            'headline': headline,
            'city': city,
            'jobExperiences': jobExperiences,
            'salaryMin': salaryMin,
            'salaryMax': salaryMax,
            'tags': tags,
            'coordinates': point
        };
        let jobHead = "Предыдущий опыт работы<br>";
        let jobExp = "";
        let jobPer = "";
        if (jobExperiences[0].companyName === "") {
            jobExp += '<span>Нет опыта</span> <br>';
        } else {
            $.each(jobExperiences, function (key, value) {
                jobPer += 'Время работы: <br>';
                jobExp += '<span>Место работы: ' + value.companyName + '</span><br>';
                jobExp += '<span>Должность: ' + value.position + '</span><br>';
                jobExp += '<span>Выполняемые задачи: ' + value.responsibilities + '</span><br>';
                let viewFirstDay = new Date(value.firstWorkDay);
                jobPer += '<span>' + viewFirstDay.toLocaleDateString() + '</span>- ';
                let viewLastDay = new Date(value.lastWorkDay);
                jobPer += '<span>' + viewLastDay.toLocaleDateString() + '</span><br>';
                return key > 2;
            });
        }
        $("#VMHeadline").text(resume.headline);
        $("#VMCity").text(resume.city.name);
        $("#VMJobHead").html(jobHead);
        $("#VMJobPer").html(jobPer);
        $("#VMJobExp").html(jobExp);
        let str = "Зарплата: ";
        if (resume.salaryMin != null) {
            str = str + "от " + resume.salaryMin + " рублей ";
        }
        if (resume.salaryMax != null) {
            str = str + "до " + resume.salaryMax + " рублей";
        }
        if (str == "Зарплата: ") {
            str = "Зарплата не указана";
        }
        $("#VMSalary").text(str);
        str = "";
        $.each(resume.tags, function (key, value) {
            str += value.name + " "
        });
        $("#VMTags").text(str);
        $('#map_collapse').attr("class", "collapsed collapse");
        jQuery.noConflict();
        $('#resumeModal').modal('toggle');
        let lat = resume.coordinates.latitudeY;
        let lng = resume.coordinates.longitudeX;
        showVacancyOnMap(lat, lng);
        let address = getAddressByCoords(lat, lng);
        $("#VMAddress").text(address);
    }
}

function sendResume() {
    $.ajax({
        method: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/api/resumes/add",
        data: JSON.stringify(resume),
        beforeSend: function (request) {
            return request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#resume_container").empty();
            $("#resume_container").append("<div class='alert alert-success' role='alert'>" +
                "Резюме добавлено!<br/>Теперь вы можете просмотреть его в разделе " +
                "<a href='/seeker/resumes'>&quot;Мои резюме&quot;</a></div>");
        },
        error: function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        }
    });
}

function tags_search() {
    let tags_span = $("#tagsWell").find("span");
    tags_span.hide();
    let str = $("#search_tags").val();
    if (str === "") {
        tags_span.show();
    } else {
        tags_span.filter("[value ^= '" + str + "']").show();
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
                    $("#tagsWell").append("<span class='label label-success' " +
                        "value='" + value.name + "' id='tagLabel_" + value.id + "' " +
                        "onclick='addTag(" + value.id + ",\"" + value.name + "\")'>" + value.name + "</span>");
                });
                flagTag = true;
            }
        });
    }
}

function addTag(id, name) {
    $("#v_tagsWell").append("<span class='label label-success' id='v_tagLabel_" +
        id + "' onclick='deleteTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    $("#tagLabel_" + id).remove();
}

function addNewTag() {
    tagId--;
    let searchTag = $("#search_tags");
    let name = searchTag.val();
    $("#v_tagsWell").append("<span class='label label-success' id='v_tagLabel_" + tagId + "' " +
        "onclick='deleteTag(" + tagId + ",\"" + name + "\")'>" + name + "</span>");
    searchTag.val(""); // clear filter after adding tag
    tags_search(); // refresh visible tags with empty filter
}

function deleteTag(id, name) {
    $("#tagsWell").append("<span class='label label-success' value='" + name + "' id='tagLabel_" +
        id + "' onclick='addTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    $("#v_tagLabel_" + id).remove();
}

function jobOn() {
    $('#cancelButton').prop('disabled', false);
    $('#editButton').prop('disabled', true);
    $('#v_company').addClass('is-invalid');
    $('#v_firstDay').addClass('is-invalid');
    $('#v_lastDay').addClass('is-invalid');
    $('#v_position').addClass('is-invalid');
    $('#v_responsibilities').addClass('is-invalid');
    $("#v_company").prop('disabled', false);
    $("#v_firstDay").prop('disabled', false);
    $("#v_lastDay").prop('disabled', false);
    $("#v_position").prop('disabled', false);
    $("#v_responsibilities").prop('disabled', false);
    $("#v_company").prop('placeholder', "Введите название компании (обязательно)");
    $("#v_firstDay").prop('placeholder', "День начала работы (обязательно)");
    $("#v_lastDay").prop('placeholder', "День окончания работы (обязательно)");
    $("#v_position").prop('placeholder', "Введите название должности (обязательно)");
    $("#v_responsibilities").prop('placeholder', "Исполнявшиеся Вами обязанности (обязательно)");
    company_check = false;
    firstDay_check = false;
    lastDay_check = false;
    position_check = false;
    responsibilities_check = false;
    bootstrapValidate('#v_company', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_company').removeClass('is-invalid');
                $('#v_company').addClass('is-valid');
                company_check = true;
            } else {
                company_check = false;
            }
        }
    );
    bootstrapValidate('#v_firstDay', 'required:', function (isValid) {
        if (isValid) {
            $('#v_firstDay').removeClass('is-invalid');
            $('#v_firstDay').addClass('is-valid');
            firstDay_check = true;
        } else {
            firstDay_check = false;
        }
    });
    bootstrapValidate('#v_lastDay', 'required:', function (isValid) {
        if (isValid) {
            $('#v_lastDay').removeClass('is-invalid');
            $('#v_lastDay').addClass('is-valid');
            lastDay_check = true;
        } else {
            lastDay_check = false;
        }
    });
    bootstrapValidate('#v_position', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_position').removeClass('is-invalid');
                $('#v_position').addClass('is-valid');
                position_check = true;
            } else {
                position_check = false;
            }
        }
    );
    bootstrapValidate('#v_responsibilities', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_responsibilities').removeClass('is-invalid');
                $('#v_responsibilities').addClass('is-valid');
                responsibilities_check = true;
            } else {
                responsibilities_check = false;
            }
        }
    );
}

function jobOff() {
    $('#editButton').prop('disabled', false);
    $('#cancelButton').prop('disabled', true);
    $('#v_company').removeClass('is-invalid');
    $('#v_company').addClass('is-valid');
    $('#v_firstDay').removeClass('is-invalid');
    $('#v_firstDay').addClass('is-valid');
    $('#v_lastDay').removeClass('is-invalid');
    $('#v_lastDay').addClass('is-valid');
    $('#v_position').removeClass('is-invalid');
    $('#v_position').addClass('is-valid');
    $('#v_responsibilities').removeClass('is-invalid');
    $('#v_responsibilities').addClass('is-valid');
    $("#v_company").prop('disabled', true);
    $("#v_firstDay").prop('disabled', true);
    $("#v_lastDay").prop('disabled', true);
    $("#v_position").prop('disabled', true);
    $("#v_responsibilities").prop('disabled', true);
    $("#v_company").prop('placeholder', "Можно не указывать");
    $("#v_firstDay").prop('placeholder', "Можно не указывать");
    $("#v_lastDay").prop('placeholder', "Можно не указывать");
    $("#v_position").prop('placeholder', "Можно не указывать");
    $("#v_responsibilities").prop('placeholder', "Можно не указывать");
    $("#v_company").val(null);
    $("#v_firstDay").val(null);
    $("#v_lastDay").val(null);
    $("#v_position").val(null);
    $("#v_responsibilities").val(null);
    company_check = true;
    firstDay_check = true;
    lastDay_check = true;
    position_check = true;
    responsibilities_check = true;
}