
$(document).ready (function () {
    $('#vacancyTable').DataTable({
        "sAjaxSource": "/api/vacancies/",
        "sAjaxDataProp": "",
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "headline"},
            {"mData": "city"},
            {"mData": "salaryMin"},
            {"mData": "salaryMax"}
        ]
    })
})



/* globals Chart:false, feather:false */

(function () {
    'use strict'

    feather.replace()


}())