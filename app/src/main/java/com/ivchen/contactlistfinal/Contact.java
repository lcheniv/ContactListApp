package com.ivchen.contactlistfinal;

import android.text.format.Time;

/**
 * Created by Owner on 2/9/2016.
 */

// This class declares variables for each piece of data needed for a contact and declares a method
// to set the value of the variable and a method to get the value of the variable (getters and setters).
// Notice that contact's ID is set to -1. This is used by the app to determine if the contact is new and
// needs to be inserted or the contact already exists and needs to be updated.

public class Contact {

    private int contactID;
    private String contactName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String eMail;
    private Time birthday;

    public Contact() {

        contactID = -1;
        Time t = new Time();
        t.setToNow();
        birthday = t;

    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int i) {
        contactID = i;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String s) {
        contactName = s;
    }

    public Time getBirthday() {
        return birthday;
    }

    public void setBirthday(Time t){
        birthday = t;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String s){
        streetAddress = s;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String s) {
        city = s;
    }

    public String getState() {
        return  state;
    }

    public void setState(String s) {
        state = s;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String s){
        zipCode = s;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String s) {
        phoneNumber = s;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String s){
        cellNumber = s;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String s) {
        eMail = s;
    }

}
