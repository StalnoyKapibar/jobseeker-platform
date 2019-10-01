let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let resume;
let jobExperiences = [];
let flagTag = false;
let tagId = 0;
let headline_check = true;
let address_check = true;
let salary_min_check = true;
let salary_max_check = true;
let company_check = true;
let position_check = true;
let responsibilities_check = true;
let firstDay_check = true;
let lastDay_check = true;
let lat;
let lng;

$(document).ready(function () {
    checkJobsExist();
    lat = parseFloat($("#lat").val());
    lng = parseFloat($("#long").val());
    let address = getAddressByCoords(lat, lng);
    $("#v_address").val(address);

    bootstrapValidate('#v_headline', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_headline').addClass('is-valid');
                headline_check = true;
            } else {
                headline_check = false;
            }
        }
    );

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

    if (checkJobsExist()) {
        let beginDate = new Date($("#v_firstDay").val());
        let endDate = new Date($("#v_lastDay").val());
        $("#v_firstDays").prop('value', formatDate(beginDate));
        $("#v_lastDays").prop('value', formatDate(endDate));
        bootstrapValidate('#v_company', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
            'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
            function (isValid) {
                if (isValid) {
                    $('#v_company').addClass('is-valid');
                    company_check = true;
                } else {
                    company_check = false;
                }
            }
        );

        bootstrapValidate('#v_firstDays', 'required:', function (isValid) {
            if (isValid) {
                $('#v_firstDays').removeClass('is-invalid');
                $('#v_firstDays').addClass('is-valid');
                firstDay_check = true;
            } else {
                firstDay_check = false;
            }
        });

        bootstrapValidate('#v_lastDays', 'required:', function (isValid) {
            if (isValid) {
                $('#v_lastDays').removeClass('is-invalid');
                $('#v_lastDays').addClass('is-valid');
                lastDay_check = true;
            } else {
                lastDay_check = false;
            }
        });

        bootstrapValidate('#v_position', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
            'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
            function (isValid) {
                if (isValid) {
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
                    $('#v_responsibilities').addClass('is-valid');
                    responsibilities_check = true;
                } else {
                    responsibilities_check = false;
                }
            }
        );
    }
    $("#search_tags").keyup(function (e) {
        e.preventDefault();
        tags_search();
    });
});

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
                    let val = document.getElementById('v_tagLabel_' + value.id + '');
                    if (val === null) {
                        $("#tagsWell").append("<span class='label label-success' " +
                            "value='" + value.name + "' id='tagLabel_" + value.id + "' " +
                            "onclick='addTag(" + value.id + ",\"" + value.name + "\")'>" +
                            value.name + "</span>");
                    }
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
    $("#v_tagsWell").append("<span class='label label-success' id='v_tagLabel_" +
        tagId + "' onclick='deleteTag(" + tagId + ",\"" + name + "\")'>" + name + "</span>");
    searchTag.val(""); // clear filter after adding tag
    tags_search(); // refresh visible tags with empty filter
}

function deleteTag(id, name) {
    if (flagTag) {
        $("#tagsWell").append("<span class='label label-success' value='" +
            name + "' id='tagLabel_" + id + "' onclick='addTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    }
    $("#v_tagLabel_" + id).remove();
}

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
        let experienceId;
        let companyName;
        let firstWorkDay;
        let lastWorkDay;
        let position;
        let responsibilities;
        let id = $("#resumeId").val();
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
        if (checkJobsExist()) {
            experienceId = $("#v_experienceId").val();
            companyName = $("#v_company").val();
            firstWorkDay = $("#v_firstDays").val();
            lastWorkDay = $("#v_lastDays").val();
            position = $("#v_position").val();
            responsibilities = $("#v_responsibilities").val();
        } else {
            companyName = $("#v_newCompany").val();
            firstWorkDay = $("#v_newFirstDay").val();
            lastWorkDay = $("#v_newLastDay").val();
            position = $("#v_newPosition").val();
            responsibilities = $("#v_newResponsibilities").val();
        }

        jobExperiences[0] = {
            'id': experienceId,
            'companyName': companyName,
            'firstWorkDay': firstWorkDay,
            'lastWorkDay': lastWorkDay,
            'position': position,
            'responsibilities': responsibilities
        };

        resume = {
            'id': id,
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
                jobPer += '<span>' + viewFirstDay.toLocaleDateString() + '</span> - ';
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
        url: "/api/resumes/update",
        data: JSON.stringify(resume),
        beforeSend: function (request) {
            return request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#resume_container").empty();
            $("#resume_container").append("<div class='alert alert-success' role='alert'>" +
                "Резюме добавлено!<br/>Вы также можете <a href='/user'>посмотреть свой профиль</a></div>");
        },
        error: function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        }
    });
}

