let resumeId = $("meta[name='resumeId']").attr("content");
let userRole = $("meta[name='userRole']").attr("content");

$(document).ready(function () {
    getSingleResume(resumeId);
    if(userRole === "ROLE_SEEKER"){
        $("#editButton").prop('hidden', false);
    }
});

function getSingleResume(id) {
    $.ajax({
        url: "/api/resumes/getbyid/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            let tags = "";
            let tagsHead = "Ключевые навыки: <br>";
            $.each(data.tags, function (key, value) {
                tags += '<span class="badge badge-pill badge-success btnClick text-dark">' + value.name + '</span>'
            });
            let jobHead = "Предыдущий опыт работы: <br>";
            let jobExp = "";
            let jobPer = "Время работы: <br>";
            if (data.jobExperiences.length === 0) {
                jobExp += '<span>Нет опыта</span> <br>';
            } else {
                $.each(data.jobExperiences, function (key, value) {
                    jobExp += '<span>Место работы: ' + value.companyName + '</span> <br>';
                    jobExp += '<span>Должность: ' + value.position + '</span> <br>';
                    jobExp += '<span>Выполняемые задачи: ' + value.responsibilities + '</span> <br>';
                    let firstDay = new Date(value.firstWorkDay);
                    jobPer += '<span>' + firstDay.toLocaleDateString() + '</span> - ';
                    let lastDay = new Date(value.lastWorkDay);
                    jobPer += '<span>' + lastDay.toLocaleDateString() + '</span> <br>'
                    return key > 2;
                });
            }
            $("#VMTagsHead").html(tagsHead);
            $("#VMTags").html(tags);
            $("#VMJobHead").html(jobHead);
            $("#VMJobPer").html(jobPer);
            $("#VMJobExp").html(jobExp);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text("Город: " + data.city);
            let str = "Зарплата: ";
            if (data.salaryMin != null) {
                str = str + "от " + data.salaryMin + " рублей ";
            }
            if (data.salaryMax != null) {
                str = str + "до " + data.salaryMax + " рублей";
            }
            if (str === "Зарплата: ") {
                str = "Зарплата не указана";
            }
            $("#VMSalary").text(str);
        }
    });
}

function edit() {
    location.href = "/seeker/resumes/edit/" + resumeId;
}
