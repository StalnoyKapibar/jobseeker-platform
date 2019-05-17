
$(document).ready (function () {
    var table = $('#vacancyTable').DataTable({
        "sAjaxSource": "/api/vacancies/",
        "sAjaxDataProp": "",
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "headline"},
            {"mData": "city"},
            {"mData": "salaryMin"},
            {"mData": "salaryMax"},
            {"mData": "state"},
            {
                bSortable: false,
                data: null,
                className: "center",
                defaultContent: '<button class="btn btn-primary" data-target="#editModal" data-toggle="modal" >Edit</button>'
            }
        ],
        "columnDefs": [
            {
                "targets": [0],
                "visible": false,
                "searchable": false
            }
        ],
        "initComplete": function () {
            this.api().columns([5]).every( function () {
                var column = this;
                var select = $('<select><option value=""></option></select>')
                    .appendTo( $(column.footer()).empty() )
                    .on( 'change', function () {
                        var val = $.fn.dataTable.util.escapeRegex(
                            $(this).val()
                        );

                        column
                            .search( val ? '^'+val+'$' : '', true, false )
                            .draw();
                    } );

                column.data().unique().sort().each( function ( d, j ) {
                    select.append( '<option value="'+d+'">'+d+'</option>' )
                } );
            } );
        }
    });

    $('#vacancyTable tbody').on( 'click', 'button', function () {
        var data = table.row( $(this).parents('tr') ).data();
        //var modal = document.getElementById('editModal');
        $('#Id').val(data['id']);
        $('#Headline').val(data['headline']);
        $('#City').val(data['city']);
        $('#State').val(data['state']);
        $('#editModal').modal('show');
    } );


    $("#modalform").submit(function (event) {
        event.preventDefault();
        fire_ajax_submit();
    });


    function fire_ajax_submit() {
        var id = $("#Id").val();
        var state = $('#State').val();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");


        $.ajax({
            url: "/api/vacancies/" + id,
            type: "PUT",
            async: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify(state),
            //dataType: 'Json',
            success: function () {
                $('#modalclosebutton').click();
                table.ajax.reload();
            },
            error: function () {
                alert("Данные не обновились");
            }
        });
    };


})

/* globals Chart:false, feather:false */

$(document).ready (function () {
    'use strict'

    feather.replace()
})
