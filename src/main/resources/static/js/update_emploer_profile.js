var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$( document ).ready(function() {
    $("#trckVacancies, #allVacancies").sortable({
        connectWith: "#trckVacancies, #allVacancies",
        update: function() {
            var vacancies = [];
            $('#trckVacancies li').each(function () {
                    vacancies.push({'position':$(this).index()+1, 'id':$(this).find('span').data('id')});
            });
            sessionStorage.setItem('vacancies', JSON.stringify(vacancies));
        }
    });

    $(".custom-file-input").on("change", function() {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });

});

function update(id) {
    var profile = {
        'profile':
            {'id':id,
            'companyname':document.getElementById("companyname").value,
            'site':document.getElementById("companywebsite").value,
            'description':document.getElementById("description").value,
            },
        'vacancies': JSON.parse(sessionStorage.getItem('vacancies'))};
    $.ajax({
        type: 'post',
        url: "/api/employerprofiles/update",
        data: JSON.stringify(profile)
        ,
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

function add_emploer_img(id) {
    var form = $('#imageUploadForm')[0];
    var data = new FormData(form);
    data.append("id", id);
    $("#btnSubmit").prop("disabled", true);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/api/employerprofiles/update_image",
        data:data,
        processData: false,
        contentType: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (image) {
            $('#logo').html('<img src="data:image/png;base64,'+image+'" class="img-rounded"' +
                '                                     alt="Logo of company">');
            $("#btnSubmit").prop("disabled", false);
        },
        error: function (e) {
            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);
        }
    });
}

function show_delete_vacancy_alert(vacancyId) {
    $('#vac_name_to_del').text($('[data-id = '+vacancyId+']').text());
    $('#del_vac_id').val(vacancyId);
    $('#del_vac_allert').modal('show');
}

function delete_vacancy(){
        $.ajax({
            type: 'post',
            url: "/api/vacancies/delete",
            data: JSON.stringify({'id':$('#del_vac_id').val()})
            ,
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            success: function (vacancies) {
                $('#'+$('#del_vac_id').val()+'_li').remove();
                $('#del_vac_allert').modal('hide');
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
                $('#'+vacancyId+'_del_btn').setAttribute('onclick', '');
            }
        })
}
