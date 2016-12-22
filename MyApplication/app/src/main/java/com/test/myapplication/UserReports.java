package com.test.myapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Nirupama Prasad on 12/8/16.
 */
public class UserReports extends AppCompatActivity {

    ListView mListView;

    DatabaseReference mRef;
    DatabaseReference dRef;
   // DatabaseReference hRef;

    String login_email;
    String fb_email;
    String gmail;
    String email;
    String Loc;

    ArrayList<Report> reportArrayList;

    ArrayList<String> latitude = new ArrayList<>();
    ArrayList<String> longitude = new ArrayList<>();
    ArrayList<String> street = new ArrayList<>();
    ArrayList<String> emailid = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> size = new ArrayList<>();
    ArrayList<String> severity = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    Report value;

    Bitmap bitmap;
    Query queryRef;

    String currentUserEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_official);

        reportArrayList = new ArrayList<>();

        dRef = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        mRef = dRef.child("report");


        if(sharedPreferences.getString("gmail_address",null)!=null) {
            gmail = sharedPreferences.getString("gmail_address",null);
            queryRef = mRef.orderByChild("emailId").equalTo(gmail);
            email =gmail;

        } else if(sharedPreferences.getString("fb_address",null)!=null) {
            fb_email = sharedPreferences.getString("fb_address",null);
            queryRef = mRef.orderByChild("emailId").equalTo(fb_email);
            email =fb_email;

        } else if(sharedPreferences.getString("login_address",null)!=null) {
            login_email = sharedPreferences.getString("login_address",null);
                queryRef = mRef.orderByChild("emailId").equalTo(login_email);
            email = login_email;
        }

        mListView = (ListView) findViewById(R.id.listView);


        final CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(UserReports.this, reportArrayList);

        mListView.setAdapter(customBaseAdapter);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                value = dataSnapshot.getValue(Report.class);

                Log.i(TAG, "onChildAdded: "+value.getEmailId());
                Loc = value.getLatitude();


                 longitude.add(value.getLongitude());
                 latitude.add(Loc);
                 street.add(value.getStreet());
                emailid.add(value.getEmailId());
                description.add(value.getDescription());
                size.add(value.getSize());
                severity.add(value.getSeverity());
                img.add(value.getImg());

                reportArrayList.add(value);


                customBaseAdapter.notifyDataSetChanged();

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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(UserReports.this,OfficialDetail.class);

                Log.i(TAG, "onItemClick: email = "+email);

                if(reportArrayList.get(i).getEmailId().equals(email)) {
                    //intent.putExtra("Date",reportArrayList.)
                    intent.putExtra("email_of", reportArrayList.get(i).getEmailId());
                    intent.putExtra("long_of", reportArrayList.get(i).getLongitude());
                    intent.putExtra("lat_of", reportArrayList.get(i).getLatitude());
                    intent.putExtra("size_of", reportArrayList.get(i).getSize());
                    intent.putExtra("severity_of", reportArrayList.get(i).getSeverity());
                    intent.putExtra("descrip_of", reportArrayList.get(i).getDescription());
                    intent.putExtra("street_of", reportArrayList.get(i).getStreet());
                    intent.putExtra("img_of",reportArrayList.get(i).getImg());
                    intent.putExtra("Off","User");
                }
                startActivity(intent);


            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reports_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
        }
        if (id == R.id.maps) {

            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
            i.putStringArrayListExtra("latlist", latitude);
            i.putStringArrayListExtra("longlist", longitude);
            i.putStringArrayListExtra("Street", street);
            i.putStringArrayListExtra("Desc", description);
            i.putStringArrayListExtra("Severity", severity);
            i.putStringArrayListExtra("Size", size);
            i.putStringArrayListExtra("Email", emailid);
            i.putStringArrayListExtra("Image", img);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

