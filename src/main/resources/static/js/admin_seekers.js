$(document).ready(function () {
    'use strict';
    feather.replace();
    $('#viewBy option').each(function () {
        var param = $(this);
        if (location.href.indexOf(param.val()) !== -1) {
            param.prop('selected', true);
        }
    });
    $('#sorBy option').each(function () {
        var param = $(this);
        if (location.href.indexOf(param.val()) !== -1) {
            param.prop('selected', true);
        }
    });

});

$("#viewBy").change(function () {
    var size = $(this).val();
    var direction = $("#sorBy option:selected").val();
    location.href = '/admin/seekers?size=' + size + '&direction=' + direction;
});

$("#sorBy").change(function () {
    var direction = $(this).val();
    var size = $("#viewBy option:selected").val();
    location.href = '/admin/seekers?size=' + size + '&direction=' + direction;
});

$(function () {
    $('#pagList a').attr('href', function (index, href) {
        var size = $("#viewBy option:selected").val();
        var direction = $("#sorBy option:selected").val();
        return href + '&size=' + size + '&direction=' + direction;
    });
});


function editSeeker(id) {
    location.href = '/admin/seeker/' + id
}

$('#editSeekerForm').click(function (event) {
    event.preventDefault();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var id = $('#editSeeker').find("input[name='id']").val();
    var email = $('#editSeeker').find("input[name='email']").val();
    var password = $('#editSeeker').find("input[name='password']").val();
    var date = $('#editSeeker').find("input[name='date']").val();
    var authorityId = $('#editSeeker').find("input[name='authorityId']").val();
    var authority = $('#editSeeker').find("input[name='authority']").val();
    var enabled = $('#editSeeker').find("input[name='enabled']").val();
    var confirm = $('#editSeeker').find("input[name='confirm']").val();


    var profId = $('#editSeeker').find("input[name='profId']").val();
    var name = $('#editSeeker').find("input[name='name']").val();
    var patronymic = $('#editSeeker').find("input[name='patronymic']").val();
    var surname = $('#editSeeker').find("input[name='surname']").val();
    var description = $('#editSeeker').find("input[name='description']").val();
    //var photo = $('#seekerPhoto img').attr('src');

    var userAuthority = {
        'id': authorityId,
        'authority': authority
    };


    var seekerProfile = {
        'id': profId,
        'name': name,
        'patronymic': patronymic,
        'surname': surname,
        'description': description
    };

    var seeker = {
        'id': id,
        'email': email,
        'password': password,
        'date': date,
        'authority': userAuthority,
        'enabled': enabled,
        'confirm': confirm,
        'seekerProfile': seekerProfile
    };

    $.ajax({
        url: '/api/seeker/edit',
        contentType: 'application/json; charset=utf-8',
        type: 'POST',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(seeker),
        success: function () {
            location.href = '/admin/seekers'
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
});

function uploadPhoto() {

    var id = $('#editPhotoForm').find("input[name='seekerId']").val();
    var photo = $('#editPhotoForm').find("input[name='file']").val();

    $.ajax({
        url: '/api/seeker/editPhoto',
        type: 'POST',
        data: {photo: photo},
        success: function () {
            location.href = '/admin/seeker/' + id
        }
    });
}

/*function editPhoto(id) {

}*/

function deleteSeeker(id) {
    $.ajax({
        url: '/api/seeker/delete/' + id,
        type: 'GET',
        success: function () {
            location.href = '/admin/seekers'
        }
    });
}
