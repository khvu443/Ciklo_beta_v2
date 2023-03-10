// TO MAKE THE MAP APPEAR YOU MUST
// ADD YOUR ACCESS TOKEN FROM
// https://account.mapbox.com
mapboxgl.accessToken = 'pk.eyJ1IjoidnVkbmsiLCJhIjoiY2xkdTJkZmNnMDFwdTN3bzNnbXFxazZhbCJ9.6Pnn8vZr1jSzrZZ2gjwlEg';
const map = new mapboxgl.Map({
    container: 'map',
    // Choose from Mapbox's core styles, or make your own style with Mapbox Studio
    style: 'mapbox://styles/mapbox/streets-v12',
    center: [108.32802127180481, 15.877122429437135],
    zoom: 16.65,
    projection: "globe",
});

//NavigationControl
const nav = new mapboxgl.NavigationControl({
    visualizePitch: true,
});

map.addControl(
    new MapboxDirections({
        accessToken: mapboxgl.accessToken
    }),
    'top-right'
);
// const bounds = [
//     [108.32249401179008, 15.874140462342945], // [west, south]
//     [108.334051360848, 15.88016770227281], // [east, north]
// ];

// map.setMaxBounds(bounds);

// get lng and lat from click map
map.on("click", function (e) {
    const loc = e.lngLat;
    console.log(loc);
});


