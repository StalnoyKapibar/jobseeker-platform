// $(function () {
//     $('#allVacancies').sortable();
//
//     // $(" #trckVacancies, #allVacancies").sortable({
//     //     connectWith: "#trckVacancies, #allVacancies",
//     //     update: function() {
//     //         var sort = [];
//     //         $('#trckVacancies').each(function () {
//     //             sort.push($(this).text());
//     //         });
//     //         var saveSort = {'sort':sort};
//     //         sessionStorage.setItem('sort', JSON.stringify(saveSort));
//     //     }
//     // });
// });
//





$( document ).ready(function() {
    $("#trckVacancies, #allVacancies").sortable({
        connectWith: "#trckVacancies, #allVacancies",
        update: function() {
            var vacancies = [];
            $('#trckVacancies li').each(function () {
                    vacancies.push({'position':$(this).index()+1, 'id':$(this).find('span').data('id')});
            });
            var saveSort = {'vacancies':vacancies};
            sessionStorage.setItem('vacancies', JSON.stringify(saveSort));
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

}

function add_emploer_img(id) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");



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
