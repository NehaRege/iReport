package com.test.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.provider.FirebaseInitProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Address;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class ReportLittering extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;

    String TAG = "ReportLittering";
    public static int ACCESS_LOCATION_REQUEST_CODE = 323;
    public Location lastLocation;
    public String latitude, longitude;
    private GoogleApiClient googleApiClient;
    TextView lat, longi, street;

    EditText editTextDescription;

    RadioGroup radioGroupSeverityLevel, radioGroupSize;
    RadioButton radioButtonSeverityLevel, radioButtonSize;

    Button buttonSubmit;

    String login_email;
    String fb_email;
    String gmail;
    DatabaseReference mRef;
    DatabaseReference dRef;
    User value;

    String currentEmail;

    private DatabaseReference litterdatabase;
    private DatabaseReference reportDatabase;
    private FirebaseAuth firebaseAuth;


//    private DatabaseReference mDatabase;
//    private FirebaseAuth firebaseAuth;

    String latString;
    String email;
    String longString;
    String DescriptionString;
    String streetAddressString;
    Drawable imageDrawable;
    String sizeString;
    String severityString;
    String Status;
    Calendar c;
    String time = null;
    List<Report> list;

    Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_littering);

        Log.i(TAG, "onCreate: ");
        c = Calendar.getInstance();
        time = c.getTime().toString();


        googleApiClientSetUp();
        dispatchTakePictureIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor;
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("lat_string",gmail);
//        editor.commit()



        firebaseAuth=FirebaseAuth.getInstance();
        litterdatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        reportDatabase = litterdatabase.child("report");
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(sharedPreferences.getString("fb_address",null)!=null) {
            fb_email = sharedPreferences.getString("fb_address", null);
            email = fb_email;
            currentEmail = fb_email;
            Log.i(TAG, "onCreate:b_emai " + currentEmail);

        }
            if(sharedPreferences.getString("gmail_address",null)!=null) {
            gmail = sharedPreferences.getString("gmail_address",null);
            email = gmail;
            currentEmail = gmail;
            Log.i(TAG, "onCreate:gmail "+currentEmail);

        }

         else if(sharedPreferences.getString("login_address",null)!=null) {
            DatabaseReference myRef;
            login_email = sharedPreferences.getString("login_address",null);
            email = login_email;
            currentEmail = login_email;
            Log.i(TAG, "oogin_em "+currentEmail);

        }
        Log.i(TAG, "curr em "+currentEmail);


      list = new ArrayList<>();
        

        lat = (TextView) findViewById(R.id.lat);
        longi = (TextView) findViewById(R.id.longi);
        street = (TextView) findViewById(R.id.street);
        imageView = (ImageView) findViewById(R.id.imageView2);
        editTextDescription = (EditText) findViewById(R.id.editText);
        buttonSubmit = (Button) findViewById(R.id.button4);



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dRef = FirebaseDatabase.getInstance().getReference();
                mRef = dRef.child("user");
                Query qRef;
                qRef = mRef.orderByChild("useremail").equalTo(currentEmail);
                //qRef = mRef.orderByChild("emailConfirmation").equalTo("no");

                qRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        value = dataSnapshot.getValue(User.class);

                       // Log.i(TAG, "onChildAddedhihihihi: "+value.getEmailId());
                        //currentEmail = value.getEmailId();
                        String checkMail = value.getEmailConfirmation();
                        Log.i(TAG, "onChildcheck mail: "+checkMail);
                        if(checkMail == "no")
                        {
                            sendEmail();
                        }


                    /*reportArrayList.add(value);
                    if (value.getDescription()==Desc){
                        byte[] decodedString = Base64.decode(value.getImg(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgView.setImageBitmap(decodedByte);
                    }*/


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                sendEmail();

                Log.i(TAG, "onClick: ");

                addListenerOnButton();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // 'bitmap' is the image returned
                byte[] b = stream.toByteArray();

                String b64Image = Base64.encodeToString(b, Base64.DEFAULT);


                Log.i(TAG, "onClick: street = "+street.getText().toString());


                Report report2 = new Report(longi.getText().toString(),lat.getText().toString(),street.getText().toString(),"still_there",radioButtonSeverityLevel.getText().toString(),b64Image,radioButtonSize.getText().toString(),time, editTextDescription.getText().toString(),currentEmail);

                reportDatabase.push().setValue(report2);


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lat",lat.getText().toString());
                editor.putString("long",longi.getText().toString());
                editor.putString("street",street.getText().toString());
                editor.putString("description",editTextDescription.getText().toString());
                editor.putString("img",imageView.getDrawable().toString());
                editor.commit();

                finish();








            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            encodeBitmapAndSaveToFirebase(imageBitmap);


        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

                saveLocation();

            } else {

            }
        }
    }

    private void saveLocation() {

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_LOCATION_REQUEST_CODE);
            return;

        }

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(lastLocation != null) {

            latitude = String.valueOf(lastLocation.getLatitude());
            longitude = String.valueOf(lastLocation.getLongitude());
            Log.i(TAG, "saveLocation: lat = "+latitude);
            Log.i(TAG, "saveLocation: longitude = "+longitude);

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {

                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            //String street = addresses.get(0).getLocality();
            String getLocality = addresses.get(0).getLocality();
            String getPremises = addresses.get(0).getPremises();
            String getSubAdminArea =addresses.get(0).getSubAdminArea();
            String getSubLocality =addresses.get(0).getSubLocality();
            String getSubThoroughfare =addresses.get(0).getSubThoroughfare();
            String getThoroughfare =addresses.get(0).getThoroughfare();

            Log.i(TAG, "saveLocation: address = "+address);
            Log.i(TAG, "saveLocation: city = "+city);
            Log.i(TAG, "saveLocation: state = "+state);
            Log.i(TAG, "saveLocation: street = "+street);
            Log.i(TAG, "saveLocation: postalCode = "+postalCode);
            Log.i(TAG, "saveLocation: knownName = "+knownName);
            Log.i(TAG, "saveLocation: country = "+country);
            Log.i(TAG, "saveLocation: getSubAdminArea = "+getSubAdminArea);
            Log.i(TAG, "saveLocation: getSubLocality = "+getSubLocality);
            Log.i(TAG, "saveLocation: getSubThoroughfare = "+getSubThoroughfare);
            Log.i(TAG, "saveLocation: getThoroughfare = "+getThoroughfare);
            Log.i(TAG, "saveLocation: getLocality = "+getLocality);
            Log.i(TAG, "saveLocation: getPremises = "+getPremises);

            lat.setText(latitude);
            longi.setText(longitude);

            if (getSubThoroughfare == null)
            {
                street.setText(getThoroughfare);
            }
            else
            {
                street.setText(getSubThoroughfare +" "+ getThoroughfare);
            }


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.location_services_lat),latitude);
            editor.putString(getString(R.string.location_services_long),longitude);
            editor.commit();

        } else {
            Log.i(TAG, "saveLocation: lastlocation is null");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        saveLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void googleApiClientSetUp(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void addListenerOnButton() {

        radioGroupSeverityLevel = (RadioGroup) findViewById(R.id.severityLevel);
        radioGroupSize = (RadioGroup) findViewById(R.id.size);
        int selectedRadioSeverityLevel = radioGroupSeverityLevel.getCheckedRadioButtonId();
        int selectedRadioSize = radioGroupSize.getCheckedRadioButtonId();
        radioButtonSeverityLevel = (RadioButton) findViewById(selectedRadioSeverityLevel);
        radioButtonSize = (RadioButton) findViewById(selectedRadioSize);
        Log.i(TAG, "saveLocation: Severity Level = "+radioButtonSeverityLevel);
        Log.i(TAG, "saveLocation: Size = "+radioButtonSize);

    }

    private void sendEmail() {
        //Getting content for email

        String email = currentEmail.trim();
        String subject = "Garbage report";
        String message = street.getText().toString() + " " + "still_there" + " " +time+" "+editTextDescription.getText().toString();
        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}