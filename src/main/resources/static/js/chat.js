var stompClient = null;
var vacancyId = document.querySelector('#vacancyId').innerText.trim();
var username = null;

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');

function connect() {
    username = document.querySelector('#username').innerText.trim();
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe("/topic/chat/" + vacancyId, onMessageReceived)
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    // if (username!==message.author) {
    //     if (username==="admin@mail.ru") {
    //         message.is="true";
    //         updateMessage(message);
    //     } else {
    //         message.adminFrom="true";
    //         updateMessage(message);
    //     }
    // }

    $(".media-list").append('<li class="media">' +
        '<div class="media-body">' +
        '<div class="media">' +
        '<div class="media-body"><small>'
        + message.text +
        '<br/></small><small class="text-muted">'
        + message.author +
        '</small><hr/></div></div></div></li>');

    $container = $('.message-area');
    $container[0].scrollTop = $container[0].scrollHeight;
}

function updateMessage(message) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url: "/api/chatmessages/4",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(message),
        //dataType: 'json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader(header, token);
        }
    })
}

function refreshMessages(chatMessagesList) {
    chatMessagesList.reverse();
    $(".media-list").html("");
    $.each(chatMessagesList, function(i, message) {
        // if (username!==message.author['email']) {
        //     if (username==="admin@mail.ru") {
        //         if (message.adminTo!=="true"){
        //         message.adminTo="true";
        //         updateMessage(message);}
        //     } else {
        //         if(message.adminFrom!=="true") {
        //         message.adminFrom="true";
        //         updateMessage(message);}
        //     }
        // }
        $(".media-list").append('<li class="media">' +
                                '<div class="media-body">' +
                                '<div class="media">' +
                                '<div class="media-body"><small>'
                                    + message.text +
                                '</small><br/><small class="text-muted">'
                                + message.author['email'] +                    ///////////????????
                                '</small><hr/></div></div></div></li>');
    });
    $container = $('.message-area');
    $container[0].scrollTop = $container[0].scrollHeight;
}

function count_not_read_messages(url) {
    $.ajax({
        url: "/api/chatmessages/count_not_read_messages/" + url,
        type: "GET",
        async: false,
        success: function (data) {
                var str = "   " + data;
                document.getElementById("count_not_read_messages").textContent=str;}

    })
}

$(function(){

        connect();

        $.get("/api/chatmessages/" + vacancyId, function (chatMessagesList) {
            refreshMessages(chatMessagesList)});

        //$("#sendMessage").on("click", function() {sendMessage()});

        messageForm.addEventListener('submit', sendMessage, true);

        count_not_read_messages(vacancyId);

        function sendMessage(event) {
        $container = $('.message-area');
        $container.animate({ scrollTop: $container[0].scrollHeight }, "slow");
        var messageContent = messageInput.value.trim();
        if(messageContent && stompClient) {
            var chatMessage = {
                author: username,                        /////////??????????
                text: messageInput.value };                   /////////????????????
            stompClient.send("/live/chat/" + vacancyId, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();

    }
})