function checkJobsExist() {
    if ($("#v_experienceId").length != 0) {
        $("#jobExperienceSet").prop('hidden', false);
        return true;
    } else {
        $("#addExperienceButton").prop('hidden', false);
        return false;
    }
}

function formatDate(date) {
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    if (day < 10) {
        day = '0' + day;
    }
    if (month < 10) {
        month = '0' + month;
    }
    return year + '-' + month + '-' + day;
}

function addExperience() {
    $("#addExperienceButton").prop('hidden', true);
    $("#addJobExperienceSet").prop('hidden', false);
    $('#v_newCompany').addClass('is-invalid');
    $('#v_newFirstDay').addClass('is-invalid');
    $('#v_newLastDay').addClass('is-invalid');
    $('#v_newPosition').addClass('is-invalid');
    $('#v_newResponsibilities').addClass('is-invalid');
    $("#v_newCompany").prop('disabled', false);
    $("#v_newFirstDay").prop('disabled', false);
    $("#v_newLastDay").prop('disabled', false);
    $("#v_newPosition").prop('disabled', false);
    $("#v_newResponsibilities").prop('disabled', false);
    company_check = false;
    firstDay_check = false;
    lastDay_check = false;
    position_check = false;
    responsibilities_check = false;
    bootstrapValidate('#v_newCompany', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_newCompany').removeClass('is-invalid');
                $('#v_newCompany').addClass('is-valid');
                company_check = true;
            } else {
                company_check = false;
            }
        }
    );
    bootstrapValidate('#v_newFirstDay', 'required:', function (isValid) {
        if (isValid) {
            $('#v_newFirstDay').removeClass('is-invalid');
            $('#v_newFirstDay').addClass('is-valid');
            firstDay_check = true;
        } else {
            firstDay_check = false;
        }
    });
    bootstrapValidate('#v_newLastDay', 'required:', function (isValid) {
        if (isValid) {
            $('#v_newLastDay').removeClass('is-invalid');
            $('#v_newLastDay').addClass('is-valid');
            lastDay_check = true;
        } else {
            lastDay_check = false;
        }
    });
    bootstrapValidate('#v_newPosition', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_newPosition').removeClass('is-invalid');
                $('#v_newPosition').addClass('is-valid');
                position_check = true;
            } else {
                position_check = false;
            }
        }
    );
    bootstrapValidate('#v_newResponsibilities', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:' +
        'Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#v_newResponsibilities').removeClass('is-invalid');
                $('#v_newResponsibilities').addClass('is-valid');
                responsibilities_check = true;
            } else {
                responsibilities_check = false;
            }
        }
    );
    $("#hideExperienceButton").prop('hidden', false);
}

function hideExperience() {
    $("#hideExperienceButton").prop('hidden', true);
    $("#addJobExperienceSet").prop('hidden', true);
    $("#v_newCompany").prop('disabled', true);
    $("#v_newFirstDay").prop('disabled', true);
    $("#v_newLastDay").prop('disabled', true);
    $("#v_newPosition").prop('disabled', true);
    $("#v_newResponsibilities").prop('disabled', true);
    $("#addExperienceButton").prop('hidden', false);
    $("#v_newCompany").val(null);
    $("#v_newFirstDay").val(null);
    $("#v_newLastDay").val(null);
    $("#v_newPosition").val(null);
    $("#v_newResponsibilities").val(null);
    company_check = true;
    firstDay_check = true;
    lastDay_check = true;
    position_check = true;
    responsibilities_check = true;
}