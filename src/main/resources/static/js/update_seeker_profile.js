var tagsName = [];
var avelibleTags =[{}];
$( document ).ready(function() {

    $.ajax({
        url: "/api/tags/",
        type: "GET",
        async: false,
        success: function (tags) {
            for(var i = 0; i<tags.length; i++){
                tagsName[i] = tags[i].name;
                avelibleTags[i] = tags[i];
            }
        }
    });

    $( "#tags_input" ).autocomplete({
        source: tagsName
    });

    $(".custom-file-input").on("change", function() {
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
        url: "/api/employerprofiles/update_image", //TODO поменять на /api/seekerprofile/update_image, написать контроллер
        data:data,
        processData: false,
        contentType: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (image) {
        },
        error: function (e) {
            console.log("ERROR : ", e.responseText);
            $("#btnSubmit").prop("disabled", false);
        }
    });
}

function show_portfolio(id){
    $.ajax({
        url: "/api/portfolios/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            $("#portf_name_input").val(data.projectName);
            $("#portf_link_input").val(data.link);
            $("#portf_description_textarea").text(data.description);
            $("#portfolioModal").modal('show');
        }
    });
}

function add_new_tag() {
    var newTag= $("#tags_input").val();
    var newTagsToUpdate = [];

    if(tagsName.indexOf(newTag)!==-1){
        $('#tag_list').append('<span class="badge badge-pill badge-success btnClick text-dark" onclick="this.remove()">' +
            '<span>'+newTag+'</span> &times;</span>');

        $('#tag_list span').each(function () {
            var tagFromList = $(this).find('span').text();
            var result = $.grep(avelibleTags, function(e){ return e.name == tagFromList; });
            newTagsToUpdate.push(result);
        });
        sessionStorage.setItem('tags', JSON.stringify(newTagsToUpdate));
        alert(JSON.stringify(JSON.parse(sessionStorage.getItem('tags'))));
        // $('#tag_list span').each(function () {
        //         $(this).find('span').text();
        //         if()
        //     })
        }
    // $('#tag_list span').each(function () {
    //     alert($(this).find('span').text());
    // })

    }







