function showVacancy(id) {
    $.ajax({
        url: "/api/vacancies/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
            $("#VMDescription").text(data.description);

            if (data.remote) {
                $('#VMRemote').show();
            }else{
                $('#VMRemote').hide();
            }


        }
    });
}