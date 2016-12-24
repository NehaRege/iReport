package com.test.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import static com.test.myapplication.R.id.button3;
import static com.test.myapplication.R.id.radioButton5;
import static com.test.myapplication.R.id.radioButton6;
import static com.test.myapplication.R.id.radioButtonRemovalConfirmed;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.database.DatabaseError;

/**
 *
 *
 * Created by NehaRege on 12/8/16.
 */
public class OfficialDetail extends AppCompatActivity {

    TextView textViewStreet;
    TextView textViewLat;
    TextView textViewLong;
    TextView textViewDescription;
    TextView textViewSeverity;
    TextView textViewSize;
    TextView textTime;

    Button update;
    TextView textViewEmail;
    RadioButton RadioButton5;
    RadioButton RadioButton6;
    RadioButton RadioButton8;
    DatabaseReference mRef;
    DatabaseReference dRef;
    DatabaseReference rRuf;
    String who;
    String ifnoimage;
    String Desc;
    String statuschange;
    String description;
    String imagepass;
    ArrayList<Report> reportArrayList;

    Report value;
    ListView mListView;


    ImageView imgView;
    String currentEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_item_detail);
        RadioButton5 = (RadioButton)findViewById(radioButton5);
        RadioButton6 = (RadioButton)findViewById(radioButton6);
        RadioButton8 = (RadioButton)findViewById(radioButtonRemovalConfirmed);
        update = (Button)findViewById(button3);
        final Intent intent = getIntent();

        initialize();
description =intent.getStringExtra("descrip_of");
        Log.i(TAG, "description: "+description);

        if(intent!=null) {

            currentEmail = intent.getStringExtra("email_of");


            textViewStreet.setText(intent.getStringExtra("street_of"));
            textViewLat.setText(intent.getStringExtra("lat_of"));
            textViewLong.setText(intent.getStringExtra("long_of"));
            textViewDescription.setText(intent.getStringExtra("descrip_of"));
            textViewSeverity.setText(intent.getStringExtra("severity_of"));
            textViewSize.setText(intent.getStringExtra("size_of"));
            textTime.setText(intent.getStringExtra("time"));

            who = intent.getStringExtra("Off");

            Log.i("detail onChildAdded: ",who);

            if(who.equals("Official")){
                RadioButton5.setEnabled(false);
                RadioButton8.setEnabled(false);
            }else{
                RadioButton6.setEnabled(false);

            }

            ifnoimage=intent.getStringExtra("img_of");
           /* if(ifnoimage.equals("NoImage")){
                reportArrayList = new ArrayList<>();
                dRef = FirebaseDatabase.getInstance().getReference();
                mRef = dRef.child("report");
                Query qRef;
                qRef = mRef.orderByChild("description").equalTo(description);

                qRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        value = dataSnapshot.getValue(Report.class);

                        Log.i(TAG, "onChildAdded: "+value.getEmailId());

                        reportArrayList.add(value);
                        if (value.getDescription()==Desc){
                            byte[] decodedString = Base64.decode(value.getImg(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgView.setImageBitmap(decodedByte);
                        }


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



            }else {*/
                byte[] decodedString = Base64.decode(intent.getStringExtra("img_of"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgView.setImageBitmap(decodedByte);
                imagepass = intent.getStringExtra("img_of");
           // }




            dRef = FirebaseDatabase.getInstance().getReference();
            mRef = dRef.child("report");
            Query qRef;
            qRef = mRef.orderByChild("description").equalTo(description);

            qRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    value = dataSnapshot.getValue(Report.class);

                    Log.i(TAG, "onChildAddedhihihihi: "+value.getEmailId());
                    currentEmail = value.getEmailId();

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





            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail();
                    reportArrayList = new ArrayList<>();
                    if(who.equals("User")) {
                        if (RadioButton5.isChecked()) {
                            statuschange = "still_there";
                        } else if (RadioButton8.isChecked()) {
                            statuschange = "removal_confirmed";
                        }
//                        if(RadioButton6.isChecked())
//                        {
//                            statuschange = "removal_claimed";
//                        }

                    }
                    if(RadioButton6.isChecked())
                    {
                        statuschange = "removal_claimed";
                    }

                    dRef = FirebaseDatabase.getInstance().getReference();
                    mRef = dRef.child("report");
                    Query qRef;
                    if(ifnoimage.equals("NoImage")){
                        qRef = mRef.orderByChild("description").equalTo(intent.getStringExtra("descrip_of"));
                    }else
                    {
                        qRef = mRef.orderByChild("img").equalTo(imagepass);
                    }
                    Log.i(TAG, ">qRef:" + qRef);
                    qRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i(TAG, ">I am here:");
//                            Log.i(TAG, "onDataChange: Off value = "+);
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            String path = "/"+key;
                            HashMap<String, Object> result = new HashMap<>();
                            if(who.equals("Official")) {
                                result.put("currentstatus", statuschange );
                                Log.i(TAG, "onDataChange: if part!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! = "+result.toString());
                                mRef.child(path).updateChildren(result);

                            }

                            else if(who.equals("User")) {
                                result.put("currentstatus", statuschange);
                                Log.i(TAG, "onDataChange: else part!!!!!!!!!!!!!!!!!!!!!!!!!!!!! = "+result.toString());
                                mRef.child(path).updateChildren(result);

                            }
//                            mRef.child(path).updateChildren(result);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i(TAG, ">>> Error:" + "find onCancelled:" + databaseError);

                        }
                    });

                    finish();

                }


            });
        }

    }

    private void initialize(){


        textViewStreet = (TextView) findViewById(R.id.textView22);
        imgView = (ImageView) findViewById(R.id.imageView);
        textViewDescription = (TextView) findViewById(R.id.item_detail);
        textViewSeverity = (TextView) findViewById(R.id.textView24);
        textViewSize = (TextView) findViewById(R.id.textView26);
        textViewLat = (TextView) findViewById(R.id.textView13);
        textViewLong = (TextView) findViewById(R.id.textView17);
        textTime = (TextView) findViewById(R.id.textView7);

    }

    private void sendEmail() {
        //Getting content for email


        Log.i(TAG, " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        Log.i(TAG, "sendEmail: "+currentEmail);

        Log.i(TAG, " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        String email = currentEmail.trim();
        String subject = "Garbage Report Handeled";

        String message = textViewStreet.getText().toString() + " " + "request has been handeled" +" "+textViewDescription.getText().toString();

//        String message = textViewStreet.getText().toString() + " " + "request has been handeled" + " " +time+" "+textViewDescription.getText().toString();
        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
