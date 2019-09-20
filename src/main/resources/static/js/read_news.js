$(document).ready(function () {
 var newsId = $('#newsId').val();
 $.ajax({
  url: "/api/news/get_news?readNewsId=" + newsId,
  type: "GET",
  contentType: 'application/json; charset=utf-8',
  success: function (data) {
   console.log(data);
  }
 });
});
