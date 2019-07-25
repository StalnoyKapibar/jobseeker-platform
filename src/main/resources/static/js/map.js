var map;
var marker;
var v_map;
var v_marker;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 55.752030, lng: 37.633685},
        zoom: 12
    });
}

function initV_Map() {
    v_map = new google.maps.Map(document.getElementById('v_map'), {
        center: {lat: 55.752030, lng: 37.633685},
        zoom: 12
    });
    google.maps.event.addListener(v_map, 'click', function (event) {
        v_marker == undefined ? null : v_marker.setMap(null);
        v_marker = new google.maps.Marker({
            position: event.latLng,
            map: v_map,
            draggable: true
        });
        $("#v_address").val(getAddressByCoords(event.latLng.lat(), event.latLng.lng()));
        google.maps.event.addListener(v_marker, 'dragend', function (a) {
            $("#v_address").val(getAddressByCoords(a.latLng.lat(), a.latLng.lng()));
        });
        $("#v_address").removeClass("is-invalid");
        $("#v_address").addClass("is-valid");
        address_check = true;
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
        url: "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + $("meta[name='apiKey']").attr("content"),
        type: "GET",
        async: false,
        success: function (data) {
            address = data.results[0].formatted_address;
        }
    });
    return address;
}

function getCoordsByAddress(address) {
    var location;
    $.ajax({
        url: "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&language=ru&key=" + $("meta[name='apiKey']").attr("content"),
        type: "GET",
        async: false,
        success: function (data) {
            location = data.results[0].geometry.location;
        }
    });
    return location;
}

function getCityByCoords(lat, lng) {
    var city;
    $.ajax({
        url: "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + $("meta[name='apiKey']").attr("content"),
        type: "GET",
        async: false,
        success: function (data) {
            for (var i = 0; i < data.results[0].address_components.length; i++) {
                for (var b = 0; b < data.results[0].address_components[i].types.length; b++) {
                    if (data.results[0].address_components[i].types[b] == "locality") {
                        city = data.results[0].address_components[i].long_name;
                        break;
                    }
                }
            }
        }
    });
    return city;
}