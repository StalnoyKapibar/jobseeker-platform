var stompClient = null;

var currentChatId;
var currentProfileId;
var profiles = [];

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var isNewMessages = false;

//const messageForm = document.querySelector('#messageForm');

$(document).ready(function () {
    currentChatId = $("#chatId").text();
    currentProfileId = $("#sreatorProfileId").text();
    profiles = $(".usersId").each(function (key, value) {
        profiles.push($(this).text());
    });
    connectToServerByChatId(currentChatId);
    getAllChatMessagesByChatId(currentChatId);
});

function connectToServerByChatId(chatId) {
    var socket = new SockJS('/chat-messaging');
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
        chatMessagesList.reverse();
        $(".msg_history").html("");
        var lastReceivedMessage = chatMessagesList[chatMessagesList.length - 1];
        $.each(chatMessagesList, function (i, message) {
            pickDisplayStrategy(message);
            if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
                //message.isReadByProfilesId.push(currentProfileId);
                if (message === lastReceivedMessage) {
                    updateChatReadStatusOnServer(message, currentProfileId);
                }
            }
        });
        isNewMessages = true;
        scrollMessageArea();
    });
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    pickDisplayStrategy(message);
    if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
        //message.isReadByProfilesId.push(currentProfileId);
        updateMessageReadStatusOnServer(message, currentProfileId);
    }
    scrollMessageArea();
}

function pickDisplayStrategy(message) {
    if (message.creatorProfile === currentProfileId) {
        addYourMessage(message);
    } else {
        addForeignMessage(message);
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

function addYourMessage(message) {
    var date = messageDateFormat(message.date);
    $(".msg_history").append('<div class="outgoing_msg">\n' +
        '                            <div class="sent_msg">\n' +
        '                                <p>' + message.text + '</p>\n' +
        '                                <span class="time_date">' + date + '</span> </div>\n' +
        '                        </div>');
}

function addForeignMessage(message) {
    var date = messageDateFormat(message.date);
    $(".msg_history").append('<div class="incoming_msg">\n' +
        '                            <div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>\n' +
        '                            <div class="received_msg">\n' +
        '                                <div class="received_withd_msg">\n' +
        '                                    <p>' + message.text + '</p>\n' +
        '                                    <span class="time_date">' + date + '</span></div>\n' +
        '                            </div>\n' +
        '                        </div>');
}

function sendMessage() {
    //scrollMessageArea();
    //messageArea.animate({scrollTop: $container[0].scrollHeight}, "slow"); //todo (Nick Dolgopolov)

    var messageContent = $(".write_msg").val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            creatorProfileId: currentProfileId,
            text: messageContent
        };
        stompClient.send("/live/private_chat/" + currentChatId, {}, JSON.stringify(chatMessage));
        $(".write_msg").val("");
    }

    //event.preventDefault();
}

function updateChatReadStatusOnServer(lastReadMessage, readerProfileId) {
    var sendData = {
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
    var sendData = {
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
    $container = $('.msg_history');
    $container[0].scrollTop = $container[0].scrollHeight;
}