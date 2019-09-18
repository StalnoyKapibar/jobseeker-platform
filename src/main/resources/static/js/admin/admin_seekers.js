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

    $('input.chkbx_enabled').change(function(){
        var id = $(this).data("id");
        if(id === "undefined"){
            return;
        }
        var url = '/api/users/enabled/'+ id +'/'+(this.checked ? 'true' : 'false');
        $.get(url, null, function(){
            $('#alert_modal').modal('show');
        });
    });
});

$("#viewBy, #sorBy").change(function () {
    var size = $("#viewBy option:selected").val();
    var direction = $("#sorBy option:selected").val();
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

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$('#editSeekerForm').click(function (event) {
    event.preventDefault();

    var id = $('#editSeeker').find("input[name='id']").val();
    var email = $('#editSeeker').find("input[name='email']").val();
    var password = $('#editSeeker').find("input[name='password']").val();
    var date = $('#editSeeker').find("input[name='date']").val();
    var enabled = $('#editSeeker').find("input[name='enabled']").val();
    var confirm = $('#editSeeker').find("input[name='confirm']").val();

    var profId = $('#editSeeker').find("input[name='profId']").val();
    var name = $('#editSeeker').find("input[name='name']").val();
    var patronymic = $('#editSeeker').find("input[name='patronymic']").val();
    var surname = $('#editSeeker').find("input[name='surname']").val();
    var description = $('#editSeeker').find("input[name='description']").val();
    //var photo = $('#seekerPhoto img').attr('validator');

    var profile = {
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
        'enabled': enabled,
        'confirm': confirm,
        'profile': profile,
        'type': 'seeker'
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

    var seekerUserId = $('#editPhotoForm').find("input[name='seekerUserId']").val();

    var data = new FormData();

    $.each($("#editPhotoForm").find("input[type='file']"), function (i, tag) {
        $.each($(tag)[0].files, function (i, file) {
            data.append(tag.name, file);
        });
    });
    data.append('seekerUserId', seekerUserId);

    $.ajax({
        url: '/api/seeker/editPhoto',
        type: 'POST',
        contentType: false,
        processData: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: data,
        success: function (param) {
            $('#sPhoto').attr('src', 'data:image/png;base64,' + param.photo)
        }
    });
}

function deleteSeekerUserById(seekerUserId) {
    $.ajax({
        url: '/api/seeker/delete/' + seekerUserId,
        type: 'GET',
        success: function () {
            location.href = '/admin/seekers'
        }
    });
}
