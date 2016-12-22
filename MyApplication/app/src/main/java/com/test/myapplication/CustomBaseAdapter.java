package com.test.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

//    private ArrayList<Report> data;
//    private Context context;
//    private ViewHolder viewHolder;

    private ArrayList<Report> reportArrayList;
    private Context context;
    private ViewHolder viewHolder;

    String currentUserEmail;


    DatabaseReference mRef;
    DatabaseReference dRef;

    public CustomBaseAdapter(Context context, ArrayList<Report> reportArrayList){
        this.reportArrayList = reportArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reportArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return reportArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if( view == null ) {

            view = LayoutInflater.from(context).inflate(R.layout.activity_custom_view,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Report report = reportArrayList.get(i);

     //   currentUserEmail = "spurshujjawal@gmail.com";

        //if(report.getEmailId().equals(currentUserEmail)) {


            viewHolder.textViewTitle.setText(report.getTimeNdate());
            viewHolder.textViewdesc.setText(report.getDescription());



            byte[] decodedString = Base64.decode(report.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.imageView.setImageBitmap(decodedByte);

        //}

        return view;

    }

    private class ViewHolder {
        TextView textViewTitle;
        ImageView imageView;
        TextView textViewdesc;

        public ViewHolder(View itemLayout){

            this.imageView = (ImageView) itemLayout.findViewById(R.id.custom_image);
            this.textViewTitle = (TextView) itemLayout.findViewById(R.id.custom_title);
            this.textViewdesc = (TextView) itemLayout.findViewById(R.id.Desc);

        }
    }
}
