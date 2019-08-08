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
            var sort = [];
            $('#card_body_1').each(function () {
                sort.push($(this).text());
            });
            var saveSort = {'sort':sort};
            sessionStorage.setItem('sort', JSON.stringify(saveSort));
        }
    });
});