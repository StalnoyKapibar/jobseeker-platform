function confirmMeeting(id){
    let status = "CONFIRMED_BY_USER";
    $.ajax({
        type: 'patch',
        url: "/api/meetings/" + id,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(status),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function () {
        },
        error: function (error) {
            console.log(error);
            alert(error.toString());
        }
    })
}