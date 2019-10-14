var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var stompClient = null;
var currentChatId;
var currentProfileId;
var isNewMessages = false;

//const messageForm = document.querySelector('#messageForm');

$(document).ready(function () {
    currentChatId = parseInt($("#chatId").text());
    currentProfileId = parseInt($("#profileId").text());
    connectToServerByChatId(currentChatId);
    getAllChatMessagesByChatId(currentChatId);

//Ввод сообщения в несколько строк:
    $('.write_msg').bind('keypress', function (event) {

        //Проверка на длину сообщения:
        if (this.value.length > 119) {
            event.preventDefault();
            console.log("Сообщение максимум 120 знаков");
        }

        //Проверка на нажатие shift+enter:
        if (event.which == 13 && event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }

        //Проверка/ограничение на высоту окна ввода:
        if(this.scrollTop > 0 && this.scrollHeight < 95) {
            this.style.height = this.scrollHeight + "px";
        } else if (this.scrollTop > 1 && event.which == 13) {
            event.preventDefault();
        }
    });
});

function connectToServerByChatId(chatId) {
    var socket = new SockJS('/private_chat-messaging');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe("/user/queue/reply", onMessageReceived);
        stompClient.subscribe("/user/queue/errors", function (message) {
            alert("Error " + message.body);
        });
    }, function (error) {
        alert("STOMP error " + error);
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
}

function getAllChatMessagesByChatId(chatId) {
    $.get("/api/chats/" + chatId, function (chatMessagesList) {
        $(".msg_history").html("");
        if (chatMessagesList && chatMessagesList.length > 0) {
            chatMessagesList.reverse();
            var lastReceivedMessage = chatMessagesList[chatMessagesList.length - 1];
            $.each(chatMessagesList, function (i, message) {
                displayMessage(message);
                if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
                    message.isReadByProfilesId.push(currentProfileId);
                }
            });
            updateChatReadStatusOnServer(lastReceivedMessage, currentProfileId);
            isNewMessages = true;
            scrollMessageArea();
        }
    });
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    displayMessage(message);
    if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
        message.isReadByProfilesId.push(currentProfileId);
        updateMessageReadStatusOnServer(message, currentProfileId);
    }
    scrollMessageArea();
}

function displayMessage(message) {
    if (message.text.startsWith("/me")) {
        addSpecialMessage(message);
    } else if (message.creatorProfile === currentProfileId) {
        addYourMessage(message);
    } else {
        addForeignMessage(message);
    }

    function addSpecialMessage(message) {
        var messageLog = getMessageLog(message);
        $(".msg_history").append('<div class="special_msg text-center"><p>' + messageLog + '</p></div>')
    }

//Вывод исходящих сообщений построчно:
    function addYourMessage(message) {
        let date = messageDateFormat(message.date);
		$.get("/api/chats/evictAllCacheValues", function () {});

        $(".msg_history").append('' +
            '<div class="outgoing_msg">' +
            	'<div class="sent_msg">' +
            		'<ul id="insertUl"></ul>' +
            		'<span class="time_date">' + date + '</span>' +
            	'</div>' +
            '</div>'
		);

        let str = message.text;
        let urlLast =  $(".sent_msg:last").find('#insertUl');
        if(str.indexOf('\n') > 0) {
            let arr = str.split('\n');
            for (var i = 0; i < arr.length; i++) {
                $(urlLast).append('<li>' + arr[i] + '</li>');
            }
        } else {
            $(urlLast).append('<li>' + str + '</li>');
        }
    }

//Вывод входящих сообщений построчно:
    function addForeignMessage(message) {
        var date = messageDateFormat(message.date);
        $(".msg_history").append('' +
			'<div class="incoming_msg">' +
				'<div class="received_msg">' +
					'<div class="incoming_msg_img">' +
						'<img src="https://ptetutorials.com/images/user-profile.png" alt="sunil">' +
					'</div>' +
					'<ul id="insertIncom"></ul>' +
					'<span class="time_date">' + date + '</span>' +
				'</div>' +
            '</div>'
		);

		let str = message.text;
		let urlLastIncom =  $(".incoming_msg:last").find('#insertIncom');
		if(str.indexOf('\n') > 0) {
			let arr = str.split('\n');
			for (var i = 0; i < arr.length; i++) {
				$(urlLastIncom).append('<li>' + arr[i] + '</li>');
			}
		} else {
			$(urlLastIncom).append('<li>' + str + '</li>');
		}
    }

}

function checkIsMessageWasReadByProfileId(message, readerProfileId) {
    if (readerProfileId === message.creatorProfile.id) {
        return true
    }
    for (var i = 0; i < message.isReadByProfilesId.length; i++) {
        if (message.isReadByProfilesId[i] === readerProfileId) {
            return true;
        }
    }
    return false;
}

function getMessageLog(message) {
    var replaced = message.text.replace("/me ", "");
    if (replaced === "CONFIRMED") {
        if (isNewMessages) {
            updateButtons(message, replaced);
        }
        return "Работодатель утвердил собеседование";
    } else if (replaced === "CONFIRMED_BY_USER") {
        if (isNewMessages) {
            updateButtons(message, replaced);
        }
        return "Пользователь подтвердил дату";
    } else {
        if (isNewMessages) {
            updateButtons(message, replaced);
        }
        return "Работодатель назначил дату встречи на <br>" + replaced.replace("SCHEDUALED", "");
    }
}

function sendMessage() {
    $(".write_msg").css({height: "55px"});
    var messageContent = $(".write_msg").val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            creatorProfileId: currentProfileId,
            text: messageContent
        };
        stompClient.send("/live/private_chat/" + currentChatId, {}, JSON.stringify(chatMessage));
        $(".write_msg").val("");
    }
}

function updateChatReadStatusOnServer(lastReadMessage, readerProfileId) {
    var sendData = {
        chatId: currentChatId,
        messageId: lastReadMessage.id,
        readerProfileId: readerProfileId
    };

    $.ajax({
        url: "/api/chats/set_chat_read_by_profile_id",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(sendData),
        //dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader(header, token);
        }
    })
}

function updateMessageReadStatusOnServer(message, readerProfileId) {
    var sendData = {
        chatId: currentChatId,
        messageId: message.id,
        readerProfileId: readerProfileId
    };

    $.ajax({
        url: "/api/chats/set_message_read_by_profile_id",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(sendData),
        //dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader(header, token);
        }
    })
}

function scrollMessageArea() {
    $container = $('.msg_history');
    $container[0].scrollTop = $container[0].scrollHeight;
}