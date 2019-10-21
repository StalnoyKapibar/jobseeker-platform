$(document).ready(function () {
    $.get("/api/chats/getCountUnReadMessageByUserId", function (data) {
        //Возвращает количество непрочитанных чатов (int)
		if ($.isNumeric(data) && data > 0) {
			$("#countNewMessage").text("Непрочитано чатов: " + data);
		}
    })
});

var currentUrl = window.location.href;
if (!currentUrl.includes('chat')) {

    function connectToChatServer() {
        var socket = new SockJS('/private_chat-messaging');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            stompClient.subscribe("/user/queue/reply", onMessageReceived);
            stompClient.subscribe("/user/queue/errors", function (message) {
                alert("Error " + message.body);
            });
        }, function (error) {
        });
    }

    function onMessageReceived(payload) {
        let message = JSON.parse(payload.body);
        let x = document.getElementById("snackbar");
        let mes = message.text;
        let messageText = mes;
        if (mes.length > 23) {
            messageText = mes.substring(0, 20);
            messageText += '...';
        }
        x.innerHTML = 'У вас новое сообщение' + '<br>' + messageText + '<br><button class="btn-flat toast-action" onclick="goToChat(' + message.id + ')">Перейти в чат</button>';
        // Add the "show" class to DIV
        x.className = "show";
        // After 4 seconds, remove the show class from DIV
        setTimeout(function () {
            x.className = x.className.replace("show", "");
        }, 4000);
    }

    function goToChat(messageId) {
        $.ajax({
            type: 'get',
            url: "api/chats/getChatByMessageId/" + messageId,
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            data: JSON.stringify(messageId),
            success: function (data) {
                window.location.replace("http://localhost:7070/private_chat/" + data.id);
            },
            error: function (error) {
                console.log(error);
                alert(error.toString());
            }
        });
    }
}

function connectToChatServer() {

}
