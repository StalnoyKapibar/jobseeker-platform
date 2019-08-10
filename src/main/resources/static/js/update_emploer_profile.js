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
            // var saveSort = {vacancies};
            // sessionStorage.setItem('vacancies', JSON.stringify(vacancies));
            sessionStorage.setItem('vacancies', JSON.stringify(vacancies));

        }
    });

    $(".custom-file-input").on("change", function() {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });

});

function update(id) {
    var companyName = document.getElementById("companyname").value;
    var site = document.getElementById("companywebsite").value;
    var description = document.getElementById("description").value;
    var vacansies = JSON.parse(sessionStorage.getItem('vacancies'));
    var vac = {'publicationPosition': 2,
        'id': 4};

    var obj = {
        'profile':
            {'id':id,
            'companyname':companyName,
            'site':site,
            'description':description,
            },
        'vacancies': vacansies};


    // var profile = {
    //     'id': id,
    //     'companyName': companyName,
    //     'website': site,
    //     'description': description,
    //
    //
    // }


    $.ajax({
        type: 'post',
        // url: "/api/employerprofiles/update?" +
        //     "id=" + id +
        //     "companyname="+companyName+
        //     "site="+site+
        //     "description="+description+
        //     "vacs="+JSON.stringify(vac),
        url: "/api/employerprofiles/update",
        data: JSON.stringify(obj)
        ,
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (profile) {
            $('#company_name').text(profile.companyName);
            $('#update_modal_close_btn').click();
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })

}



function add_emploer_img(id) {
    var form = $('#fileUploadForm')[0];
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

            var profile_img = document.getElementById('profile_img');
            profile_img.innerHTML = '';
            var img = document.createElement('img');
            img.setAttribute('class', 'img-rounded');
            img.setAttribute('alt', 'Photo');
            img.setAttribute('src', 'data:image/png;base64,'+image);
            profile_img.appendChild(img);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {

            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}
