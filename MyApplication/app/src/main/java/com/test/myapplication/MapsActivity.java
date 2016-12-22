package com.test.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.NumberFormat;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static android.R.attr.format;
import static android.R.attr.mapViewStyle;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.test.myapplication.R.id.lat;
import static com.test.myapplication.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    SharedPreferences sharedPreferences;
    public String latReport;
    public String longiReport;
    public String streetReport;
    public String repClicked;

    private GoogleMap Mmap;
    DatabaseReference mRef;
    DatabaseReference dRef;
    String login_email;
    String fb_email;
    String gmail;
    String email;
    String Data;
    HashMap<Marker, String> Imagemap = new HashMap<>();
    // ArrayList<Report> reportArrayList;
    ArrayList<String> Lat = new ArrayList<>();
    ArrayList<String> Lng = new ArrayList<>();
    ArrayList<String> Street = new ArrayList<>();
    ArrayList<String> emailid = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> size = new ArrayList<>();
    ArrayList<String> severity = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    //ArrayList<String> Street = new ArrayList<>();
    Report value;
    Query queryRef;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        Intent intent = getIntent();
        String markerimage;
        Lat = intent.getStringArrayListExtra("latlist");
        Lng = intent.getStringArrayListExtra("longlist");
        Street = intent.getStringArrayListExtra("Street");
        img = intent.getStringArrayListExtra("Image");
        description = intent.getStringArrayListExtra("Desc");
        emailid = intent.getStringArrayListExtra("Email");
        severity = intent.getStringArrayListExtra("Severity");
        size = intent.getStringArrayListExtra("Size");

        for (i = 0; i < Lat.size(); i++) {
            latReport = Lat.get(i);
            longiReport = Lng.get(i);
            streetReport = Street.get(i);

            double number = Double.parseDouble(latReport);
            double number2 = Double.parseDouble(longiReport);

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(number, number2))
                    .title(streetReport));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(number, number2), 15));
            Mmap = map;
            String cimage = img.get(i);
            Imagemap.put(marker,cimage);
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerimage = Imagemap.get(marker);
                Intent intentnew = new Intent(getApplicationContext(), OfficialDetail.class);
                for (int iter = 0; iter < Lat.size(); iter++) {
                    if (markerimage.equals(img.get(iter)))
                    {
                        Data = Street.get(iter);
                        intentnew.putExtra("street_of", Data);
                        Data = Lat.get(iter);
                        intentnew.putExtra("lat_of", Data);
                        Data = Lng.get(iter);
                        intentnew.putExtra("long_of", Data);
                        Data = description.get(iter);
                        intentnew.putExtra("lat_of", Data);
                        Data = size.get(iter);
                        intentnew.putExtra("size_of", Data);
                        Data = severity.get(iter);
                        intentnew.putExtra("severity_of", Data);
                        Data = img.get(iter);
                        intentnew.putExtra("img_of", Data);
                        intentnew.putExtra("Off","User");
                        break;
                    }
                }
                startActivity(intentnew);
                return false;
            }
        });
    }
}