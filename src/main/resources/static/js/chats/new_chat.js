let stompClient = null;

let currentChatId;
let currentProfileId;

//let currentMeetingId;
//let meeting;

let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

let isNewMessages = false;

$(async function () {
    currentChatId = parseInt($("#chatId").text());
    currentProfileId = parseInt($("#profileId").text());
    //currentMeetingId = parseInt($("#meetingId").text());
    //await getMeeting(currentMeetingId);
    connectToServerByChatId(currentChatId);
    getAllChatMessagesByChatId(currentChatId);
});

function connectToServerByChatId(chatId) {
    const socket = new SockJS('/chat-messaging');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe("/topic/chat/" + chatId, onMessageReceived)
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

            const lastReceivedMessage = chatMessagesList[chatMessagesList.length - 1];

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
    let message = JSON.parse(payload.body);

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
        let messageLog = getMessageLog(message);
        $(".msg_history").append('<div class="special_msg text-center"><p>' + messageLog + '</p></div>')
    }

    function addYourMessage(message) {
        let date = messageDateFormat(message.date);
        $(".msg_history").append('<div class="outgoing_msg">\n' +
            '                            <div class="sent_msg">\n' +
            '                                <p>' + message.text + '</p>\n' +
            '                                <span class="time_date">' + date + '</span> </div>\n' +
            '                        </div>');
    }

    function addForeignMessage(message) {
        let date = messageDateFormat(message.date);
        $(".msg_history").append('<div class="incoming_msg">\n' +
            '                            <div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>\n' +
            '                            <div class="received_msg">\n' +
            '                                <div class="received_withd_msg">\n' +
            '                                    <p>' + message.text + '</p>\n' +
            '                                    <span class="time_date">' + date + '</span></div>\n' +
            '                            </div>\n' +
            '                        </div>');
    }
}

function checkIsMessageWasReadByProfileId(message, readerProfileId) {

    if (readerProfileId === message.creatorProfile.id) {
        return true
    }

    for (let i = 0; i < message.isReadByProfilesId.length; i++) {
        if (message.isReadByProfilesId[i] === readerProfileId) {
            return true;
        }
    }

    return false;
}

function getMessageLog(message) {
    let replaced = message.text.replace("/me ", "");
    if (replaced === "CONFIRMED") {
        if (isNewMessages) updateButtons(message, replaced);
        return "Работодатель утвердил собеседование";
    } else if (replaced === "CONFIRMED_BY_USER") {
        if (isNewMessages) updateButtons(message, replaced);
        return "Пользователь подтвердил дату";
    } else {
        if (isNewMessages) updateButtons(message, replaced);
        return "Работодатель назначил дату встречи на <br>" + replaced.replace("SCHEDUALED", "");
    }
}

function sendMessageButton() {
    const messageContent = $(".write_msg");
    if (messageContent) {
        sendMessageToServer(messageContent.val().trim());
        messageContent.val("");
    }
}

function sendMessageToServer(message) {
    if (stompClient) {
        const chatMessage = {
            creatorProfileId: currentProfileId,
            text: message
        };
        stompClient.send("/live/chat/" + currentChatId, {}, JSON.stringify(chatMessage));
    }
}

function updateChatReadStatusOnServer(lastReadMessage, readerProfileId) {
    const sendData = {
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
    const sendData = {
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