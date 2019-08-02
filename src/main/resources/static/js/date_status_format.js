function getMonth(month) {
    let months =  [
        "января", "февраля", "марта", "апреля", "мая", "июня", "июля",
        "августа", "сентября", "октября", "ноября", "декабря"
    ];
    return months[parseInt(month-1)];
}

function formatDates(list) {
    for (let i = 0; i < list.length; i++) {
        let date = $(list[i]).text();
        if(date==="N/A"){
            $(list[i]).text("не назначено");
            continue;
        }
        let year = date.substring(0, 4);
        let month = date.substring(5, 7);
        let day = date.substring(8, 10);
        let time = date.substring(11, 16);
        $(list[i]).text(day + " " + getMonth(month) + " в " + time);
    }
}

function formatStatuses(list) {
    for (let i = 0; i < list.length; i++) {
        let status = $(list[i]).text();
        switch (status) {
            case "CONFIRMED":
                $(list[i]).text("Утверждена");
                break;
            case "NOT_CONFIRMED":
                $(list[i]).text("Не утверждена");
                break;
            case "SCHEDUALED":
                $(list[i]).text("Ожидание подтверждения");
                break;
            case "CONFIRMED_BY_USER":
                $(list[i]).text("Одобрена кандидатом");
                break;
        }
    }
}

$(document).ready(function() {
    let dates = $("td.tableDate");
    let statuses = $("td.tableStatus");
    formatDates(dates);
    formatStatuses(statuses);
});