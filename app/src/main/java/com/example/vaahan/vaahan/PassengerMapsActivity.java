package com.example.vaahan.vaahan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class PassengerMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = PassengerMapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private List<LatLng> latLngList;
    private TextView distanceValue;
    private TextView durationValue, fareValue;
    private EditText from, to;
    GPSTracker gps;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    public static String passengerMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_maps);

        passengerMob = getIntent().getStringExtra("PASSENGER_PHONE");

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        distanceValue = (TextView)findViewById(R.id.dist);
        durationValue = (TextView)findViewById(R.id.time);
        fareValue = findViewById(R.id.fare);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        latLngList = new ArrayList<LatLng>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void otp(View v) {

        String fromtxt = from.getText().toString();
        String totxt = to.getText().toString();

        Intent i = new Intent(this, PassengerOTPActivity.class);
        i.putExtra("FROM_TEXT",fromtxt);
        i.putExtra("TO_TEXT",totxt);
        startActivity(i);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(PassengerMapsActivity.this);
        gps = new GPSTracker(PassengerMapsActivity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng here = new LatLng(latitude, longitude);
            onMapClick(here);
            //latLngList.add(here);
            //Toast.makeText(getApplicationContext(), "Your location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(latLngList.size() > 1){
            refreshMap(mMap);
            latLngList.remove(1);
            //latLngList.clear();
            distanceValue.setText("");
            durationValue.setText("");
        }
        latLngList.add(latLng);
        Log.d(TAG, "Marker number " + latLngList.size());
        createMarker(latLng, latLngList.size());
        if(latLngList.size() == 2){
            LatLng origin = latLngList.get(0);
            LatLng destination = latLngList.get(1);
            //use Google Direction API to get the route between these Locations
            String directionApiPath = Helper.getUrl(String.valueOf(origin.latitude), String.valueOf(origin.longitude),
                    String.valueOf(destination.latitude), String.valueOf(destination.longitude));
            Log.d(TAG, "Path " + directionApiPath);
            getDirectionFromDirectionApiServer(directionApiPath);
        }
    }
    private void refreshMap(GoogleMap mapInstance){
        mapInstance.clear();
    }
    private void createMarker(LatLng latLng, int position){
        MarkerOptions mOptions = new MarkerOptions().position(latLng);
        if(position == 1){
            mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }else{
            mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        addCameraToMap(latLng);
        mMap.addMarker(mOptions);
    }
    private void addCameraToMap(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(16)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void getDirectionFromDirectionApiServer(String url){
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(serverRequest);
    }
    private Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    Log.d("JSON Response", response.toString());
                    if(response.getStatus().equals("OK")){
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(mMap, mDirections);
                    }else{
                        Toast.makeText(PassengerMapsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }
    private void setRouteDistanceAndDuration(String distance, String duration, String fare, String start, String end){
        distanceValue.setText(distance);
        durationValue.setText(duration);
        fareValue.setText(fare);
        from.setText(start);
        to.setText(end);
    }
    private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                Log.d(TAG, "getDirectionPolylines: " + leg.getDistance().toString());
                String routeDistance = leg.getDistance().getText();
                String routeDuration = leg.getDuration().getText();
                int distInMeter = leg.getDistance().getValue();
                int init_fare = Math.round(((distInMeter - 1800) * 18) / 1000);
                int fare = init_fare < 0 ? 25 : init_fare + 25;
                String routeFare = "Rs. " + fare;
                String startAddress = leg.getStartAddress();
                String endAddress = leg.getEndAddress();
                setRouteDistanceAndDuration(routeDistance, routeDuration, routeFare, startAddress, endAddress);
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolyLineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }
    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
