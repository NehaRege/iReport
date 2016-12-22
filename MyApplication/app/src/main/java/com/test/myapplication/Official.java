//package com.test.myapplication;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CursorAdapter;
//import android.widget.ListView;
//
///**
// * Created by NehaRege on 12/11/16.
// */
//public class Official extends AppCompatActivity {
//
//    private CursorAdapter cursorAdapter;
//    private Cursor reportSelectedCursor;
//
//    private ListView listView;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_official);
//
//        initViews();
//
//
//        cursorAdapter = new CursorAdapter(Official.this,reportSelectedCursor,0) {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//
//                return LayoutInflater.from(context).inflate(R.layout.activity_custom_view, viewGroup, false);
//
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//
//            }
//        }
//
//    }
//
//    public void initViews(){
//        listView = (ListView) findViewById(R.id.listView);
//    }
//
//
//}
