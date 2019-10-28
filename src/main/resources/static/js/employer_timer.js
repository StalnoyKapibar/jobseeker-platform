$(document).ready(function () {
    $.ajax({
        url: "/api/employerprofiles/employer_timer/",
        type: "GET",
        success: function (data) {
            $.each(data, function (i, item) {
                let num = "timeEndTheLock_" + item.userId;
                document.getElementById(num).innerHTML = item.time;
            });
        }
    });
});

$("#btnSuccess").on( "click", function() {
    setTimeout("location.reload()", 500);
});
