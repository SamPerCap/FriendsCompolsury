package com.example.friendscompolsury;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.friendscompolsury.Model.BEFriend;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dk.easv.friendsv2.R;

public class MapActivity extends FragmentActivity  implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    String TAG = MainActivity.TAG;
    GoogleMap m_map;
    MarkerOptions friend_marker;
    BEFriend f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_details);
            Log.d(TAG, "Detail Activity started");

            MapFragment mapFragment =
                    (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            initMap();
        }
        catch(Exception ex)
        {
            Log.d(TAG, "onCreate map: " + ex);
        }

    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        Intent x = new Intent(this, DetailActivity.class);
        Log.d(TAG, "Detail activity will be started");
        startActivity(x);
        Log.d(TAG, "Detail activity is started");
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location)
    {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        m_map = googleMap;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED )
        {
            m_map.setMyLocationEnabled(true);
        }
        m_map.setOnMyLocationButtonClickListener(this);
        m_map.setOnMyLocationClickListener(this);

        zoomToCurrentLocation();
        userLocation();

        /*m_map.setMinZoomPreference(6f);
        m_map.setMaxZoomPreference(14f);*/
    }

    private void initMap()
    {
        f = (BEFriend) getIntent().getSerializableExtra("friend");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  m_map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Log.d(TAG, "getting the map async");
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "The map is ready - adding markers");
                m_map = googleMap;
                final LatLng ROUND = new LatLng(f.getM_location_x(), f.getM_location_y());

                friend_marker = new MarkerOptions().alpha((float) 0.3).position(ROUND).title(f.getM_name()
                        + " lives here!");

                m_map.addMarker(friend_marker);
            }
        });
    }

    private void zoomToCurrentLocation()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED )
        {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


            //Location location = locationManager.getLastKnownLocation(provider);

            Location location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));

            Log.d(TAG, "zoomToCurrentLocation: " + location);
            if (location != null)
            {
                m_map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                m_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private void userLocation()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



            Location location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            Toast.makeText(this, "Cords: " + userLocation, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED )
        {
            m_map.setMyLocationEnabled(true);
        }

    }

}
