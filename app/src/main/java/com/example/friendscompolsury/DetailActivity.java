package com.example.friendscompolsury;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DetailActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 1;

    String TAG = MainActivity.TAG;


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap mImageBitmap;

    MarkerOptions friend_marker;

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etLocation;
    CheckBox cbFavorite;
    ImageView mImageView;
    GoogleMap m_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");

        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        cbFavorite = findViewById(R.id.cbFavorite);
        mImageView = findViewById(R.id.pictureView);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initMap();
        setGUI();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  m_map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Log.d(TAG, "getting the map async");
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "The map is ready - adding markers");
                m_map = googleMap;

                BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
                final LatLng ROUND = new LatLng(f.getM_location_x(), f.getM_location_y());

                friend_marker = new MarkerOptions().alpha((float) 0.3).position(ROUND).title(f.getM_name()
                        + " lives here!");

                m_map.addMarker(friend_marker);
            }
        });
    }

    private void setGUI() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
        Log.d(TAG, "setGUI: " + f.toString());
        etName.setText(f.getM_name());
        etEmail.setText(f.getM_email());

        etPhone.setText(f.getM_phone());
        cbFavorite.setChecked(true);
        mImageView.setImageResource(f.getM_img());
    }

    private void sendSMS() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
                .show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            } else
                Log.d(TAG, "permission to SEND_SMS granted!");

        }

        SmsManager m = SmsManager.getDefault();
        String text = "Hi, it goes well on the android course...";
        m.sendTextMessage(etPhone.getText().toString(), null, text, null, null);
    }

    public void smsButton(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("SMS Handling");

        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.this.sendSMS();
                    }
                })
                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.this.startSMSActivity();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void startSMSActivity() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + etPhone.getText()));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        startActivity(sendIntent);
    }

    public void mailButton(View view) {
        Log.e(TAG, "Email: " + etEmail.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = {etEmail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "This is a test mail");
        startActivity(emailIntent);
    }

    public void camButton(View view) {
        Log.e(TAG, "What happens?");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {

                    Log.d(TAG, "permission denied to CAMERA - requesting it");
                    String[] permissions = {Manifest.permission.CAMERA};

                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }
            }
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            Bundle b = data.getExtras();

            mImageBitmap = (Bitmap) b.get("data");
            mImageView.setImageBitmap(mImageBitmap);
        }
    }

    public void callButton(View view) {

        Toast.makeText(this, "A CALL will be made", Toast.LENGTH_LONG)
                .show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to CALL - requesting it");
                String[] permissions = {android.Manifest.permission.CALL_PHONE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            } else {
                makeCalle();
                Log.d(TAG, "permission to CALL granted!");
            }
        }
        makeCalle();
    }

    private void makeCalle() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + etPhone.getText().toString()));
        Log.e(TAG, "Calling permission: " + intent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager m = SmsManager.getDefault();
            m_map.setMyLocationEnabled(true);
            String text = "Hi, it goes well on the android course...";
            m.sendTextMessage(etPhone.getText().toString(), null, text, null, null);
        }

    }

    private void userLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = service.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = service.getLastKnownLocation(provider);

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        etLocation.setText("Cords: " + userLocation);
    }

    public void getLocation(View view) {
        userLocation();

        m_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.d(TAG, "Map clicked [" + point.latitude + " / " + point.longitude + "]");
                Toast.makeText(DetailActivity.this, "Map cords" + point.latitude + ", " + point.longitude, Toast.LENGTH_SHORT).show();
                //Do your stuff with LatLng here
                //Then pass LatLng to other activity
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            m_map.setMyLocationEnabled(true);
        }
        m_map.setOnMyLocationButtonClickListener(this);
        m_map.setOnMyLocationClickListener(this);

        zoomToCurrentLocation();

        /*m_map.setMinZoomPreference(6f);
        m_map.setMaxZoomPreference(14f);*/
    }

    private void zoomToCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        Log.d(TAG, "zoomToCurrentLocation: " + location);
        if (location != null)
        {
            m_map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }}
}
