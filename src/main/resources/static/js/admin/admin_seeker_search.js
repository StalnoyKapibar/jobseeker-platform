$(document).ready(function () {
    $("#btnsearch").on("click", function () {
        event.preventDefault();
        let strSearch = $("#search").val();
        $("#search").val("");
        mySearch(strSearch);
    })
});

function mySearch(strSearch) {
    $("#rowid").empty();
    $("#seekerTable").empty();
    $("#navpagination").empty();

    $.ajax({
        url: "/api/seeker_search/" + strSearch,
        type: "GET",
        success: function (data) {
            $("#seekerTable").append("<h1>SEEKER</h1>");
        },
        error: function () {
            $("#seekerTable").append("<h1>ОШИБКА SEEKER</h1>");
        }
    });
}
