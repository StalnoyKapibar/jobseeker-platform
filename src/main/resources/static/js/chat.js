var stompClient = null;

var chatId = parseInt(document.getElementById("chatId").getAttribute('value'));
var profileId = parseInt(document.getElementById("profileId").getAttribute('value'));

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');


function count_not_read_messages(url) { //todo (Nick Dolgopolov)
    // $.ajax({
    //     url: "/api/chats/count_not_read_messages/" + url,
    //     type: "GET",
    //     async: false,
    //     success: function (data) {
    //             var str = "   " + data;
    //             document.getElementById("count_not_read_messages").textContent=str;}
    //
    // })
}

$(function () {

    connect();

    getMessages();

    messageForm.addEventListener('submit', sendMessage, true);

    if (profileId == "admin@mail.ru") { //todo
        count_not_read_messages("admin");
    } else {
        count_not_read_messages(chatId);
    }

    function connect() {
        var socket = new SockJS('/chat');
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

    function getMessages() {
        $.get("/api/chats/" + chatId, function (chatMessagesList) {

            chatMessagesList.reverse();
            $(".media-list").html("");
            $.each(chatMessagesList, function (i, message) {
                processReceivedMessage(message);
            });
            $container = $('.message-area');
            $container[0].scrollTop = $container[0].scrollHeight;
        });

    }

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);

        processReceivedMessage(message);

        $container = $('.message-area');
        $container[0].scrollTop = $container[0].scrollHeight;
    }

    function processReceivedMessage(message) {
        if ((profileId !== message.creatorProfile) && (!message.read)) {
            message.read = true;
            updateMessageOnServer(message);
        }
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
        $container = $('.message-area');
        $container.animate({scrollTop: $container[0].scrollHeight}, "slow");
        var messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            var chatMessage = {
                creatorProfileId: profileId,
                text: messageInput.value
            };
            stompClient.send("/live/chat/" + chatId, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
    }

    function updateMessageOnServer(message) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: "/api/chats/update_message",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(message),
            //dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.setRequestHeader(header, token);
            }
        })
    }
});