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
            createChat(vacancyId,seekerId);
        },
        error: function (error) {
            console.log(error);
            alert("function sendRespond ERROR " + error.toString());
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
    });
    sendMessageToServer("/me " + status + dateFormat(meeting.date));
}

async function editMeeting(id) {
    await getMeeting(id);
    $('#meetingModal').modal('toggle');
}

function getMeeting(id) {
    $.ajax({
        url: '/api/meetings/' + id,
        type: 'get',
        async: false,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            meeting = data;
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
    sendMessageToServer("/me " + status);
}


function createChat(vacancyId, seekerId) {
    $.ajax({
        type: 'post',
        url: "/api/chats?vacancyId=" + vacancyId + "&seekerId=" + seekerId,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#chat_alert").removeClass("d-none");
            $("#chat_ref").attr("href","/chat/seeker-vacancy-employer/" + data);
        },
        error: function (error) {
            console.log(error);
            alert("function createChat ERROR " + error.toString());
        }
    })
}

function confirmMeeting(id){
    let status = "CONFIRMED_BY_USER";
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
    sendMessageToServer("/me " + status);
}

function updateButtons(message, replaced){
    let buttonBlock = $(".text-center.mb-2.mr-2.mt-1");
    if(replaced === "CONFIRMED") {
        buttonBlock.empty();
        buttonBlock.append('<button type="button" disabled="disabled"\n' +
            '                        class="btn btn-success disabled">Встреча назначена</button>');
    }else if(replaced === "CONFIRMED_BY_USER") {
        if(message.creatorProfile === currentProfileId){
            buttonBlock.empty();
            buttonBlock.append('<button type="button" disabled="disabled" ' +
                '                       class="btn btn-warning disabled">Ожидание подверждения</button>')
        }else{
            buttonBlock.empty();
            buttonBlock.append('<button onclick="updateMeeting(' + currentMeetingId +')" type="button"' +
                '                        class="btn btn-dark">Подтвердить встречу</button>')
        }
    } else {
        if (message.creatorProfile === currentProfileId) {
            buttonBlock.empty();
            buttonBlock.append('<button type="button" disabled="disabled"\n' +
                '                        class="btn btn-warning disabled">Ожидание подверждения</button>');
        } else {
            buttonBlock.empty();
            buttonBlock.append('<button onclick="confirmMeeting(' + currentMeetingId + ')" ' +
                '                           type="button" class="btn btn-dark">Подтвердить</button>');
        }
    }
}
