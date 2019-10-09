var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(function () {
    $('#search_box').bind("input keyup click", function () {
        if (this.value === '') {
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
                    if (data.length === 0) {
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


function myFunction() {
    var x = document.getElementById("myDIV");
    if (x.style.display === "none") {
        x.style.display = "block";
        showTags()
    } else {
        x.style.display = "none";
        document.getElementById("tagsWell").innerHTML = "";
        document.getElementById("selectedTags").innerHTML = "";
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
        success: function () {
            alert("Ваше приглашение было отправленно");
            $('#inviteFriendModal').modal('hide');
        }
    })
}

function inFavorite(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/inFavoriteVacancy",
        data: "seekerProfileId=" + seekerProfileId + "&vacancyId=" + vacancyId,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
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
        url: "/api/seekerprofiles/outFavoriteVacancy",
        data: "seekerProfileId=" + seekerProfileId + "&vacancyId=" + vacancyId,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
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

function toSubscribe(vacancyId, seekerProfileId) {
    var div = document.getElementById("selectedTags");
    var nodelist = div.getElementsByTagName("label").length;
    if (nodelist < 1) {
        alert('Выберите как минимум 1 тег!');
    } else {
        var tags = $("#selectedTags").find("span").map(function () {
            return this.innerText;
        }).get();

        $.ajax({
            type: 'post',
            url: "/api/seekerprofiles/toSubscribe",
            data: "vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId + "&tags=" + tags,
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            success: function () {
                $('#toSubscribe').remove();
                $('#btnSubscribe').append('<button type="button" id="unSubscribe" class="btn btn-outline-primary"' +
                    ' onclick="unSubscribe(' + vacancyId + ',' + seekerProfileId + ')">Отписаться<i class="fas fa-envelope"></i></button>');
                var x = document.getElementById("myDIV");
                x.style.display = "none";
                document.getElementById("tagsWell").innerHTML = "";
                document.getElementById("selectedTags").innerHTML = "";
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
            }
        })
    }
}

function unSubscribe(vacancyId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/unSubscribe",
        data: "vacancyId=" + vacancyId + "&seekerProfileId=" + seekerProfileId,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#unSubscribe').remove();
            $('#btnSubscribe').append('<button type="button" id="toSubscribe" class="btn btn-outline-primary"' +
                ' onclick="myFunction()">Подписаться<i class="fas fa-envelope"></i></button>');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function deleteSubscription(employerProfileId, seekerProfileId) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/deleteSubscription",
        data: "employerProfileId=" + employerProfileId + "&seekerProfileId=" + seekerProfileId,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = '/seeker/get_subscriptions/';
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function removeTag(name) {
    $.ajax({
        type: 'post',
        url: "/api/seekerprofiles/removeTag",
        data: "tag=" + name,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

$(document).on('show.bs.modal', '#selectionTagsModal', function () {
    $('#btnSuccess').click(function () {
        let btnTags = $(`.btn.btn-success.btn-sm.badge.badge-pill.text-dark.active`);
        let tags = [];
        if (btnTags.length !== 0) {
            for (let tag of btnTags) {
                tags.push(tag.textContent.toString());
            }
            $.ajax({
                type: 'post',
                url: "/api/seekerprofiles/updateUserTags?updatedTags=" + tags,
                contentType: 'application/json; charset=utf-8',
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                success: function () {
                    location.reload();
                },
                error: function (error) {
                    console.log(error);
                }
            })
        }
    })
});
