let header = $("meta[name='_csrf_header']").attr("content");
let token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    getCompanies();
});

function getCompanies() {
    $.ajax ({
        url: "api/employerprofiles/companies",
        type: "GET",
        async: false,
        dataType: 'json',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (companies) {
            showCompanies(companies);
        }
    })
}

function showCompanies(companies) {
    $.each(companies, function (key, value) {
        let companyTags = "";
        $.each(value.vacancies[Math.floor(Math.random() * Math.floor(value.vacancies.length))].tags, function (i, item) {
            companyTags += '<span class="badge badge-pill badge-success btnClick text-dark" style="white-space: pre"><h7>' + item.name + '   </h7></span>';
        });

        $('#companiesList').append('<li class="list-group-item clearfix" id="more'+ value.id +'" style="max-height: 90px" data-toggle="modal"' +
            '<div class="headLine"><span>' + value.companyName + ' </span></div>' +
            '<div class="resumeTags" style="position: absolute; left: 75%; top: 5%">' + companyTags + '</div>' +
            '<div class="companyData" id="textStyle'+ value.id +'" style="white-space: nowrap; ' +
            'overflow: hidden; padding: 5px; text-overflow: ellipsis; max-width: 75%"><span>Описание: ' + value.description + '</span>' +
            '</div>' +
            '<br>' +
            '<div class="pull-right">' +
            '<span id="readMore'+ value.id +'" class="btn btn btn-dark btn-sm btnReadMore"' +
            'onclick="readMore('+ value.id +')">Подробнее</span>' +
            '<span class="btn btn-outline-info btn-sm btnCompanyInfo"' +
            'onclick="window.location.href=\'/employer/' + value.id + '\'">Профиль компании</span>' +
            '</div>' +
            '</li>');
    });
}

function readMore(id) {
    let more = document.getElementById("more" + id);
    let textStyle = document.getElementById("textStyle" + id);
    let readMore = document.getElementById("readMore" + id);

    more.style.maxHeight = "none";
    textStyle.style.whiteSpace = "pre-wrap";
    readMore.style.visibility = "hidden";
}