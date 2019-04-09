package com.example.friendscompolsury;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendscompolsury.Model.BEFriend;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import dk.easv.friendsv2.R;

public class DetailActivity extends FragmentActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    String TAG = MainActivity.TAG;
    DataAccessFactory _dataAccess = MainActivity._dataAccess;
    EditText etName, etPhone, etEmail, etAddress, etURL, etBirthday;
    ImageView mImageView;
    GoogleMap m_map;
    Button updateBtn;
    BEFriend currentFriend;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");
        LocateItems();
        setGUI();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dataAccess.updateContact(new BEFriend(getIntent().getLongExtra("friend", 0), etName.getText().toString(), etAddress.getText().toString(),
                        etPhone.getText().toString(), etEmail.getText().toString(), etURL.getText().toString(),
                        etBirthday.getText().toString(), 0, 0, mImageView.getTransitionName()));
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
        Log.d(TAG, "current friend" + currentFriend);
    }

    private void LocateItems() {
        Log.d(TAG, "Locating items");
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etURL = findViewById(R.id.etURL);
        CameraActivity.mImageView = findViewById(R.id.pictureView);
        etBirthday = findViewById(R.id.etBirthday);
        updateBtn = findViewById(R.id.btnUPDATE);
    }

    private void setGUI() {
        if (_dataAccess.getFriendsList().size() <= 0) {
            Log.d(TAG, "The database is empty");
        } else {
            currentFriend = _dataAccess.getFriendByID(getIntent().getLongExtra("friend", 0));
            Log.d(TAG, "setGUI: " + currentFriend.toString());

            etName.setText(currentFriend.getM_name());
            etEmail.setText(currentFriend.getM_email());
            etPhone.setText(currentFriend.getM_phone());
            etAddress.setText(currentFriend.getM_address());
            etBirthday.setText(currentFriend.getM_birthday());
            etURL.setText(currentFriend.getM_webSite());
            try {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(currentFriend.getM_img()));
            } catch (Exception ex) {
                Log.d(TAG, "Can't parse this to image: " + currentFriend.getM_img());
                Log.d(TAG, "" + ex);
            }
        }
    }


    private void sendSMS() {
        Log.d(TAG, "sendSMS method invoked");
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

    private void smsButton(View view) {
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

    private void mailButton(View view) {
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

    private void callButton(View view) {

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
                makeCall();
                Log.d(TAG, "permission to CALL granted!");
            }
        }
        makeCall();
    }

    private void makeCall() {
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

    private void currentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


                //Location location = locationManager.getLastKnownLocation(provider);

                Location location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                currentFriend.setM_location(location.getLatitude(), location.getLongitude());

                Toast.makeText(this, "Location saved: " + userLocation, Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex);
        }
    }

    private void addData(Intent x, BEFriend f) {
        x.putExtra("friend", f);
    }

    private void showMap(View view) {
        try {
            Intent x = new Intent(this, MapActivity.class);
            addData(x, currentFriend);
            Log.d(TAG, "Detail activity will be started");
            startActivity(x);
            Log.d(TAG, "Detail activity is started");
        } catch (Exception ex) {
            Log.d(TAG, "showMap: " + ex);
        }
    }

    private void saveLocation(View view) {
        currentLocation();
    }

    private void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);
    }

    private void goToURL(View view) {
        openWebURL(currentFriend.getM_webSite().toString());
    }
}
