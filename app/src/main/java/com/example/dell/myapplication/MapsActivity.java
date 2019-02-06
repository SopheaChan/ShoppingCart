package com.example.dell.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, LocationListener{

    private GoogleMap mMap;
    private String mCompanyName;
    private double mCompanyLatitude;
    private double mCompanyLongitude;
    private Toolbar mMapToolbar;
    private LatLng mCurrentLocation;
    private static int mMapType = GoogleMap.MAP_TYPE_NONE;
    private static final int REQUEST_LOCATION = 123;

    private FloatingActionButton btnDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMapToolbar = findViewById(R.id.mapsToolbar);
        setActionBar(mMapToolbar);
        if (getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnDirection = findViewById(R.id.btnDirection);

        mCompanyName = getIntent().getStringExtra("companyName");
        mCompanyLatitude = getIntent().getDoubleExtra("lat", 0);
        mCompanyLongitude = getIntent().getDoubleExtra("lng", 0);

//        btnDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDirection();
//            }
//        });
    }

//    private void getDirection() {
//        LatLng currentMarker = new LatLng(mCurrentLocation.latitude+3, mCurrentLocation.longitude+2);
//        GoogleDirection.withServerKey("AIzaSyAuB_9ndViwmXq92wI6EwvZjR6PhKHiWIs")
//                .from(mCurrentLocation)
//                .to(currentMarker)
//                .execute(new DirectionCallback() {
//                    @Override
//                    public void onDirectionSuccess(Direction direction, String rawBody) {
//                        if (direction.isOK()){
//                            Toast.makeText(MapsActivity.this, "Successfully obtained direction...", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionFailure(Throwable t) {
//                        Toast.makeText(MapsActivity.this, "Fail to establish direction...", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        Log.d("CurrentLatLng:", String.format(Locale.US, "%.2f", mCurrentLocation.latitude));
//
//    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            Toast.makeText(MapsActivity.this, "No access to user permission", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(mMapType);

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        getCompanyLocation();
        setupListener();

//        DialogItemDetail dialog = new DialogItemDetail(this);
//        dialog.setOnClickListener(callBack);
    }

    private void getCompanyLocation() {
//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.fresh_milk1);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(mCompanyLatitude, mCompanyLongitude);
        mMap.addMarker(markerOptions.position(latLng).title(mCompanyName));
    }

    private void setupListener() {
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(latLng).title(getAddressName(latLng)));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(latLng)));

    }

    private String getAddressName(LatLng latLng) {
        String placeName = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            placeName = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (Exception e) {
            Log.d("Location name:", e.toString());
        }
        return placeName;
    }

    private CameraPosition getCameraPosition(LatLng latLng) {
        return CameraPosition.builder()
                .target(latLng)
                .tilt(0)
                .zoom(6.50f)
                .bearing(0)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.setMinZoomPreference(6f);
        mMap.setMaxZoomPreference(13f);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

//    private OnDialogClick callBack = new OnDialogClick() {
//        @Override
//        public void onClick() {
//            Toast.makeText(MapsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.road_map:{
                mMapType = GoogleMap.MAP_TYPE_NORMAL;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.satellite:{
                mMapType = GoogleMap.MAP_TYPE_SATELLITE;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.terrain:{
                mMapType = GoogleMap.MAP_TYPE_TERRAIN;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.hybrid:{
                mMapType = GoogleMap.MAP_TYPE_HYBRID;
                mMap.setMapType(mMapType);
                break;
            }
            case 16908332:{
                onBackPressed();
                finish();
            }
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
