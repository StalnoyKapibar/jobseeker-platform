var news;
var headline_check = false;
var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    bootstrapValidate('#n_headline', 'regex:^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$:Поле может содержать русские и латинские буквы, цифры, пробелы, круглые скобки от 3-х до 100 символов',
        function (isValid) {
            if (isValid) {
                $('#n_headline').addClass('is-valid');
                headline_check = true;
            } else {
                headline_check = false;
            }
        }
    );

    $('#n_description').summernote({
        height: 200,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,
        popover: {
            air: [
                ['color', ['color']],
                ['font', ['bold', 'underline', 'clear']]
            ]
        } // set maximum height of editor
    });
    showTags();
});



$(function () {
    $('#search_box').bind("input keyup click", function () {
        if (this.value == '') {
            $("#search_advice_wrapper").remove();
            $(".search_area").append('<div id="search_advice_wrapper"></div>')
        }
        if (this.value.length > 0) {
            var param = this.value;
            $.ajax({
                type: 'post',
                url: "api/tags/search",
                contentType: 'application/json; charset=utf-8',
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                data: JSON.stringify(param),
                success: function (data) {
                    $("#search_advice_wrapper").html("").show();
                    if (data.length == 0) {
                        $('#search_advice_wrapper').append('<div class="advice_variant"> По запросу "' + param + '" ничего не найдено</div>');
                    } else {
                        $.each(data, function (key, value) {
                            var isAdded = false;
                            $('.listTags').each(function (i, item) {
                                var tagName = $(this).find('.tagButton').text();
                                if (value.name === tagName) {
                                    isAdded = true;
                                }
                            });
                            if (isAdded) {
                                $('#search_advice_wrapper').append('<div style="display: none" class="advice_variant"  onclick="addTag(\'' + value.id + '\',\'' + value.name + '\')">' + value.name + '</div>');
                            } else {
                                $('#search_advice_wrapper').append('<div class="advice_variant" onclick="addTag(\'' + value.id + '\',\'' + value.name + '\')">' + value.name + '</div>');
                            }
                        });
                    }
                },
                error: function (error) {
                    console.log(error);
                    alert(error.toString());
                }
            })
        }
    });

    $('html').on('click', function () {
        $('#search_advice_wrapper').hide();
    });
});

function addTag(id, name) {
    var div = document.getElementById("selectedTags");
    var nodelist = div.getElementsByTagName("label").length;
    if (nodelist === 0) {
        div.style.display = "block";
    }
    $("#selectedTags").append("<label class='listTags' id='tagItem-" + id + "'><span class='badge badge-pill badge-success tagButton' value='" + id + "' id='v_tagLabel_" + id + "' onclick='deleteTag(" + id + ",\"" + name + "\")'>" + name + "</span></label> ");
    $("#tagLabel_" + id).remove();
    $('#search_box').val('');
    $('#search_advice_wrapper').fadeOut(350).html('');
}


function deleteTag(id, name) {
    var div = document.getElementById("selectedTags");
    $('#tagItem-' + id).remove();
    $("#tagsWell").append("<span class='badge badge-pill badge-success' value='" + name + "'id='tagLabel_" + id + "' onclick='addTag(" + id + ",\"" + name + "\")'>" + name + "</span>");
    $("#v_tagLabel_" + id).remove();
    var nodelist = div.getElementsByTagName("label").length;
    if (nodelist === 0) {
        div.style.display = "none";
    }
}


function showTags() {
    $.ajax({
        url: "/api/tags/",
        type: "GET",
        async: false,
        success: function (data) {
            $.each(data, function (key, value) {
                $("#tagsWell").append("<span class='badge badge-pill badge-success' value='" + value.name + "' id='tagLabel_" + value.id + "' onclick='addTag(" + value.id + ",\"" + value.name + "\")'>" + value.name + "</span>");
            });
        }
    });
}


function validateAndPreview() {
    var div = document.getElementById("selectedTags");
    var nodelist = div.getElementsByTagName("label").length;
    if (nodelist < 1) {
        alert('Выберите как минимум 1 тег!');
        return;
    } else {
        var tags = $("#selectedTags").find("span").map(function () {
            return this.innerText;
        }).get();
    }
    if (headline_check) {
        var headline = $("#n_headline").val();
        var description = $('#n_description').summernote('code');
        var date = new Date();
        var employerProfileId = $('#employerProfileId').val();

        news = {
            'headline': headline,
            'description': description,
            'date': date
        };
    }

    $.ajax({
        method: "post",
        url: "/api/news/add?employerProfileId=" + employerProfileId + "&tags=" + tags,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(news),
        success: function () {
            $("#n_headline").val('');
            $('.note-editable').text('');
            $('#selectedTags').html("");
            $('#tagsWell').html("");
            showTags();
            alert('Новость добавлена');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}