
package com.test.myapplication;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by NehaRege on 12/8/16.
 */

@IgnoreExtraProperties

public class Report {


    public  String longitude;
    public  String latitude;
    public  String street;
    public  String currentstatus;
    public  String severity;
    public String img;
    public  String size;
    public String timeNdate;
    public  String description;
    public String emailId;
    public List<User> user;

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getStreet() {
        return street;
    }

    public String getCurrentstatus() {
        return currentstatus;
    }

    public String getSeverity() {
        return severity;
    }

    public String getImg() {
        return img;
    }

    public String getSize() {
        return size;
    }

    public String getTimeNdate() {
        return timeNdate;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getUser() {
        return user;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setTimeNdate(String timeNdate) {
        this.timeNdate = timeNdate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Report(String longitude, String latitude, String street, String currentstatus, String severity, String img, String size, String timeNdate, String description, String emailId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.currentstatus = currentstatus;
        this.severity = severity;
        this.img = img;
        this.size = size;
        this.timeNdate = timeNdate;
        this.description = description;
        this.emailId = emailId;
    }

    public Report() {
    }
}
