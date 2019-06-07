$(document).ready(function () {
    getSeekers();
});

function getSeekers() {
    $.ajax({
        url: '/api/seekerprofiles',
        type: 'GET',
        success: function (list) {
            var trHTML = '';
            $.each(list, function (i, item) {
                trHTML += '<tr>' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.email + '</td>' +
                    '<button onclick="viewSeeker(' + item.seekerProfile.id + ')" data-toggle="modal" data-target="#seekerProfileModal" class="btn btn-primary seekerProfileModal">View Profile</button> ' +
                    '<td>' + printTags(item.roles) + '</td>' +
                    '<td>' + printPortfolios(item.roles) + '</td>' +
                    '<td>' +
                    '<button onclick="editSeeker(' + item.id + ')" data-toggle="modal" data-target="#editSeekerModal" class="btn btn-primary editSeekerModal">Edit User</button> ' +
                    '<button onclick="editSeekerProfile(' + item.seekerProfile.id + ')"  data-toggle="modal" data-target="#editSeekerProfileModal" class="btn btn-primary editSeekerProfileModal">Edit Profile</button>' +
                    '</td>' +
                    '</tr>';
            });

            $('tbody').html(trHTML);
        }
    });
}

function viewSeeker(id) {
    $.ajax({
        url: '/api/seekerprofiles/' + id,
        type: 'GET',
        success: function (seeker) {
            var id = seeker.id;
            var name = seeker.name;
            var patronymic = seeker.patronymic;
            var surname = seeker.surname;
            var description = seeker.description;
            var photo = seeker.photo;

            $("#seekerProfileModal").find("input[name='Id']").val(id);
            $("#seekerProfileModal").find("input[name='Name']").val(name);
            $("#seekerProfileModal").find("input[name='Patronymic']").val(patronymic);
            $("#seekerProfileModal").find("input[name='Surname']").val(surname);
            $("#seekerProfileModal").find("input[name='Description']").val(description);
            $("#seekerProfileModal").find("input[name='Photo']").val(photo);
        }
    });
}