$(document).ready(function () {
    $("#btnsearch").on("click", function () {
        event.preventDefault();
        let strSearch = $("#search").val();
        $("#search").val("");
        mySearch(strSearch);
    })
});

function mySearch(strSearch) {
    $("#employerTable").empty();
    $("#navpagination").empty();
    $("#rowid").empty();

    $.ajax({
        url: "/api/employer_search/" + strSearch,
        type: "GET",
        success: function (data) {
            $("#employerTable").append("<h1>EMPLOYER</h1>" + data.id);
        },
        error: function () {
            $("#employerTable").append("<h1>ОШИБКА EMPLOYER</h1>");
        }
    });
}
