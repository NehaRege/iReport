package com.test.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Asmita Deshpande on 12/5/16.
 */

public class HomePage extends AppCompatActivity {

    private static final String TAG = "HomePage";
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;
    private FirebaseAuth firebaseAuth;
    Query queryRef;
    int flagCreate = 0;

    String login_email;
    String fb_email;
    String gmail;
    String email;
    User value;
    ArrayList<User> userArrayList = null;

    Button buttonReport;
    Button buttonMyReports;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_features);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(myToolbar);

        Log.i(TAG, "onCreateSpursh ");
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("user");



        Intent intent = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        if(sharedPreferences.getString("gmail_address",null)!=null) {
            gmail = sharedPreferences.getString("gmail_address",null);
            email = gmail;
//            editTextScreenName.setText(gmail);


        } else if(sharedPreferences.getString("fb_address",null)!=null) {
            fb_email = sharedPreferences.getString("fb_address",null);
            email = fb_email;
//            editTextScreenName.setText(fb_email);


        } else if(sharedPreferences.getString("login_address",null)!=null) {
            login_email = sharedPreferences.getString("login_address",null);
            email = login_email;
//            editTextScreenName.setText(login_email);

        }
        Log.i(TAG, "i am here"+ email);

         //queryRef = userDatabase.orderByChild("useremail");

        Log.i(TAG, "After Queryref"+queryRef);


        userDatabase.addChildEventListener(new ChildEventListener() {
            //Log.e()
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                value = dataSnapshot.getValue(User.class);

                if (value.getUseremail() == email)
                {
                   flagCreate = 1 ;
                   // break;
                }


                Log.i(TAG, "onChildAdded1: "+value.getUseremail());


//                userArrayList.add(value);






                //customBaseAdapter.notifyDataSetChanged();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded2: "+value.getUseremail());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildAdded3: "+value.getUseremail());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: "+value.getUseremail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onChildAdded4: "+value.getUseremail());

            }
        });

        /*for (User uA:userArrayList
             ) {
            Log.i(TAG, "forloop: ");
            if (email == uA.getUseremail())
            {
                flagCreate =1 ;
            }

        }*/

        if (flagCreate == 0)
        {
           // Log.i(TAG, "onUsernewCreate: "+value.getUseremail());

            User user1 = new User("","",email,"","","");

                    userDatabase.push().setValue(user1);
        }


        Log.i(TAG, "onCreate: latitude = "+sharedPreferences.getString("lat",null));



        Log.i(TAG, "onCreate: ");

        initializeViews();

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,ReportLittering.class);
                startActivity(intent);
            }
        });

        buttonMyReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,UserReports.class);
               // intent.putExtra("UserEmail",login_email);
                startActivity(intent);
            }
        });
        Log.i(TAG, "onCreate: HOMEPAGE");



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
//            Toast.makeText(this, "User profile clicked: "+login_email, Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(),Resident_features_settings.class);
            startActivity(i);

            return true;
        }

        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
        }
        if (id == R.id.maps) {

            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        buttonMyReports = (Button) findViewById(R.id.button_myreports);
        buttonReport = (Button) findViewById(R.id.button_report);

    }
}
