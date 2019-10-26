$(document).ready(function () {
    timer();
});

var time;
var num;
function timer() {
     $.ajax({
        url: "/api/employerprofiles/employer_timer/",
        type: "GET",
        success: function (data) {
            $.each(data, function (i, item) {
                time = item.time;
                num = "timeEndTheLock_" + item.userId;
                setTimer();
            });
        }
    });
}

function setTimer() {
    if(time == 0) {
        return false;
    }
    time--;
    var t = time;
    var s = t%60;
    t -= s;
    t = Math.floor (t/60);
    var m = t%60;
    t -= m;
    var h = Math.floor (t/60);
    if (h < 10) h = '0' + h;
    if (m < 10) m = '0' + m;
    if (s < 10) s = '0' + s;
    var timePrint = h + ':' + m + ':' + s;
    document.getElementById(num).innerHTML = timePrint;
// Если раскомментировать то будет работать обратный отсчёт но только на 1 кнопке:
//    setTimeout("setTimer()", 1000);
}

$("#btnSuccess").on( "click", function() {
    setTimeout("location.reload()", 100);
});
