$(document).ready(function () {
    'use strict';
    feather.replace();
    $.ajax({
       url: "/api/report/comments/",
       type: "GET",
       success: function (data) {
           console.log(data);
       }
    });
});