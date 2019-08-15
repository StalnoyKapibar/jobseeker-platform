var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var tagsName = [];
var avelibleTags = [];
$(document).ready(function () {

    $.ajax({
        url: "/api/tags/",
        type: "GET",
        async: false,
        success: function (tags) {
            for (var i = 0; i < tags.length; i++) {
                tagsName[i] = tags[i].name;
                avelibleTags[i] = tags[i];
            }
        }
    });

    $("#tags_input").autocomplete({
        source: tagsName
    });

    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
});

function add_seeker_img(id) {
    var form = $('#imageUploadForm')[0];
    var data = new FormData(form);
    data.append("id", id);
    $("#btnSubmit").prop("disabled", true);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/api/seekerprofiles/update_image",
        data: data,
        processData: false,
        contentType: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (image) {
            $('#profile_img').html('<img src="data:image/png;base64,' + image + '" class="img-rounded"' +
                '                                     alt="profile image">');
            $("#btnSubmit").prop("disabled", false);
        },
        error: function (e) {
            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);
        }
    });
}

function show_portfolio(id) {
    $.ajax({
        url: "/api/portfolios/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            $("#portf_name_input").val(data.projectName);
            $("#portf_link_input").val(data.link);
            $("#portf_description_textarea").text(data.description);
            $('#prtf_id_to_updte').val(data.id);
            $("#portfolioModal").modal('show');
        }
    });
}

function add_new_tag() {
    var newTag = $("#tags_input").val();
    var newTagsToUpdate = [];

    if (tagsName.indexOf(newTag) !== -1) {
        $('#tag_list').append('<span class="badge badge-pill badge-success btnClick text-dark" onclick="this.remove()">' +
            '<span>' + newTag + '</span> &times;</span>');
        $('#tag_list span').each(function () {
            var tagFromList = $(this).find('span').text();
            if (tagFromList !== null) {
                var result = avelibleTags.find(x => x.name === tagFromList);
                newTagsToUpdate.push(result);
            }
        });
        sessionStorage.setItem('tags', JSON.stringify(newTagsToUpdate));
        $("#tags_input").val('');
    }
}

function update(id) {
    var profile = {
        'id': id,
        'name': $('#name').val(),
        'surname': $('#surname').val(),
        'patronymic': $('#patronymic').val(),
        'description': $('#profile_description').val(),
        'tags': JSON.parse(sessionStorage.getItem('tags'))
    };
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/update",
        data: JSON.stringify(profile),
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {
            alert("Изменения успешно внесены")
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })

}

function show_add_portf_modal(id) {
    $('#profile_id_to_add_portf').val(id);
    $('#add_prtf_modal').modal('show');
}

function add_portfolio() {
    var profile =
        {
            'id': $('#profile_id_to_add_portf').val(),
            'portfolios':
                [{
                    'projectName': $('#add_portf_name_input').val(),
                    'link': $('#add_portf_link_input').val(),
                    'description': $('#add_portf_description_textarea').val()
                }]
        };
    $.ajax({
        type: 'post',
        url: "/api/portfolios/add",
        data: JSON.stringify(profile),
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (portfolios) {
            $('#portfolio_list').empty();

            for(var i = 0; i<portfolios.length; i++){
                $('#portfolio_list').append(' <a class="list-group-item list-group-item-action" href="#" ' +
                    'data-toggle="modal" data-target="#portfolioModal" onclick="show_portfolio(' + portfolios[i].id + ')">' + portfolios[i].projectName + '</a>')
            }
            $('#add_portf_name_input').val('');
            $('#add_portf_link_input').val('');
            $('#add_portf_description_textarea').val('');
            $('#add_portf_modal').modal('hide');
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })
}

function update_prtf() {
    var portfolio = {
        'id': $('#prtf_id_to_updte').val(),
        'projectName': $('#portf_name_input').val(),
        'link': $('#portf_link_input').val(),
        'description': $('#portf_description_textarea').val()
    };
    $.ajax({
        type: 'post',
        url: "/api/portfolios/update",
        data: JSON.stringify(portfolio),
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (portfolio) {
            $('#' + portfolio.id + '_portf').text(portfolio.projectName);
            $('#portf_modal').modal('hide');
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })
}

function del_prtf(id) {
    var portfolio = {
        'id': $('#prtf_id_to_updte').val()
    };
    $.ajax({
        type: 'post',
        url: "/api/portfolios/delete",
        data: JSON.stringify(portfolio),
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (portfolio) {
            $('#' + portfolio.id + '_portf').text(portfolio.projectName);
            $('#portf_modal').modal('hide');
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })
}



