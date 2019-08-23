var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    var employerProfileId = $('#employerProfileId').val();
    $.ajax({
        type: 'get',
        url: "/api/chats/get_all_by_employer_profile_id?employerProfileId=" + employerProfileId,
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
    });


});

function getLastMessage(chatId) {
    $.get("/api/chats/" + chatId, function (chatMessagesList) {
        if (chatMessagesList && chatMessagesList.length > 0) {
            chatMessagesList.reverse();
            var lastReceivedMessage = chatMessagesList[chatMessagesList.length - 1];
            var date = messageDateFormat(lastReceivedMessage.date);
            $('#chatMessageData_' + chatId).append('<div class="chatMessageText"><span>' + lastReceivedMessage.text + '</span></div>' +
                '<div class="chatMessageDate"><span>' + date + '</span></div>');
        }
    });
}