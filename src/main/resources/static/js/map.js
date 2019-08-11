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

    $("#v_address_modal").val(document.getElementById('v_address').value);
    if(!address_check){
        $('#v_address_modal').removeClass('is-valid');
        $('#v_address_modal').addClass('is-invalid');
    }
    else {
        $('#v_address_modal').removeClass('is-invalid');
        $('#v_address_modal').addClass('is-valid');
    }
    if (typeof lat == "undefined") {
        lat = 55.722030;
        lng = 37.633685;
    }
    v_map = new google.maps.Map(document.getElementById('v_map'), {
        center: {lat: lat, lng: lng},
        zoom: 12
    });

    v_marker = new google.maps.Marker({
        position: {lat, lng},
        map: v_map,
        draggable: true
    });
    google.maps.event.addListener(v_map, 'click', function (event) {
        v_marker == undefined ? null : v_marker.setMap(null);
        v_marker = new google.maps.Marker({
            position: event.latLng,
            map: v_map,
            draggable: true
        });
        lat=event.latLng.lat();
        lng=event.latLng.lng();
        $("#v_address").val(getAddressByCoords(event.latLng.lat(), event.latLng.lng()));
        $("#v_address_modal").val(getAddressByCoords(event.latLng.lat(), event.latLng.lng()));
        google.maps.event.addListener(v_marker, 'dragend', function (a) {
            $("#v_address").val(getAddressByCoords(a.latLng.lat(), a.latLng.lng()));
            $("#v_address_modal").val(getAddressByCoords(a.latLng.lat(), a.latLng.lng()));
        });
        $("#v_address").removeClass("is-invalid");
        $("#v_address_modal").removeClass("is-invalid");
        $("#v_address").addClass("is-valid");
        $("#v_address_modal").addClass("is-valid");
        address_check = true;
    });

    var input = document.getElementById('v_address_modal');

    var autocomplete = new google.maps.places.Autocomplete(input);

    input.addEventListener("change", function () {
        input.value = "";
        $('#v_address_modal').removeClass('is-valid');
        $('#v_address_modal').addClass('is-invalid');
    });

    autocomplete.addListener('place_changed', function () {
        var place = autocomplete.getPlace();
        document.getElementById('v_address').value = place.formatted_address;
        $('#v_address_modal').removeClass('is-invalid');
        $('#v_address_modal').addClass('is-valid');
    });

    autocomplete.bindTo('bounds', v_map);

    var infowindow = new google.maps.InfoWindow();

    var marker = new google.maps.Marker({
        map: v_map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    autocomplete.addListener('place_changed', function () {
        infowindow.close();
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            // User entered the name of a Place that was not suggested and
            // pressed the Enter key, or the Place Details request failed.
            window.alert("No details available for input: '" + place.name + "'");
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            v_map.fitBounds(place.geometry.viewport);
        } else {
            v_map.setCenter(place.geometry.location);
            v_map.setZoom(17);  // Why 17? Because it looks good.
        }
        v_marker.setVisible(false);
        marker.setPosition(place.geometry.location);
        lat=marker.getPosition().lat();
        lng=marker.getPosition().lng();
        marker.setVisible(true);
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