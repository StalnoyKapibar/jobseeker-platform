var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    $.ajax({
        type: 'get',
        url: "/api/chats/getAllChatsByMemberId",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
			$.each(data, function (i, item) {
				printListChats(item);
			})
        },
        error: function (error) {
            console.log(error);
        }
    })
});

function printListChats(item) {
	let seekerProfileId = $('#seekerProfileId').val();
	let chatId = item.id;
	let strLength = item.lastMessageText;
	let date = messageDateFormat(item.lastMessageDate);
	let newMess = "";

	$.get("/api/chats/getBooleanReadMessage?chatId="  + chatId + "&profId=" + seekerProfileId, function (chatBoolean) {
		if (strLength.length > 32) {
			strLength = strLength.substring(0, 32) + "...";
		}

		if (chatBoolean) {
			newMess = "У вас новое сообщение";
		}

		$('#chatMessageData_' + chatId).append('' +
			'<div class="chatMessageText">' +
				'<span>' + strLength + '</span>' +
			'</div>' +
			'<div class="chatMessageDate">' +
				'<span>' + date + '</span>' +
			'</div>' +
			'<div class="chatMessageInfo">' +
				'<span>' + newMess + '</span>' +
			'</div>'
		)
	})
}
