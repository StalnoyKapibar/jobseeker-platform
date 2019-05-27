var map;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 55.752030, lng: 37.633685},
        zoom: 12
    });
}