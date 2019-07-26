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
});


function validateAndPreview() {
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
        url: "/api/news/add?employerProfileId=" + employerProfileId,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(news),
        success: function () {
            $("#n_headline").val('');
            $('.note-editable').text('');
            alert('Новость добавлена');
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}