jQuery(function($) {
    // Asynchronously Load the map API 
    var script = document.createElement('script');
    script.src = "//maps.googleapis.com/maps/api/js?key=AIzaSyDhLgRIlfExRABThMGFnYg2bzKaTCEgrXU?sensor=false&callback=initialize";
    document.body.appendChild(script);
});


function initialize() {
    var map;
    var bounds = new google.maps.LatLngBounds();
    var mapOptions = {
        mapTypeId: 'roadmap'
    };
                    
    // Display a map on the page
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    map.setTilt(45);
        
    // Multiple Markers
    var markers = [
        ['Somewhere, Washington', 38.964072,-76.992120],
        ['Somewhere, Washington', 38.367719,-77.222838],
	['Somewhere, Washington', 38.264072,-74.992120],
        ['Somewhere, Washington', 38.167719,-73.222838],
	['Somewhere, Washington', 38.934072,-76.122120],
        ['Somewhere, Washington', 38.267719,-77.432838]
    ];
                        
    // Info Window Content
    var infoWindowContent = [
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here</p>' +        '</div>'],
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here.</p>' +
        '</div>'],
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here</p>' +        '</div>'],
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here.</p>' +
        '</div>'],
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here</p>' +        '</div>'],
        ['<div class="info_content">' +
        '<h3>Sample Text</h3>' +
        '<p>Put info here.</p>' +
        '</div>']
    ];
        
    // Display multiple markers on a map
    var infoWindow = new google.maps.InfoWindow(), marker, i;
    
	var image = "placeholder.png";
	
    // Loop through our array of markers & place each one on the map  
    for( i = 0; i < markers.length; i++ ) {
        var position = new google.maps.LatLng(markers[i][1], markers[i][2]);
        bounds.extend(position);
        marker = new google.maps.Marker({
            position: position,
            map: map,
            title: markers[i][0],
			icon: image
        });
        
        // Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent(infoWindowContent[i][0]);
                infoWindow.open(map, marker);
            }
        })(marker, i));

        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
    }

    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
        this.setZoom(5);
        google.maps.event.removeListener(boundsListener);
    });
    
}