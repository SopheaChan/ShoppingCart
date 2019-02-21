package com.example.dell.myapplication.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, LocationListener, SearchView.OnQueryTextListener,
        android.support.v7.widget.SearchView.OnQueryTextListener {

    private GoogleMap mMap;
    private String mCompanyName;
    private double mCompanyLatitude;
    private double mCompanyLongitude;
    private Toolbar mMapToolbar;
    private LatLng mCurrentLocation;
    private static int mMapType = GoogleMap.MAP_TYPE_NONE;
    private static final int REQUEST_LOCATION = 123;

    private String[] points = {
            "sgneAsoe_S@lA?H?B@@?@@@@?@@B?@?D?TCTC",
            "qeneAwle_SBHDFNP",
            "wdneAske_SsA~Ai@f@QFi@La@Ni@LaANI@M?s@BW?g@?Y?QAE?MCMCAA]MIEUKOEMAKAYAIAG?IE_@Q",
            "y{neAsge_Sz@eBDKRc@d@qABSDW@W?_@Ae@",
            "swneAese_SK@?l@Cd@ARERGRINEJc@hAEHy@hBw@rAQVy@hAGFc@`@s@l@qBpAmBx@wAn@SH{@\\G@ODA?{ANgCReAFM@SBo@Ds@Do@@O?WAYCs@O_@Kg@I}Cq@SEOGYMa@QIECAGCGASK{@g@e@Ya@W{@i@IECAIIWSMMGG[Y]_@IG}@}@AC_@_@CEm@m@q@o@cAaAIICAAAEAEAC?GAI?G@E@GBOD}@X_A^{@XA?EBqBv@u@ZMFIDg@RkCfA{@\\o@TEB]Li@L",
            "u{qeAk`e_SS@]BsBLWBqAFiENcCHeBHu@Bs@DeCPE?EFM@mDHoGLmMRwIRgCFiFJmDFeCDuDH",
            "c|teAuyd_SDlH?vDLfFBpBBz@Dp@",
            "a{teAg|c_SeD|@e@LUFM@YBQ@uABgCFiCDcCHcCBiBH_@@eDJc@@c@@E?oAB}ADe@@mEHu@?gBB",
            "kuveAuvc_SAMM?}@Dm@BU?UAgHw@{Ei@s@IgKqAIAMCGAiEg@yBUc@G_BQy@KqE}AaDkAg@Qi@S",
            "sgneAsoe_SB|ADD`@CTCHPNPsA~Ai@f@QFkA\\i@LaANW@kAByAA[G_@Oo@W}@GQE_@Qz@eBXo@d@qABSFo@AeAK@?l@Cd@Gf@Qb@i@tA_ArBiAjBaApAwAnAqBpAmBx@kBx@cA^QDcFb@wCRcBFg@AmASgAUqDw@i@Uw@][MaBaAkBiAw@s@aC_CsAwAcC}BQEQAMBuCbAuDtAuB|@}F~BgAZq@DkCP{GVsI\\kCPEFM@}LVmMRwIRqJRsHLuDHDlH?vDLfFFlDDp@eD|@{@Tg@DyJRgGLiCJmFNyEJcGHgBBAMkADcABmQmBgLyAgMwAy@KqE}AiE}Ai@S`@mALUNKTIH?"
    };

    private FloatingActionButton btnDirection;
    private android.support.v7.widget.SearchView svSearchNearbyPlace;

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
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnDirection = findViewById(R.id.btnDirection);
        svSearchNearbyPlace = findViewById(R.id.svSearchPlace);
        svSearchNearbyPlace.setOnQueryTextListener(this);

        mCompanyName = getIntent().getStringExtra("companyName");
        mCompanyLatitude = getIntent().getDoubleExtra("lat", 0);
        mCompanyLongitude = getIntent().getDoubleExtra("lng", 0);

        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirection();
            }
        });
    }

    private void getDirection() {
        final LatLng currentMarker = new LatLng(mCurrentLocation.latitude + 3, mCurrentLocation.longitude + 2);
        GoogleDirection.withServerKey("AIzaSyAuB_9ndViwmXq92wI6EwvZjR6PhKHiWIs")
                .from(mCurrentLocation)
                .to(currentMarker)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Snackbar.make(btnDirection, "Direction Ok...", Snackbar.LENGTH_SHORT).show();
//                            Route route = direction.getRouteList().get(0);
//                            mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
//                            mMap.addMarker(new MarkerOptions().position(currentMarker));
//
//                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
//                            mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));
//                            setCameraWithCoordinationBounds(route);
                        } else {
                            Snackbar.make(btnDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MapsActivity.this, "Fail to establish direction...", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d("CurrentLatLng:", String.format(Locale.US, "%.2f", mCurrentLocation.latitude));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION){
            if (checkLocationPermission()) {
                mMap.setMyLocationEnabled(true);
                LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

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

    private void getCompanyLocation() {
//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.fresh_milk1);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(mCompanyLatitude, mCompanyLongitude);
        mMap.addMarker(markerOptions.position(latLng).title(mCompanyName));
    }

    private void setupListener() {
        mMap.setOnMapClickListener(this);
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

    private CameraPosition getCameraPosition(LatLng latLng, float zoomLevel) {
        return CameraPosition.builder()
                .target(latLng)
                .tilt(0)
                .zoom(zoomLevel)
                .bearing(0)
                .build();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.setMinZoomPreference(6f);
        mMap.setMaxZoomPreference(16f);
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
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(latLng).title(getAddressName(latLng)));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(latLng, 6.5f)));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(mMapType);

//        CameraUpdate camera = CameraUpdateFactory.newLatLng(new LatLng(11.546982, 104.890985));
//        mMap.animateCamera(camera);

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        for(String points : points){
            List<LatLng> steps = PolyUtil.decode(points);
            mMap.addPolyline( new PolylineOptions().addAll(steps));
        }

        getCompanyLocation();
        setupListener();

//        DialogItemDetail dialog = new DialogItemDetail(this);
//        dialog.setOnClickListener(callBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.road_map: {
                mMapType = GoogleMap.MAP_TYPE_NORMAL;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.satellite: {
                mMapType = GoogleMap.MAP_TYPE_SATELLITE;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.terrain: {
                mMapType = GoogleMap.MAP_TYPE_TERRAIN;
                mMap.setMapType(mMapType);
                break;
            }
            case R.id.hybrid: {
                mMapType = GoogleMap.MAP_TYPE_HYBRID;
                mMap.setMapType(mMapType);
                break;
            }
            case 16908332: {
                onBackPressed();
                finish();
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String placeName = query.toLowerCase();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + mCurrentLocation.latitude + "," + mCurrentLocation.longitude + "&radius=500&type=" + placeName + "&key=AIzaSyAjZhFkknKof0QER1PxyzbDB_2pvR1E4qk";
        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    Log.d("Response: ", "response...");
                    if (results.length() > 0) {
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject restaurant = results.getJSONObject(i);
                            JSONObject geometry = restaurant.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
//                            String icon = restaurant.getString("icon");
                            LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .title(restaurant.getString("name"));
                            mMap.addMarker(markerOptions);
                        }
                    } else {
                        Log.d("Response: ", "results.length()>0 is false...");
                    }
                } catch (Exception e) {
                    Snackbar.make(svSearchNearbyPlace, "response error...", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(svSearchNearbyPlace, "Failed sending request..", Snackbar.LENGTH_SHORT).show();
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(new LatLng(mCurrentLocation.latitude,
                mCurrentLocation.longitude), 15.0f)));
        requestQueue.add(jsonObjectRequest);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
