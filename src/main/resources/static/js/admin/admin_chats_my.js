const currentProfileId = parseInt(document.getElementById("profileId").getAttribute('value'));

$(document).ready(function () {
    const table = $('#chatTable').DataTable({
        scrollY: '50vh',
        scrollCollapse: true,
        paging: false,
        searching: false,
        info: false,

        "sAjaxSource": "/api/chats/getAllChatsByMemberId/",
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
                    const author =
                        full.lastMessageCreatorType + " " +
                        full.lastMessageCreatorName + " " +
                        "(id=" + full.lastMessageProfileId + ")";
                    return "<p>" + author + "<\p>" +
                            "<p>" + full.lastMessageText + "<\p>" +
                             "<p>" + full.lastMessageDate + "<\p>";
                }
            },
            {
                "mData": "null",
                "mRender": function (data, type, full) {
                    return "<p>" + full.countOfUnreadMessages + "<\p>";
                }
            }
        ]
    });
});

$(document).ready(function () {
    'use strict';

    feather.replace()
});