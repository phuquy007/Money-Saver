package com.phuquytran_300303518.moneysaver.model;

import com.google.firebase.database.DataSnapshot;

public class User {
    private String name;
    private String email;
    private String dob;
    private String phoneNumber;

    public User(){
        this.name = "";
        this.email = "";
        this.dob = "";
        this.phoneNumber = "";
    }

    public User(String name, String email, String dob, String phoneNumber){
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public static User fromFirebase(DataSnapshot snapshot){
        User user = new User();
        user.setName(snapshot.child("name").getValue(String.class));
        user.setEmail(snapshot.child("email").getValue(String.class));
        user.setDob(snapshot.child("dob").getValue(String.class));
        user.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));

        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
