let stompClient = null;

let currentChatId;
let currentProfileId;

//const messageForm = document.querySelector('#messageForm');
const messageInput = $(".write_msg");

$(function () {
    currentChatId = parseInt($("#chatId").text());
    currentProfileId = parseInt($("#userId").text());

    connectToServerByChatId(currentChatId);

    //getAllChatMessagesByChatId(currentChatId);

    //messageForm.addEventListener('submit', sendMessage, true);
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
        console.log(chatMessagesList);
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

        //scrollMessageArea();
    });
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    if(message.creatorProfile===currentProfileId){
        addYourMessage(message);
    }else {
        addForeignMessage(message);
    }

    // if (!checkIsMessageWasReadByProfileId(message, currentProfileId)) {
    //     message.isReadByProfilesId.push(currentProfileId);
    //     updateMessageReadStatusOnServer(message, currentProfileId);
    // }

    //scrollMessageArea();
}

// function checkIsMessageWasReadByProfileId(message, readerProfileId) {
//
//     if (readerProfileId === message.creatorProfile.id) {
//         return true
//     }
//
//     for (let i = 0; i < message.isReadByProfilesId.length; i++) {
//         if (message.isReadByProfilesId[i] === readerProfileId) {
//             return true;
//         }
//     }
//
//     return false;
// }

function addYourMessage(message) {
    $(".msg_history").append('<div class="outgoing_msg">\n' +
        '                            <div class="sent_msg">\n' +
        '                                <p>'+ message.text +'</p>\n' +
        '                                <span class="time_date"> 11:01 AM    |    June 9</span> </div>\n' +
        '                        </div>');
}

function addForeignMessage(message) {
    $(".msg_history").append('<div class="incoming_msg">\n' +
        '                            <div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>\n' +
        '                            <div class="received_msg">\n' +
        '                                <div class="received_withd_msg">\n' +
        '                                    <p>'+ message.text +'</p>\n' +
        '                                    <span class="time_date"> 11:01 AM    |    June 9</span></div>\n' +
        '                            </div>\n' +
        '                        </div>');
}

function sendMessage() {
    //scrollMessageArea();
    //messageArea.animate({scrollTop: $container[0].scrollHeight}, "slow"); //todo (Nick Dolgopolov)

    const messageContent = $(".write_msg").val().trim();

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

//todo Matvey
// function updateChatReadStatusOnServer(lastReadMessage, readerProfileId) {
//     let token = $("meta[name='_csrf']").attr("content");
//     let header = $("meta[name='_csrf_header']").attr("content");
//
//     const sendData = {
//         chatId: currentChatId,
//         lastReadMessageId: lastReadMessage.id,
//         readerProfileId: readerProfileId
//     };
//
//     $.ajax({
//         url: "/api/chats/set_chat_read_by_profile_id",
//         type: "PUT",
//         contentType: "application/json",
//         data: JSON.stringify(sendData),
//         //dataType: 'json',
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader("Accept", "application/json");
//             xhr.setRequestHeader("Content-Type", "application/json");
//             xhr.setRequestHeader(header, token);
//         }
//     })
// }
// function updateMessageReadStatusOnServer(message, readerProfileId) {
//     let token = $("meta[name='_csrf']").attr("content");
//     let header = $("meta[name='_csrf_header']").attr("content");
//
//     const sendData = {
//         chatId: currentChatId,
//         lastReadMessageId: message.id,
//         readerProfileId: readerProfileId
//     };
//
//     $.ajax({
//         url: "/api/chats/set_message_read_by_profile_id",
//         type: "PUT",
//         contentType: "application/json",
//         data: JSON.stringify(sendData),
//         //dataType: 'json',
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader("Accept", "application/json");
//             xhr.setRequestHeader("Content-Type", "application/json");
//             xhr.setRequestHeader(header, token);
//         }
//     })
// }

// function scrollMessageArea() {
//     $container = $('.message-area');
//     $container[0].scrollTop = $container[0].scrollHeight;
// }