package com.test.myapplication;

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

/**
 * Created by NehaRege on 12/8/16.
 */
public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";


    ListView mListView;

    DatabaseReference mRef;
    DatabaseReference dRef;

    ArrayList<Report> reportArrayList;

    ArrayAdapter<Report> arrayAdapter;
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

    String currentUserEmail;

    String Loc;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_official);

        Toast.makeText(OfficialActivity.this, "Official", Toast.LENGTH_SHORT).show();

        reportArrayList = new ArrayList<>();


        dRef = FirebaseDatabase.getInstance().getReference();


        mRef = dRef.child("report");
        Query qRef;
        qRef = mRef.orderByChild("currentstatus").equalTo("still_there");

        mListView = (ListView) findViewById(R.id.listView);


        final CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(OfficialActivity.this, reportArrayList);

        mListView.setAdapter(customBaseAdapter);


        qRef.addChildEventListener(new ChildEventListener() {
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
                Toast.makeText(OfficialActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();





                Intent intent = new Intent(OfficialActivity.this,OfficialDetail.class);

                    intent.putExtra("email_of", reportArrayList.get(i).getEmailId());
                    intent.putExtra("long_of", reportArrayList.get(i).getLongitude());
                    intent.putExtra("lat_of", reportArrayList.get(i).getLatitude());
                    intent.putExtra("size_of", reportArrayList.get(i).getSize());
                    intent.putExtra("severity_of", reportArrayList.get(i).getSeverity());
                    intent.putExtra("descrip_of", reportArrayList.get(i).getDescription());
                    intent.putExtra("street_of", reportArrayList.get(i).getStreet());
                    intent.putExtra("img_of",reportArrayList.get(i).getImg());
                    intent.putExtra("time",reportArrayList.get(i).getTimeNdate());

                intent.putExtra("Off","Official");

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
            i.putExtra("Off","Official");
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
