let stompClient = null;

const currentChatId = parseInt(document.getElementById("chatId").getAttribute('value'));
const currentProfileId = parseInt(document.getElementById("profileId").getAttribute('value'));

const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');

$(function () {

    connectToServerByChatId(currentChatId);

    getAllChatMessagesByChatId(currentChatId);

    messageForm.addEventListener('submit', sendMessage, true);
});

function connectToServerByChatId(chatId) {
    const socket = new SockJS('/chat');
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

        chatMessagesList.reverse();
        $(".media-list").html("");

        let lastReceivedMessage = chatMessagesList[chatMessagesList.length - 1];

        $.each(chatMessagesList, function (i, message) {
            addMessageToMessageArea(message);

            if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
                message.isReadByProfilesId.push(currentProfileId);

                if (message === lastReceivedMessage) {
                    updateChatReadStatusOnServer(message, currentProfileId);
                }
            }
        });

        scrollMessageArea();
    });
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    addMessageToMessageArea(message);

    if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
        message.isReadByProfilesId.push(currentProfileId);
        updateMessageReadStatusOnServer(message, currentProfileId);
    }

    scrollMessageArea();
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

function addMessageToMessageArea(message) {
    $(".media-list").append('<li class="media">' +
        '<div class="media-body">' +
        '<div class="media">' +
        '<div class="media-body"><small>'
        + message.text +
        '</small><br/><small class="text-muted">'
        + message.creatorProfile +
        '</small><hr/></div></div></div></li>');
}


function sendMessage(event) {
    scrollMessageArea();

    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            creatorProfileId: currentProfileId,
            text: messageContent
        };

        stompClient.send("/live/chat/" + currentChatId, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }

    event.preventDefault();
}

function updateChatReadStatusOnServer(lastReadMessage, readerProfileId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    const sendData = {
        chatId: currentChatId,
        lastReadMessageId: lastReadMessage.id,
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
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    const sendData = {
        chatId: currentChatId,
        lastReadMessageId: message.id,
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
    // $container = $('.message-area');
    // $container[0].scrollTop = $container[0].scrollHeight;
    //$(".message-area").scrollTop = messages.scrollHeight;
}