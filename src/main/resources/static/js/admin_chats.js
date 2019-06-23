$(document).ready (function () {
    var vacancyId;
    var table = $('#chatTable').DataTable({
        scrollY:        '50vh',
        scrollCollapse: true,
        paging:         false,
        searching: false,
        info: false,

        "sAjaxSource": "/api/chats/last", //todo ChatMessageRestController.getAllLastMessages()
        "sAjaxDataProp": "",
        "order": [[1, "desc"]],
        "aoColumns": [
            {"mData": "vacancyHeadline"},
            {"mData": "date"},
            {"mData": "chatId",
                "mRender": function (data, type, full) {
                    vacancyId=data;
                    return data;}},
            {"mData": "lastMessage",
             "mRender": function(data, type, full) {
                return '<a href="/chat/' + vacancyId + '">' + data + '</a>'}},
            {"mData": "read"}
        ],
        "columnDefs": [
            {"targets": [1,2,4], "visible": false }
        ],
        "rowCallback": function (row, data, index) {
            if ((data.read === false) & (data.author!=="admin@mail.ru")) {
                $(row).css("background-color", "#C6C7C8");
            }
        }
    });

    var url = "admin";
    count_not_read_messages(url);

});

function count_not_read_messages(url) {
    $.ajax({
        url: "/api/chats/count_not_read_messages/" + url,
        type: "GET",
        async: false,
        success: function (data) {
            if (data!==0){
                var str = "   " + data;
                document.getElementById("count_not_read_messages").textContent=str;}
        }
    })
}


/* globals Chart:false, feather:false */

$(document).ready (function () {
    'use strict'

    feather.replace()
})