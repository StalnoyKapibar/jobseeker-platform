$(document).ready(function () {
    const table = $('#chatTable').DataTable({
        scrollY: '50vh',
        scrollCollapse: true,
        paging: false,
        searching: false,
        info: false,

        "sAjaxSource": "/api/chats/all", //todo (Nick Dolgopolov) ChatMessageRestController.getAllLastMessages()
        "sAjaxDataProp": "",
        "order": [[1, "desc"]],
        "aoColumns": [
            {
                "mData": "id",
                "mRender": function (data, type, full) {
                    return '<a href="/chat/' + data + '">' + data + '</a>'
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return full.creatorProfileId + " "
                        + full.creatorName + " "
                        + full.creatorType;
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return full.participantProfileId + " "
                        + full.participantName + " "
                        + full.participantType;
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return full.topicId + " "
                        + full.topicType + " "
                        + full.topicTitle;
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return full.lastMessage.text + " " + full.lastMessage.date;
                }
            }
        ]
    });

    var url = "admin";
    //count_not_read_messages(url);

});

function count_not_read_messages(url) {
    $.ajax({
        url: "/api/chats/count_not_read_messages/" + url,
        type: "GET",
        async: false,
        success: function (data) {
            if (data !== 0) {
                var str = "   " + data;
                document.getElementById("count_not_read_messages").textContent = str;
            }
        }
    })
}


/* globals Chart:false, feather:false */

$(document).ready(function () {
    'use strict';

    feather.replace()
});