$(document).ready(function () {
    $("#btnsearch").on("click", function () {
        event.preventDefault();
        let string = $("#search").val();
        $("#search").val("");
        searchEmployer(string);
    })
});

function searchEmployer(string) {
    $("#rowid").empty();
    $("#employerTable").empty();
    $("#navpagination").empty();
    $("#bottom").empty();

    $.ajax({
        url: "/api/search/employer/" + string,
        type: "GET",
        success: function (data) {
            var button = '' +
                '<table class="table table-striped table-sm table-bordered sort-order" cellspacing="0" width="100%">' +
                    '<thead align="center">' +
                        '<tr>' +
                            '<th>Id</th>' +
                            '<th>Block</th>' +
                            '<th>Email</th>' +
                            '<th>Last Visit</th>' +
                            '<th>Action</th>' +
                        '</tr>' +
                    '</thead>' +
                    '<tbody align="center">'
            ;

            let heightNum = 30;
            if (data.length < 4) {
                heightNum = 125/data.length;
            }

            $.each(data, function (i, employerUser) {
                let bool = employerUser.enabled ? 'display:none' : '';
                let boolfalse = employerUser.enabled ? '' : 'display:none';
                let insert = '' +
                    '<div id="userBlockId' + employerUser.id + '">' +
                        '<button class="btn btn-success user_unblock adminEnableUserBtn" style="' + bool + '"' +
                                'data-id="' + employerUser.id + '">Разблокировать<br>' +
                            '<span id="timeEndTheLock_' + employerUser.id + '"></span>' +
                        '</button>' +
                        '<div class="dropdown user_block" style="margin:0; ' + boolfalse + '">' +
                            '<button class="btn btn-danger dropdown-toggle" type="button" data-toggle="dropdown">' +
                                    'Блокировать<span class="caret"></span>' +
                            '</button>' +
                            '<div class="dropdown-menu">' +
                                '<a class="dropdown-item user_block_operation" data-id="' + employerUser.id + '"' +
                                        'data-period="1" data-email="' + employerUser.email + '">На сутки</a>' +
                                '<a class="dropdown-item user_block_operation" data-id="' + employerUser.id + '"' +
                                        'data-period="3" data-email="' + employerUser.email + '">На 3 дня</a>' +
                                '<a class="dropdown-item user_block_operation" data-id="' + employerUser.id + '"' +
                                        'data-period="7" data-email="' + employerUser.email + '">На 7 дней</a>' +
                                '<a class="dropdown-item user_block_operation" data-id="' + employerUser.id + '"' +
                                        'data-period="14" data-email="' + employerUser.email + '">На 14 дней</a>' +
                                '<a class="dropdown-item user_block_operation" data-id="' + employerUser.id + '"' +
                                        'data-period="0" data-email="' + employerUser.email + '">Бессрочно</a>' +
                            '</div>' +
                        '</div>' +
                    '</div>' +
                    '<div class="modal fade" id="jobSeekerLock" role="dialog" aria-hidden="true">' +
                        '<div class="modal-dialog" role="document">' +
                            '<div class="modal-content">' +
                                '<div class="modal-header bg-dark text-white">' +
                                    '<b>Блокировка пользователя</b>' +
                                '</div>' +
                                '<div class="modal-body">Заблокировать пользователя' +
                                    '<span id="modalBodyEmail" class="font-weight-bolder"></span>на' +
                                    '<span id="modalBodyPeriod" class="font-weight-bolder"></span>?' +
                                '</div>' +
                                '<div class="modal-footer">' +
                                    '<button id="btnSuccess" class="btn btn-primary">' +
                                        'Подтвердить' +
                                    '</button>' +
                                    '<button class="btn btn-secondary" data-dismiss="modal">' +
                                        'выход' +
                                    '</button>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                    '</div>'
                ;

            button += '' +
                '<tr style="height: ' + heightNum + 'px">' +
                    '<td>' + employerUser.id + '</td>' +
                    '<td>' + insert + '</td>' +
                    '<td>' + employerUser.email + '</td>' +
                    '<td>' + employerUser.date.format("yyyy-mm-dd") + '</td>' +
                    '<td>' +
                        '<button onclick="editEmployer(' + employerUser.id + ')" type="button"' +
                                'class="btn btn-primary">' +
                            'Edit Employer' +
                        '</button>' +
                        '<button onclick="deleteEmployer(' + employerUser.id + ')" type="button"' +
                                'class="btn btn-primary" style="margin-left: 5px">' +
                            'Delete Employer' +
                        '</button>' +
                    '</td>' +
                '<tr>'
            ;
});

            button += '</tbody></table>';
            $("#employerTable").append(button);

            $("#bottom").append("<script src='/js/admin.js'></script>\n" +
                                "<script src='/js/admin/admin_employers.js'></script>\n" +
                                "<script src='/js/admin/admin_employer_search.js'></script>\n"
            );

            $(document).ready(function () {
                $.ajax({
                    url: "/api/employerprofiles/employer_timer/",
                    type: "GET",
                    success: function (data) {
                        $.each(data, function (i, item) {
                            let num = "timeEndTheLock_" + item.userId;
                            document.getElementById(num).innerHTML = item.time;
                        });
                    }
                });
            });
        },
    })
}
