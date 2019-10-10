var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    var employerProfileId = $('#employerProfileId').val();
    $.ajax({
        type: 'get',
        url: "/api/chats/getAllChatsByMemberId",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            $.each(data, function (i, item) {
                getLastMessage(item.id);
            })
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
});

function getLastMessage(chatId) {
    $.get("/api/chats/" + chatId, function (chatMessagesList) {
        if (chatMessagesList && chatMessagesList.length > 0) {
            let lastReceivedMessage = chatMessagesList[0];
			let date = messageDateFormat(lastReceivedMessage.date);
			let employerProfileId = $('#employerProfileId').val();
			let profileId = lastReceivedMessage.creatorProfile;
			let newMess = "";

			if (chatMessagesList[0].isReadByProfilesId.length == 1 && profileId != employerProfileId) {
				newMess = "Новое сообщение";
			}

            $('#chatMessageData_' + chatId).append('' +
				'<div class="chatMessageText">' +
					'<span>' + lastReceivedMessage.text + '</span>' +
				'</div>' +
                '<div class="chatMessageDate">' +
					'<span>' + date + '</span>' +
				'</div>' +
				'<div class="chatMessageInfo">' +
					'<span>' + newMess + '</span>' +
				'</div>'
			)
        }
    })
};
