package com.example.kirantask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //    initializing variables
    private GoogleMap mMap;
    String starting_lat, starting_lng;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private LocationManager mLocationManager;
    boolean isInternetGranted = false;
    String mobile_number = "8019442330";

    boolean isMessageSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

//        mobile_number = getIntent().getStringExtra("mobile_number");

//        initializing map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


//        Checking for Location Access Permission
        if (isInternetGranted) {
//         Initializing MapView
            initMap();
        } else {
//            Asking for Access permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }


    //    After Getting response from Access Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //        If Access Permissions granted
            initMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        handler for updating current location for every 1 sec
        handler.post(periodicUpdate);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        Getting access permission status
        isInternetGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (isInternetGranted) {
            getCurrentLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isInternetGranted) {
//            Removing location manager updates in onPause();
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("TAG", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
//                Adding marker to LatLngs
                drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.d("TAG", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    //    Handler for 1 sec
    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 2000);
            if (isInternetGranted) {
                getCurrentLocation();
            }
        }
    };

    //    Initializing map
    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
            finish();
        } else {
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    //    Getting current location
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(this, "Enable GPS network.", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("TAG", String.format("getCurrentLocation(%f, %f)", location.getLatitude(),
                    location.getLongitude()));
            drawMarker(location);
        }
    }

    //    adding marker to the LatLng
    private void drawMarker(Location location) {
        if (mMap != null) {
            mMap.clear();
            LatLng gps_current = new LatLng(location.getLatitude(), location.getLongitude());
            if (starting_lat == null && starting_lng == null) {
                starting_lng = "" + location.getLongitude();
                starting_lat = "" + location.getLatitude();
            }
            LatLng gps_start = new LatLng(Double.parseDouble(starting_lat), Double.parseDouble(starting_lng));
            mMap.addMarker(new MarkerOptions()
                    .position(gps_start)
                    .title("Fixed Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            mMap.addCircle(new CircleOptions()
                    .center(gps_start)
                    .radius(50)
                    .strokeWidth(1f)
                    .fillColor(0x880000FF));

            mMap.addMarker(new MarkerOptions()
                    .position(gps_current)
                    .title("Moving Position"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps_current, 18));

            float distance = distance_btw_pts(gps_start, gps_current);

            if (distance > 50) {
                if (mobile_number != null) {
                    if (!isMessageSend) {
                        sendSMSto_user(mobile_number);
                        isMessageSend = true;
                    }
                }
            }

            Toast.makeText(this, "Distance : " + distance, Toast.LENGTH_SHORT).show();
        }
    }

    //    Calculating distance between two LatLng points
    private float distance_btw_pts(LatLng from, LatLng to) {
        Location locationA = new Location("start");
        locationA.setLatitude(from.latitude);
        locationA.setLongitude(from.longitude);
        Location locationB = new Location("end");
        locationB.setLatitude(to.latitude);
        locationB.setLongitude(to.longitude);
        float distance = locationA.distanceTo(locationB);
        return distance;
    }

    private void sendSMSto_user(String number) {
        SmsManager.getDefault().sendTextMessage(number, null, "The person crossed 50 meters from his location.", null, null);

    }

    private void count_down_timer() {
        new CountDownTimer(10 * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

            }
        }.start();

    }
}
