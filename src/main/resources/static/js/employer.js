function showVacancy(id) {
    $.ajax({
        url: "/api/vacancies/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            var tags = "";
            $.each(data.tags, function (key, value) {
                tags += value.name + " "
            });
            $("#VMTags").text(tags);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
            $("#VMDescription").text(data.description);

            var str = "Зарплата: ";

            if (data.salaryMin!=null ){
                str = str + "от " + data.salaryMin + " рублей ";
            }
            if (data.salaryMax!=null ){
                str = str + "до " + data.salaryMax + " рублей";
            }
            if (str == "Зарплата: "){
                str = "Зарплата не указана";
            }
            $("#VMSalary").text(str);

            if (data.remote) {
                $('#VMRemote').show();
            }else{
                $('#VMRemote').hide();
            }

        }
    });
}

$("#input-id").rating();