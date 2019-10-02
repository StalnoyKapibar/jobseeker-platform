let vacancy;
let flagTag = false;
let tagId = 0;

let headline_check = true;
let address_check = true;
let remote_check = true;
let salary_min_check = true;
let salary_max_check = true;
let shrt_desc_check = true;
let desc_check = true;

let lat;
let lng;

$(document).ready(function () {
    lat = parseFloat($("#lat").val());
    lng = parseFloat($("#long").val());
    let address = getAddressByCoords(lat, lng);

    $("#v_address").val(address);


    bootstrapValidate('#v_headline', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
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

    bootstrapValidate('#v_remote', 'required:', function (isValid) {
        if (isValid) {
            $('#v_remote').removeClass('is-invalid');
            $('#v_remote').addClass('is-valid');
            remote_check = true;
        } else {
            remote_check = false;
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

    bootstrapValidate('#v_shortDescription', 'required:', function (isValid) {
        if (isValid) {
            $('#v_shortDescription').removeClass('is-invalid');
            $('#v_shortDescription').addClass('is-valid');
            shrt_desc_check = true;
        } else {
            shrt_desc_check = false;
        }
    });

    $("#search_tags").keyup(function (e) {
        e.preventDefault();
        tags_search();
    });

    vacancyDescription = $('#v_description').val();

    $('#v_description').summernote({
        height: 300,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,
        popover: {
            air: [
                ['color', ['color']],
                ['font', ['bold', 'underline', 'clear']]
            ]
        } // set maximum height of editor
    });

    $(".panel-heading").css('background-color', 'white');
//   $('#v_description').summernote('pasteHTML', vacancyDescription);

// Валидация поля подробного описания ( не менее 100 символов )
    if ($(".note-editable").text().length >= 100) {
        $(".invalid-feedback-description").hide();
        $(".note-editor.note-frame").css("border", "1px solid #28a745"); //#a9a9a9
        desc_check = true;
    } else {
        $(".invalid-feedback-description").show();
        $(".note-editor.note-frame").css("border", "1px solid #dc3545");
        desc_check = false;
    }

    $(".note-editable").keydown(function () {
        if ($(".note-editable").text().length >= 100) {
            $(".invalid-feedback-description").hide();
            $(".note-editor.note-frame").css("border", "1px solid #28a745"); //#a9a9a9
            desc_check = true;
        } else {
            $(".invalid-feedback-description").show();
            $(".note-editor.note-frame").css("border", "1px solid #dc3545");
            desc_check = false;
        }
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
    };

    function validateAndPreview() {
        let isValid = headline_check && address_check && remote_check
            && salary_min_check && salary_max_check && shrt_desc_check && desc_check;

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
            let id = $("#vacancyid").val();
            let headline = $("#v_headline").val();
            let remote = $("#v_remote").val() == "true";
            let shortDescription = $("#v_shortDescription").val();
            let description = $('#v_description').summernote('code');
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

            vacancy = {
                'id': id,
                'headline': headline,
                'city': city,
                'remote': remote,
                'shortDescription': shortDescription,
                'description': description,
                'salaryMin': salaryMin,
                'salaryMax': salaryMax,
                'tags': tags,
                'coordinates': point,
                'state': null,
                'expiryBlock': null
            };

            $("#VMHeadline").text(vacancy.headline);
            $("#VMCity").text(vacancy.city.name);
            $("#VMShortDescription").text(vacancy.shortDescription);
            $("#VMDescription").html(description);

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
            let address = getAddressByCoords(lat, lng);
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
        $("#tagsWell").append("<span class='label label-success' value='" +
            name + "'id='tagLabel_" + id + "' onclick='addTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
        $("#v_tagLabel_" + id).remove();
    }

    function sendVacancy() {
        const header = $("meta[name='_csrf_header']").attr("content");
        const token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            method: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            url: "/api/vacancies/update",
            data: JSON.stringify(vacancy),
            beforeSend: function (request) {
                return request.setRequestHeader(header, token);
            },
            success: function (data) {
                $("#vacancy_container").empty();
                $("#vacancy_container").append("<div class='alert alert-success' role='alert'>" +
                    "Вакансия обновлена!<br/>Вы также можете <a href='/user'>посмотреть свой профиль</a></div>");
            },
            error: function (error) {
                console.log(error);
                alert(error.responseJSON.message);
            }
        });
    }
