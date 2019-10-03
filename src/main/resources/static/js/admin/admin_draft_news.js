$(document).ready(function () {
    resetDraftTableContent();

    $("#filter").change(function () {
        var filter = $("#filter option:selected").val();
        resetDraftTableContent(filter);
    });

    $("#approveDraft").click(function() {
        approveDraft($("#draftId").val());
    })

    $("#rejectDraft").click(function() {
        rejectDraft($("#draftId").val());
    })
})

function resetDraftTableContent(filter = 'ACTIVE') {
    var url = "/api/draftNews/" + (filter === 'ALL' ? '?filter=ALL' : '');
    $.get(url, function (data) {
        var tBodyHtml = '';
        $.each(data, function (i, draft) {
            tBodyHtml += '<tr>'
            tBodyHtml += '<td>' + draft.id + '</td>';
            tBodyHtml += '<td>' + draft.headline + '</td>';
            var description = draft.description.length > 100 ?
                draft.description.substr(0, 100) + '...' : draft.description;
            tBodyHtml += '<td>' + description + '</td>';
            tBodyHtml += '<td class="text-center">';
            if (draft.valid == null) {
                tBodyHtml += '<button type="button" onclick="showDraft(' + draft.id + ')"' +
                    ' class="btn btn-primary" data-toggle="modal" data-target="#editModal">' +
                    'Проверить</button>';
            } else {
                tBodyHtml += '<span class="' + (draft.valid ? 'alert-success' : 'alert-danger') + '">' +
                    (draft.valid ? 'Принято' : 'Отклонено') + '</span>';
            }
            tBodyHtml += '</td>';
            tBodyHtml += '</tr>'
        });
        if(tBodyHtml.length == 0) {
            tBodyHtml = '<tr><td class="text-center" colspan="4">Записей нет</td></tr>';
        }
        $("#draftNewsTable tbody").empty().append(tBodyHtml);
    })
}

function rejectDraft(draftId) {
    $.get("/api/draftNews/" + draftId + "/reject", function () {
        $('#alert_modal').modal('show');
        resetDraftTableContent();
    })
}

function approveDraft(draftId) {
    $.get("/api/draftNews/" + draftId + "/approve", function () {
        $('#alert_modal').modal('show');
        resetDraftTableContent();
    })
}

function showDraft(draftId) {
    $("#draftId").val(draftId);
    $.get("/api/draftNews/" + draftId, function (data) {
        $("#draftHeadline").val(data.headline);
        $("#draftDescription").text(data.description);
        $("#originalHeadline").val(data.original.headline);
        $("#originalDescription").text(data.original.description);
    });
}