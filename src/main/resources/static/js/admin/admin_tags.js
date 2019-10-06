$(document).ready(function () {
    $("body").on("click", ".btn-primary", function () {
        $(this).hide();
    });

    $("#btnCreateNewTag").click(function (event) {
        event.preventDefault();
        let nameNewTag = $("#textNewTag").val();
        if (!nameNewTag) {
            return;
        }
        createNewTag(nameNewTag);
    });

    $(".btn.btn-danger").click(function () {
        $("#modalBodyRemoveTag").text($(this).data("name"))
        $("#modalBodyRemoveTag").data("id", $(this).data("id"));
    });
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
            window.location.reload();
            console.log('Новый тэг ' + nameNewTag + ' успешно записался');
        },
        error: function (error) {
            $('#alert_modal').modal('show');
            console.log("Ошибка записи нового тэга " + error.toString());
        }
    });
}

$(document).on('show.bs.modal', '#tagRemovalModal', function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let tagId = $("#modalBodyRemoveTag").data("id");
    $('#btnSuccess').click(function () {
        $.ajax({
            url: "/api/tags/" + tagId,
            contentType: 'application/json; charset=utf-8',
            dataType: "json",
            type: "DELETE",
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            success: function () {
                $("#tagRemovalModal").modal("hide");
                window.location.reload();
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
            }
        });
    })
});

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
            window.location.reload();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}