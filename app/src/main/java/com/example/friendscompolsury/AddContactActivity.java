package com.example.friendscompolsury;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.friendscompolsury.Model.BEFriend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.easv.friendsv2.R;

public class AddContactActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String TAG = MainActivity.TAG;
    private EditText etSaveName, etSaveEmail, etSaveAddress, etSavePhone, etSaveBirthday, etSaveURL;
    private Button saveContactButton;
    private DataAccessFactory _dataAccess;
    private ImageView _pictureView;
    private IDataCRUD _dataCRUD;
    Bitmap mImageBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        Log.d(TAG, "Add contact Activity started");
        locateItems();

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContactInDatabase();
                setContentView(R.layout.activity_detail);
            }
        });
        _dataCRUD = _dataAccess.getInstance();
    }

    private void locateItems(){
        saveContactButton = findViewById(R.id.SaveContactButton);
        etSaveName = findViewById(R.id.etSName);
        etSaveEmail = findViewById(R.id.etSEmail);
        etSaveURL = findViewById(R.id.etSURL);
        etSaveAddress = findViewById(R.id.etSAddress);
        etSavePhone = findViewById(R.id.etSPhone);
        etSaveBirthday = findViewById(R.id.etSBirthday);
        _pictureView = findViewById(R.id.pictureView);
    }

    private void saveContactInDatabase() {
        Log.d(TAG,"Saving a new contact into the database");
        //String m_name, String m_address, String m_phone, String m_email,
        //String m_webSite, String m_birthday, double m_location_x,
        //double m_location_y, int m_img
        _dataCRUD.addPerson(new BEFriend(etSaveName.getText().toString(),etSaveAddress.getText().toString(),
                etSavePhone.getText().toString(), etSaveEmail.getText().toString(), etSaveURL.getText().toString(),
                etSaveBirthday.getText().toString(), 0,0, _pictureView.toString()));
        Log.d(TAG,"New person added");
        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
    }
    public void goToCamera(View view) {
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
            _pictureView.setImageBitmap(mImageBitmap);
            saveFileInLocalFolder();
        }
    }

    private void saveFileInLocalFolder() {
        FileOutputStream outputPhoto = null;
        try {
            File f = getOutputMediaFile();
            outputPhoto = new FileOutputStream(f);
            mImageBitmap
                    .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
            Log.d(TAG, "Photo taken - size: " + f.length() );
            Log.d(TAG, "     Location: " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private File getOutputMediaFile(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {


            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to CAMERA - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            String postfix = "jpg";
            String prefix = "IMG";

            File mediaFile = new File(mediaStorageDir.getPath() +
                    File.separator + prefix +
                    "_" + timeStamp + "." + postfix);

            return mediaFile;
        }
        Log.d(TAG, "Permission for writing NOT granted");
        return null;
    }
}
