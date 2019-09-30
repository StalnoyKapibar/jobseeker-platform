$(document).ready(function(){
    $("body").on("click",".btn-danger", function(){
        $(this).parents("tr").hide();
    });

    $("body").on("click",".btn-primary", function(){
        $(this).hide();
    });

    $("body").on("click", "#btnCreateNewTag", function () {
        let nameNewTag = $("#textNewTag").val();
        if (!nameNewTag) return;
        createNewTag(nameNewTag);
    })
});

function createNewTag(nameNewTag) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let tagCheck = document.getElementById("tagCheck");
    let newDataTag = {'name': nameNewTag, 'verified': tagCheck.checked};

    $.ajax({
        url: "/api/tags/createNewTagController/",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        method: "POST",
        data: JSON.stringify(newDataTag),
        success: function () {
            console.log('Новый тэг ' + nameNewTag + ' успешно записался');
        },
        error: function (error) {
            console.log("Ошибка записи нового тэга " + error.toString());
        }
    });
}

function deleteTag(userId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url: "/api/tags/" + userId,
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
        type: "DELETE",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}

function changeVerifiedTag(userId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        url: "/api/tags/change_verified/" + userId,
        dataType: "json",
        type: "PUT",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data, status, jqXHR) {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}