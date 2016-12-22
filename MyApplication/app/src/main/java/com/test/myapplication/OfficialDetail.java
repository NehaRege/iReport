package com.test.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
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
import static com.test.myapplication.R.id.radioButton8;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
    Button update;
    TextView textViewEmail;
    RadioButton RadioButton5;
    RadioButton RadioButton6;
    RadioButton RadioButton8;
    DatabaseReference mRef;
    DatabaseReference dRef;
    DatabaseReference rRuf;
    String who;
    String statuschange;
    String description;
    String imagepass;
    ArrayList<Report> reportArrayList;

    Report value;
    ListView mListView;


    ImageView imgView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_item_detail);
        RadioButton5 = (RadioButton)findViewById(radioButton5);
        RadioButton6 = (RadioButton)findViewById(radioButton6);
        RadioButton8 = (RadioButton)findViewById(radioButton8);
        update = (Button)findViewById(button3);
        final Intent intent = getIntent();

        initialize();
description =intent.getStringExtra("descrip_of");

        if(intent!=null) {

            textViewStreet.setText(intent.getStringExtra("street_of"));
            textViewLat.setText(intent.getStringExtra("lat_of"));
            textViewLong.setText(intent.getStringExtra("long_of"));
            textViewDescription.setText(intent.getStringExtra("descrip_of"));
            textViewSeverity.setText(intent.getStringExtra("severity_of"));
            textViewSize.setText(intent.getStringExtra("size_of"));
            who = intent.getStringExtra("Off");
            Log.i("detail onChildAdded: ",who);
            if(who.equals("Official")){
                RadioButton5.setEnabled(false);
                RadioButton8.setEnabled(false);
            }else{
                RadioButton6.setEnabled(false);

            }


            byte[] decodedString = Base64.decode(intent.getStringExtra("img_of"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgView.setImageBitmap(decodedByte);
            imagepass = intent.getStringExtra("img_of");

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportArrayList = new ArrayList<>();
                 /*   if(who.equals("User")) {
                        if (RadioButton5.isChecked()) {
                            statuschange = "still_there";
                        } else if (RadioButton8.isChecked()) {
                            statuschange = "removal_confirmed";
                        }
                    }*/

                    dRef = FirebaseDatabase.getInstance().getReference();
                    mRef = dRef.child("report");
                    Query qRef;
                    qRef = mRef.orderByChild("img").equalTo(imagepass);
                    qRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            String path = "/"+key;
                            HashMap<String, Object> result = new HashMap<>();
                            //if(who=="Off")
                                result.put("currentstatus", "removal_claimed");
                            //else if(who=="User") {
                                //result.put("currentstatus", statuschange);
                            //}
                            mRef.child(path).updateChildren(result);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i(TAG, ">>> Error:" + "find onCancelled:" + databaseError);

                        }
                    });

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

    }
}
