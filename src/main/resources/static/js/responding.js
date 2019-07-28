let meeting;

function sendRespond(vacancyId, seekerId) {
    $.ajax({
        type: 'post',
        url: "/api/meetings?vacancyId=" + vacancyId + "&seekerId=" + seekerId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            $('#respond').removeClass();
            $('#respond').addClass("btn btn-warning disabled");
            $('#respond').attr("disabled","disabled");
            $('#respond').text("В ожидании");

        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function sendMeetingEdition() {
    let dateTime = $("#dateTimeLocal").val();
    let status = "SCHEDUALED";
    meeting.date = dateTime;
    meeting.status = status;
    $.ajax({
        type: 'put',
        url: "/api/meetings/" + meeting.id,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(meeting),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}

function editMeeting(id) {
    $.ajax({
        url: '/api/meetings/' + id,
        type: 'get',
        async: false,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            meeting = data;
            console.log(meeting);
            $('#meetingModal').modal('toggle');
        }
    });
}

function updateMeeting(id) {
    let status = "CONFIRMED";
    $.ajax({
        type: 'patch',
        url: "/api/meetings/" + id,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(status),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}