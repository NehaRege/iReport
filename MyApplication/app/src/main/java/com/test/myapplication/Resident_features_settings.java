package com.test.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * Created by Asmita Deshpande on 12/5/16.
 */



public class Resident_features_settings extends AppCompatActivity {

    private static final String TAG = "Resident_features";

    private FirebaseAuth firebaseAuth;

    CheckBox email_confirmation;
    CheckBox email_notification;
    CheckBox report_anonymously;

    EditText editTextScreenName;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextHomeaddress;
    EditText editTextDescription;

    String login_email;
    String fb_email;
    String gmail;
    String firstname;
    String lastname;
    String address;
    String emailPref;
    String email;
    User value;
    int flagCreate = 0;
    Query qRef;

    Button update;
    //FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;

    boolean checked;

    int userListSize;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_settings);

        // database = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userDatabase = mDatabase.child("user");



        FirebaseUser user= firebaseAuth.getCurrentUser();


        Log.i(TAG, "onCreate: ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email_confirmation = (CheckBox)findViewById(R.id.checkBox);
        email_notification = (CheckBox)findViewById(R.id.checkBox1);
        report_anonymously = (CheckBox)findViewById(R.id.checkBox2);

        editTextScreenName = (EditText) findViewById(R.id.editText2);
        update= (Button)findViewById(R.id.button2);
        editTextFirstName = (EditText) findViewById(R.id.editText3);
        editTextLastName = (EditText) findViewById(R.id.editText4);
        editTextHomeaddress = (EditText) findViewById(R.id.editText5);


        boolean email_confirmationChecked = email_confirmation.isChecked();
        boolean email_notificationChecked = email_notification.isChecked();
        boolean report_anonymouslyChecked = report_anonymously.isChecked();

        Intent intent = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        userListSize = sharedPreferences.getInt("list_user_size",0);

        Log.i(TAG, "onCreate: user list size = "+userListSize);


        Log.i(TAG, "onCreate: description = "+sharedPreferences.getString("description",null));

        if(sharedPreferences.getString("gmail_address",null)!=null) {
            gmail = sharedPreferences.getString("gmail_address",null);
            Log.i(TAG, "onCreate: gmail ID = "+gmail);
            editTextScreenName.setText("");
            email = gmail;
            editTextScreenName.setText(gmail);


        }
        if(sharedPreferences.getString("fb_address",null)!=null) {
            fb_email = sharedPreferences.getString("fb_address",null);
            Log.i(TAG, "onCreate: fb ID = "+fb_email);

            editTextScreenName.setText("");

            editTextScreenName.setText(fb_email);
            email  = fb_email;


        }
        if(sharedPreferences.getString("login_address",null)!=null) {
            DatabaseReference myRef;
            login_email = sharedPreferences.getString("login_address",null);
            editTextScreenName.setText("");
            Log.i(TAG, "onCreate: login ID = "+ login_email);

            editTextScreenName.setText(login_email);
            email = login_email;

            // myRef= database.getReference("users").child("email");
            //myRef.setValue(login_email);

        }
        qRef = userDatabase.orderByChild("useremail").equalTo(email);

        qRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                value = dataSnapshot.getValue(User.class);
                editTextHomeaddress.setText(value.getAddress());
                editTextFirstName.setText(value.getFirstname());
                editTextLastName.setText(value.getLastname());

                Log.i(TAG, "email is here"+value.getUseremail());

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

       /* for (int i = 0; i < userListSize; i++) {

            userDatabase.addChildEventListener(new ChildEventListener() {

                //Log.e()
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    Log.i(TAG, "hey hey hey");

                    value = dataSnapshot.getValue(User.class);

                    Log.i(TAG, "onChildAdded: value email = " + value.getUseremail());


                    if (value.getUseremail() == email) {

                        Log.i(TAG, "bey bey bey");
                        flagCreate = 1;
                        // break;
                    }


                    Log.i(TAG, "onChildAdded1: " + value.getUseremail());


//                userArrayList.add(value);


                    //customBaseAdapter.notifyDataSetChanged();

                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.i(TAG, "onChildAdded2: " + value.getUseremail());

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "onChildAdded3: " + value.getUseremail());

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.i(TAG, "onChildAdded: " + value.getUseremail());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG, "onChildAdded4: " + value.getUseremail());

                }
            });
        }


        if (flagCreate == 0)
        {
            // Log.i(TAG, "onUsernewCreate: "+value.getUseremail());

            User user1 = new User("","",email,"","","");

            userDatabase.push().setValue(user1);
        }





*/
            update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // DatabaseReference myRef;
                firstname=editTextFirstName.getText().toString();
                lastname=editTextLastName.getText().toString();
                address=editTextHomeaddress.getText().toString();

                FirebaseUser user= firebaseAuth.getCurrentUser();
//                mDatabase.child(user.getUid()).setValue(person);

                if (checked)
                {
                    User user1 = new User(firstname,lastname,editTextScreenName.getText().toString(),address,"yes");
                    userDatabase.push().setValue(user1);

                }
                else
                {

                    User user1 = new User(firstname,lastname,editTextScreenName.getText().toString(),address,"no");
                    userDatabase.push().setValue(user1);


                }

//                User user1 = new User(firstname,lastname,editTextScreenName.getText().toString(),address,email_confirmation.getText().toString(),email_notification.getText().toString());
//                email_confirmation.getText().toString()

                Log.i(TAG, "onClick: first Name = "+firstname);


//                userDatabase.push().setValue(user1);

//                mDatabase.push().setValue(person);

                //writeNewUser(firstname,lastname,);
                //myRef
//                 String key       = database.getReference("users").child("email").push().getKey();
//                //myRef.setValue(login_email);
//                myRef= database.getReference("users").child(key).child("lastName");
//                myRef.setValue(lastname);
//                myRef.getRef();
//                myRef= database.getReference("users").child(key).child("homeAddress");
//                myRef.setValue(address);

//                myRef.child("users").push({"firstName": firstname, "lastName": lastname , "homeAddress": address});

                finish();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox2:
                if (checked)
                {
                    email_confirmation.setChecked(false);
                    email_notification.setChecked(false);
                    emailPref = "No";

                }
                else
                {
                    emailPref = "Yes";
                    email_confirmation.setChecked(true);
                    email_notification.setChecked(true);
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void writeNewUser(String firstname, String userId, String lastname, String useremail, List<Report> reportList) {
//        User user = new User(firstname,lastname,useremail,reportList);
//
//        mDatabase.child("users").child(userId).setValue(user);
//    }
}
