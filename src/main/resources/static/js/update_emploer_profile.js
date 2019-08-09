// $(function () {
//     $('#allVacancies').sortable();
//
//     // $(" #trckVacancies, #allVacancies").sortable({
//     //     connectWith: "#trckVacancies, #allVacancies",
//     //     update: function() {
//     //         var sort = [];
//     //         $('#trckVacancies').each(function () {
//     //             sort.push($(this).text());
//     //         });
//     //         var saveSort = {'sort':sort};
//     //         sessionStorage.setItem('sort', JSON.stringify(saveSort));
//     //     }
//     // });
// });
//





$( document ).ready(function() {
    $("#trckVacancies, #allVacancies").sortable({
        connectWith: "#trckVacancies, #allVacancies",
        update: function() {
            var vacancies = [];
            $('#trckVacancies li').each(function () {
                    vacancies.push({'position':$(this).index()+1, 'id':$(this).find('span').data('id')});
            });
            var saveSort = {'vacancies':vacancies};
            sessionStorage.setItem('vacancies', JSON.stringify(saveSort));
        }
    });

    $(".custom-file-input").on("change", function() {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });

});

function update(id) {
    var companyName = document.getElementById("companyname").value;
    var site = document.getElementById("companywebsite").value;
    var description = document.getElementById("description").value;
    var vacansies = JSON.parse(sessionStorage.getItem('vacancies'));

}

function update_img() {


}
