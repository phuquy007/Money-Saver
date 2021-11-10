package com.phuquytran_300303518.moneysaver.Entities;

import com.google.firebase.database.DataSnapshot;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String dob;
    private String phoneNumber;


    public User(){
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.dob = "";
        this.phoneNumber = "";
    }

    public User(String firstName, String email, String dob, String phoneNumber){
        this.firstName = firstName;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public static User fromFirebase(DataSnapshot snapshot){
        User user = new User();
        user.setFirstName(snapshot.child("firstName").getValue(String.class));
        user.setLastName(snapshot.child("lastName").getValue(String.class));
        user.setEmail(snapshot.child("email").getValue(String.class));
        user.setDob(snapshot.child("dob").getValue(String.class));
        user.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));

        return user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
