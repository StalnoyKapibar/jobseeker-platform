var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function showPortfolio(id) {
    $.ajax({
        url: "/api/portfolios/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            $("#PMHeadline").text(data.projectName);
            $("#PMlink").text(data.link);
            $("#PMlink").attr("href", data.link);
            $("#PMDescription").text(data.description);
        }
    });
}

function showInviteModal(id) {
    $.ajax({
        url: "/api/seekerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (seeker) {
            $('#seekerProfile-input-add').val(seeker.name);
            $('#inviteFriendModal').modal();
        },
        error: function (message) {
            console.log(message);
        }
    });
}

function inviteFriend(id, friend) {
    $.ajax({
        url: "/api/users/inviteFriend/" + id + "/" + friend,
        type: "GET",
        async: false,
        success: function () {
            alert("Ваше приглашение было отправленно");
            $('#inviteFriendModal').modal('hide');
        }
    })
}

function inFavorite(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/inFavoriteVacancy?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#inFavorite').remove();
            $('#btnFavorite').append('<button id="outFavorite" class="btn btn-primary" onclick="outFavorite(' + vacancyId + ',' + seekerProfileId + ')">Убрать из избранное' +
                '</button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function outFavorite(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/outFavoriteVacancy?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#outFavorite').remove();
            $('#btnFavorite').append('<button id="inFavorite" class="btn btn-primary" onclick="inFavorite(' + vacancyId + ',' + seekerProfileId + ')">В избранное' +
                '</button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function showUpdateNameModal(id) {
    $.ajax({
        url: "/api/seekerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (seeker) {
            $('#update_name_input').val(seeker.name);
            $('#update_patronymic_input').val(seeker.patronymic);
            $('#update_sure_name_input').val(seeker.surname);
            $('#profile_id_for_name').val(seeker.id)
            $('#update_name_modal').modal();
        },
        error: function (message) {
            console.log(message);
        }
    });
}

