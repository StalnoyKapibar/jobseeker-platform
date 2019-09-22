let header = $("meta[name='_csrf_header']").attr("content");
let token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    getSeekerResumes();
});

function getSeekerResumes() {
    $.ajax ({
        url: "api/resumes/seeker",
        type: "POST",
        async: false,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (resumes) {
            seekerResumes(resumes);
        }
    })
}


function seekerResumes(resumeList) {
    $.each(resumeList.content, function (key, value) {
        let minSalary = '';
        let resumeTags = "";
        if (value.salaryMin) {
            minSalary = '<div class="salary"><span>Зарплата от: ' + value.salaryMin + ' руб.</span></div>';
        }
        $.each(value.tags, function (i, item) {
            resumeTags += '<span class="badge badge-pill badge-success btnClick text-dark"' +
                'style="white-space: pre"><h7>' + item.name + '</h7></span>';
        });

        $('#searchList').append('<li class="list-group-item clearfix">' +
            '<div class="headLine"><span>' + value.headline + '</span></div>' +
            '<div class="resumeTags" style="position: absolute; left: 75%; top: 5%">' + resumeTags + '</div>' +
            '<div class="companyData"><span>Сикер: ' + resumeList.seeker.fullName + '</span><br>' +
            '<span>Город: ' + value.city + '</span></div>' +
            '<br>' +
            minSalary +
            '<div class="text-right">' +
            '<span class="btn btn-outline-info btn-sm btnShowResume" data-toggle="modal"' +
            ' data-target="#resumeModal" onclick="showChosenResume(\'' + value.id + '\')">Подробнее</span>' +
            '<span class="btn btn-outline-secondary btn-sm btnEditResume"' +
            'onclick="#">Редактировать</span>' +
            '<span class="btn btn-outline-danger btn-sm btnDeleteResume"' +
            'onclick="deleteSeekerResumeById(\'' + value.id + '\')">Удалить резюме</span>'
            + '</div>' +
            '</li>');
    });
}

function showChosenResume(id) {
    $.ajax({
        url: "/api/resumes/getbyid/" + id,
        type: "GET",
        async: false,
        success: function (data) {
            let tags = "";
            $.each(data.tags, function (key, value) {
                tags += '<span class="badge badge-pill badge-success btnClick text-dark">' + value.name + '</span>'
            });
            let jobExp = "Предыдущий опыт работы: <br>";
            if (data.jobExperiences.length === 0) {
                jobExp += '<span>Нет опыта</span> <br>';
            } else {
                $.each(data.jobExperiences, function (key, value) {
                    jobExp += '<span>Место работы: ' + value.companyName + '</span> <br>';
                    jobExp += '<span>Должность: ' + value.position + '</span> <br>';
                    jobExp += '<span>Выполняемые задачи: ' + value.responsibilities + '</span> <br>';
                    let firstDay = new Date(value.firstWorkDay);
                    jobExp += '<span>Время работы: ' + firstDay.toLocaleDateString() + '</span> - ';
                    let lastDay = new Date(value.lastWorkDay);
                    jobExp += '<span>' + lastDay.toLocaleDateString() + '</span> <br>'
                    return key > 2;
                });
            }
            $("#VMTags").html(tags);
            $("#VMJobExp").html(jobExp);
            $("#VMHeadline").text(data.headline);
            $("#VMCity").text(data.city);
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

function deleteSeekerResumeById(seekerProfileIdResume) {

    $.ajax({
        url: '/api/resumes/delete/' + seekerProfileIdResume,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
            location.href = "/seeker/resumes"
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    });
}