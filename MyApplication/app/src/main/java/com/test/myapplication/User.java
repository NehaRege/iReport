package com.test.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by NehaRege on 12/8/16.
 */

@IgnoreExtraProperties

public class User {
    
    public String firstname;
    public String lastname;
    //public String userid;
    public String useremail;
    public String address;
    public String emailConfirmation;
    public String emailNotification;
    public String residentAnonymous;


    public List<Report> reportList;

    public User(String firstname, String lastname, String useremail, List<Report> reportList,String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        //this.userid = userid;
        this.useremail = useremail;
        this.reportList = reportList;
        this.address = address;
    }

    public User(String firstname, String lastname, String useremail, String address, String emailConfirmation, String emailNotification) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.useremail = useremail;
        this.address = address;
        this.emailNotification = emailNotification;
        this.emailConfirmation = emailConfirmation;
    }

    public User(String firstname, String lastname, String useremail, String address, String emailConfirmation/*, String emailNotification*/) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.useremail = useremail;
        this.address = address;
//        this.emailNotification = emailNotification;
        this.emailConfirmation = emailConfirmation;
    }







    public User() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

//    public String getUserid() {
//        return userid;
//    }

//    public void setUserid(String userid) {
//        this.userid = userid;
//    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }


}

