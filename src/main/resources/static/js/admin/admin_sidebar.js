$(document).ready(function () {
    $.ajax({
        url: "/api/chats/getCountOfChatsByAuthProfileId",
        type: "GET",
        async: true,
        success: function (data) {
            let str = "";
            data > 0 ? str = "(" + data + ")" : str = "";

            document.getElementById("count_not_read_chats").textContent = str
        }
    });
});