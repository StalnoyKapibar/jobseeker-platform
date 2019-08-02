const currentProfileId = parseInt(document.getElementById("profileId").getAttribute('value'));

$(document).ready(function () {
    const table = $('#chatTable').DataTable({
        scrollY: '50vh',
        scrollCollapse: true,
        paging: false,
        searching: false,
        info: false,

        "sAjaxSource": "/api/chats/my/" + currentProfileId, //todo (Nick Dolgopolov) ChatMessageRestController.getAllLastMessages()
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
                    return "<p>" + full.creatorType + " (id=" + full.creatorProfileId + ")<\p>"
                        + "<p>" + full.creatorName + "<\p>";
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return "<p>" + full.topicCreatorType + " (id=" + full.topicCreatorProfileId + ")<\p>"
                        + "<p>" + full.topicCreatorName + "<\p>";
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return "<p>" + full.topicType + " (id=" + full.topicId + ")<\p>"
                        + "<p>" + full.topicTitle + "<\p>";
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return "<p>" + full.lastMessage.text + "<\p>"
                        + "<p>" + full.lastMessage.date + "<\p>";
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