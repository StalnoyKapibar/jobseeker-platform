$(function () {
    var user_id = $('#seekerProfileId').val();
    if (typeof user_id !== 'undefined') {
        getViewedVacancies(user_id);
    }
});
function getViewedVacancies(user_id) {
    $.ajax({
        url: "/api/seeker_vacancy_record/" + user_id,
        type: "GET",
        async: false,
        success: function (vacancies) {
            $.each(vacancies.reverse(), function (key, value) {
                $("#viewed_vacancies").append(
                    '<li class="card border-success" style="margin: 10px; display: inline-block">' +
                    '<div class="card-header"><span>' + value.headline + '</span></div>' +
                    '<div class="card-body"><h6 style="color: #777777;">Компания:</h6>' + value.companyname + '<br/><br/><h6 style="color: #777777;">Зарплата:</h6><span>'
                    +salaryFormat(value.salarymin, value.salarymax)+'</span></div>' +
                    '<div class="card-footer">' +
                    '<span class="btn btn-outline-success btn-sm"' +
                    ' onclick="window.location.href =\'/vacancy/' + value.id + '\';event.stopPropagation();">На страницу вакансии</span></div></li>');
            })
        }
    })
}

function salaryFormat(salarymin, salarymax) {
    let salary = '';
    if (salarymin!==0 && salarymax!==0) {
        return salary = '<span style="white-space: pre">от:  ' + salarymin + ' &#8381   до:  ' + salarymax +' &#8381</span>';
    }
    else if (salarymin===0 && salarymax!==0) {
        return salary = '<span style="white-space: pre">до:  ' + salarymax +' &#8381</span>';
    }
    else if (salarymin!==0 && salarymax===0) {
        return salary = '<span style="white-space: pre">от:  ' + salarymin + ' &#8381</span>';
    }
    else if (salarymin===0 && salarymax===0) {
        return salary = '<span style="white-space: pre">Зарплата не указана</span>';
    }
}