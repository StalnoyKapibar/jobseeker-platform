var map;
var marker;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 55.752030, lng: 37.633685},
        zoom: 12
    });
}

function showVacancyOnMap(lat, lng) {
    initMap();
    map.setZoom(15);
    map.setCenter(new google.maps.LatLng(lat, lng));
    marker = new google.maps.Marker({
        position: {lat: lat, lng: lng},
        map: map,
        title: $("#EPcompanyName").text()
    });
}

function getAddressByCoords(lat, lng) {
    var address;
    $.ajax({
        url: "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&key=AIzaSyD4rYuGJ61GR1luiHDRlwek1ufv5-1WkwE",
        type: "GET",
        async: false,
        success: function (data) {
            address = data.results[0].formatted_address;
        }
    });
    return address;
}