function updateName(id, name, patronymic, surname) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/update?" +
            "id=" + id +
            "&name=" + name +
            "&patronymic=" + patronymic +
            "&surname=" + surname,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {
            // document.getElementById('update_name_input').val("");
            document.getElementById("profile_name").innerHTML = '';
            var text = document.createElement("span");
            text.innerText = profile.name + " " + profile.patronymic + " " + profile.surname;
            document.getElementById("profile_name").appendChild(text);
            $('#update_name_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function showModalUpdatePortfolio(id) {
    $.ajax({
        url: "/api/portfolios/" + id,
        type: "GET",
        async: false,
        success: function (project) {
            $("#project_id_for_update").val(project.id);
            $("#update_project_name_input").val(project.projectName);
            $("#update_project_link_input").val(project.link);
            // $("#PMlink").attr("href", data.link);
            $("#update_project_description_textarea").text(project.description);
        }
    });
}

function updateProject(id, name, link, description) {
    $.ajax({
        type: 'post',
        url: "/api/portfolios/update?id=" + id + "&name="
            + name
            + "&link=" + link
            + "&description=" + description,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (project) {
            document.getElementById("project_td_" + project.id).innerText = project.projectName;
            $('#update_project_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })
}

function showModalAddNewProject(id) {
    $("#project_id_for_add_new_project").val(id);
}

function add_new_project(id, name, link, description) {
    $.ajax({
        type: 'post',
        url: "/api/portfolios/add?" +
            "id=" + id
            + "&name=" + name
            + "&link=" + link
            + "&description=" + description,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (project) {
            var tbody = document.getElementById("tbody_projects");
            var tr = document.createElement("tr");
            tr.setAttribute('class', 'table-light');
            var th = document.createElement('th');
            var td_edit = document.createElement('td');
            var td_delete = document.createElement('td');
            var a_edit = document.createElement('a');
            var a_delete = document.createElement('a');
            th.setAttribute('scope', 'row');
            th.setAttribute('style', 'width: 60%');
            th.setAttribute('data-toggle', 'modal');
            th.setAttribute('data-target', '#portfolioModal');
            th.setAttribute('onclick', 'showPortfolio(' + project.id + ')');
            th.innerText = project.projectName;
            a_edit.setAttribute('href', '#');
            a_edit.setAttribute('class', 'text-primary');
            a_edit.setAttribute('data-toggle', 'modal');
            a_edit.setAttribute('data-target', '#update_project_modal');
            a_edit.setAttribute('onclick', 'showModalUpdatePortfolio(' + project.id + ')');
            a_edit.innerText = 'edit';
            td_edit.appendChild(a_edit);
            a_delete.setAttribute('href', '#');
            a_delete.setAttribute('class', 'text-danger');
            a_delete.setAttribute('onclick', 'showDeliteAllert(' + id + ',' + project.id + ')');
            a_delete.setAttribute('data-toggle', 'modal');
            a_delete.setAttribute('data-target', '#delete_portfolio_alert_modal');
            a_delete.text = 'delete';
            td_delete.appendChild(a_delete);
            tr.appendChild(th);
            tr.appendChild(td_edit);
            tr.appendChild(td_delete);
            tbody.appendChild(tr);
            $('#add_new_project_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })

}

function showModalUpdateTag(id) {
    $.ajax({
        url: "api/seekerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (profile) {
            var tags = profile.tags;
            tags.forEach(function (tag) {
                $('#tag_list').append('<button class="btn btn-light" id="' + tag.name + '" value="' + tag.name + '" onclick="$(this).remove()">' + tag.name + '&times;</button>&nbsp;');
            });
            $('#seeker_id_for_add_new_tags').val(id);
            getAllTags();
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    });
}

function getAllTags() {
    $.ajax({
        url: "api/tags/",
        type: "GET",
        async: false,
        success: function (tags) {
            tags.forEach(function (tag) {
                $('#tags_from_base').append('<button class="btn btn-light" value="' + tag.name + '" onclick="add_tag_to_list(this.value)">' + tag.name + '+</button>&nbsp;')
            });
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    });
}

function add_tag_to_list(val) {
    $('#tag_list').append('<button class="btn btn-light" value="' + val + '" onclick="$(this).remove()">' + val + '&times;</button>&nbsp;');
}

function add_new_tag_to_list(tag) {
    $('#tag_list').append('<button class="btn btn-light"  value="' + tag + '" onclick="$(this).remove()">' + tag + '&times;</button>&nbsp;');
}


function add_tags(tags, id) {
    var result = [];
    for (var i = 0; i < tags.length; i++) {
        result [i] = tags[i].value;
    }
    $.ajax({
        type: "POST",
        url: "/api/seekerprofiles/update?id=" + id + "&tags=" + result,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: "json",
        success: function (profile) {

            var tags = profile.tags;
            $('#profile_tags').empty();
            tags.forEach(function (tag) {
                $('#profile_tags').append('<span class="badge badge-light shadow-sm" ' +
                    '>' + tag.name + '</span>&nbsp;');
            });
            $('#update_tag_modal_close_btn').click();
            $('#tag_list').empty();
            $('#tags_from_base').empty();
        },
        error: function (errMsg) {
            alert(errMsg);
        }
    });
}


function showModalAboutMe(id) {
    $.ajax({
        url: "/api/seekerprofiles/" + id,
        type: "GET",
        async: false,
        success: function (profile) {
            $('#about_me_text_area').val(profile.description);
            $('#seeker_id_for_update_about_me').val(profile.id);
        },
        error: function (message) {
            console.log(message);
        }
    });

}

function updateAboutMe(id, description) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/update?id=" + id
            + "&description=" + description,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {
            $('#about_me').text(profile.description);
            $('#about_me_update_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })

}

function add_seeker_img(id) {
    var form = $('#fileUploadForm')[0];
    var data = new FormData(form);
    data.append("id", id);
    $("#btnSubmit").prop("disabled", true);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/api/seekerprofiles/update_image",
        data: data,
        //http://api.jquery.com/jQuery.ajax/
        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (image) {
            var profile_img = document.getElementById('profile_img');
            profile_img.innerHTML = '';
            var img = document.createElement('img');
            img.setAttribute('class', 'img-rounded');
            img.setAttribute('alt', 'Photo');
            img.setAttribute('src', 'data:image/png;base64,' + image);
            profile_img.appendChild(img);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {
            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);
        }
    });
}

function showDeliteAllert(profile_id, portfolio_id) {
    $.ajax({
        url: "/api/portfolios/" + portfolio_id,
        type: "GET",
        async: false,
        success: function (portfolio) {
            $('#name_of_portfolio_to_delete').text(portfolio.projectName);

            $('#portfolio_id_to_delete').val(portfolio.id);
            $('#profile_id_to_delete').val(profile_id);
        },
        error: function (error) {
            alert(error.responseText);
            console.log(error);
        }
    });
}

function delete_project(profile_id, portfolio_id) {
    $.ajax({
        type: 'post',
        url: "/api/portfolios/delete?profileid=" + profile_id
            + "&portfolioid=" + portfolio_id,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {

            var table = document.getElementById('table_projects');
            table.innerHTML = '';
            var tbody = document.createElement("tbody");
            var portfolios = profile.portfolios;
            for (var i = 0; i < portfolios.length; i++) {
                var tr = document.createElement("tr");
                tr.setAttribute('class', 'table-light');
                var th = document.createElement('th');
                var td_edit = document.createElement('td');
                var td_delete = document.createElement('td');
                var a_edit = document.createElement('a');
                var a_delete = document.createElement('a');
                th.setAttribute('scope', 'row');
                th.setAttribute('style', 'width: 60%');
                th.setAttribute('data-toggle', 'modal');
                th.setAttribute('data-target', '#portfolioModal');
                th.setAttribute('onclick', 'showPortfolio(' + portfolios[i].id + ')');
                th.innerText = portfolios[i].projectName;
                a_edit.setAttribute('href', '#');
                a_edit.setAttribute('class', 'text-primary');
                a_edit.setAttribute('data-toggle', 'modal');
                a_edit.setAttribute('data-target', '#update_project_modal');
                a_edit.setAttribute('onclick', 'showModalUpdatePortfolio(' + portfolios[i].id + ')');
                a_edit.innerText = 'edit';
                td_edit.appendChild(a_edit);
                a_delete.setAttribute('href', '#');
                a_delete.setAttribute('class', 'text-danger');
                a_delete.setAttribute('onclick', 'showDeliteAllert(' + profile.id + ',' + portfolios[i].id + ')');
                a_delete.setAttribute('data-toggle', 'modal');
                a_delete.setAttribute('data-target', '#delete_portfolio_alert_modal');
                a_delete.text = 'delete';
                td_delete.appendChild(a_delete);
                tr.appendChild(th);
                tr.appendChild(td_edit);
                tr.appendChild(td_delete);
                tbody.appendChild(tr);
            }
            table.appendChild(tbody);
            $('#delete_portfolio_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.responseText);
        }
    })
}

$(document).ready(function () {
    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
});
function toSubscribe(vacancyId, seekerProfileId) {

    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/toSubscribe?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#toSubscribe').remove();
            $('#btnSubscribe').append('<button type="button" id="unSubscribe" class="btn btn-outline-primary"' +
                ' onclick="unSubscribe(' + vacancyId + ',' + seekerProfileId + ')">Отписаться<i class="fas fa-envelope"></i></button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function unSubscribe(vacancyId, seekerProfileId) {

    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/unSubscribe?vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#unSubscribe').remove();
            $('#btnSubscribe').append('<button type="button" id="toSubscribe" class="btn btn-outline-primary"' +
                ' onclick="toSubscribe(' + vacancyId + ',' + seekerProfileId + ')">Подписаться<i class="fas fa-envelope"></i></button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function deleteSubscription(employerProfileId, seekerProfileId) {
    $.ajax({
        type: 'get',
        url: "/api/seekerprofiles/deleteSubscription?employerProfileId=" + employerProfileId + "&seekerProfileId=" + seekerProfileId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = '/seeker/get_subscriptions/' + seekerProfileId;
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}




