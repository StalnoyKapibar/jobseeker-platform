$(document).ready(function () {

    var role = $("meta[name='user_role']").attr("content");
    var useremail = $("meta[name='user_email_head']").attr("content");

    if (role == "[ROLE_SEEKER]") {
        $.ajax({
            url: '/api/seekerprofiles/email/' + useremail,
            type: 'GET',
            success: function (profilename) {
                $('#seekerProfileName').text(profilename);
                if (profilename == useremail) {
                    var answer = confirm("Профиль не найден, хотите создать сейчас?");
                    if (answer) {

                    }
                }
            }
        });
    }

    if (role == "[ROLE_EMPLOYER]") {
        $.ajax({
            url: '/api/employerprofiles/email/' + useremail,
            type: 'GET',
            success: function (profilename) {
                $('#employerProfileName').text(profilename);
                if (profilename == useremail) {
                    var answer = confirm("Профиль не найден, хотите создать сейчас?");
                    if (answer) {

                    } else {
                        return;
                    }
                }
            }
        });
    }
});