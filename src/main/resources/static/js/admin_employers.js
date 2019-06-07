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
    location.href = '/admin/employers?size=' + size + '&direction=' + direction;
});

$("#sorBy").change(function () {
    var direction = $(this).val();
    var size = $("#viewBy option:selected").val();
    location.href = '/admin/employers?size=' + size + '&direction=' + direction;
});

$(function () {
    $('#pagList a').attr('href', function (index, href) {
        var size = $("#viewBy option:selected").val();
        var direction = $("#sorBy option:selected").val();
        return href + '&size=' + size + '&direction=' + direction;
    });
});


function editEmployer(id) {
    location.href = '/admin/employer/' + id
}

$('#editEmployerForm').click(function (event) {
    event.preventDefault();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var id = $('#editEmployer').find("input[name='id']").val();
    var email = $('#editEmployer').find("input[name='email']").val();
    var password = $('#editEmployer').find("input[name='password']").val();
    var date = $('#editEmployer').find("input[name='date']").val();
    var authorityId = $('#editEmployer').find("input[name='authorityId']").val();
    var authority = $('#editEmployer').find("input[name='authority']").val();
    var enabled = $('#editEmployer').find("input[name='enabled']").val();
    var confirm = $('#editEmployer').find("input[name='confirm']").val();


    var profId = $('#editEmployer').find("input[name='profId']").val();
    var companyName = $('#editEmployer').find("input[name='companyName']").val();
    var website = $('#editEmployer').find("input[name='website']").val();
    var description = $('#editEmployer').find("input[name='description']").val();

    var userAuthority = {
        'id': authorityId,
        'authority': authority
    };


    var employerProfile = {
        'id': profId,
        'companyName': companyName,
        'website': website,
        'description': description
    };

    var employer = {
        'id': id,
        'email': email,
        'password': password,
        'date': date,
        'authority': userAuthority,
        'enabled': enabled,
        'confirm': confirm,
        'employerProfile': employerProfile
    };

    $.ajax({
        url: '/api/employer/edit',
        contentType: 'application/json; charset=utf-8',
        type: 'POST',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: JSON.stringify(employer),
        success: function () {
            location.href = '/admin/employers'
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
});

function deleteEmployer(id) {
    $.ajax({
        url: '/api/employer/delete/' + id,
        type: 'GET',
        success: function () {
            location.href = '/admin/employers'
        }
    });
}
