package com.ivchen.contactlistfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Owner on 2/9/2016.
 */
public class ContactDataSource {

    // Variables are declared to hold instances of the SQLite database and the helper class.
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    // The helper class is instantiated when the data source class is instantiated.
    public ContactDataSource(Context context){
        dbHelper = new ContactDBHelper(context);
    }

    // Open and close methods are used to access and end access to the database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //------------------------------------------------------------------------------------

    /*

    The primary difference between the two method is that the updateContact method
    uses the Contact ID to overwrite values in the Contact table, whereas the insertContact
    method just inserts contact data and the database inserts the ID because the _id field
    was declared as an autoincrement field.

     */


    // Both methods return a boolean value to tell the calling code if the operation succeeded
    // boolean value is initially 'false' and then changed to true only if the operation succeeds
    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {

            // This object is used to store a set of key/value pairs that are used to assign
            // contact data to the correct field in the table.
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.geteMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

            // The database's insert method is called and passed the name of the table and values
            // to insert. The method returns the number of records (rows) successfully inserted.
            // The value is compared to zero. If greater than zero, then the operation succeeded and the
            // return value is true.
            didSucceed = database.insert("contact", null, initialValues) > 0;

        } catch (Exception e) {
            // Do nothing - will return false if there is no exception
        }
        return didSucceed;
    }

    //------------------------------------------------------------------------------------

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;

        try{
            // Update method needs the contact's ID to correctly update the table.
            // Retrieved from Contact class and assigned to rowId
            Long rowId = Long.valueOf(c.getContactID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.geteMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

            // If the operation succeeds, the method returns the number of records affected.
            // If this number is greater than zero, the operation was successful
            didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;

        } catch (Exception e) {
            // Do nothing - will retrn false if there is an exception
        }
        return didSucceed;
    }

    //----------------------------------------------------------------------------------------------

    public int getLastContactId() {

        int lastId = -1;

        try {

            // SQL query written to get the maximum value for the _id field in the contact table
            // The last contact entered will have the maximum value because the _id field is
            // set to auto increment

            // A cursor is declared and assigned to hold the results of the execution of the query
            // A cursor is an object that is used to hold and move through the results of a query
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);

            // Cursor is told to move to the first record in the returned data
            // The maximum ID is retrieved from the recordset. Fields in the recordset are indexed
            // starting at 0
            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();

        } catch (Exception e) {

            lastId = -1;

        }
        return lastId;
    }

    //----------------------------------------------------------------------------------------------

    // RETRIEVING CONTACT DATA

    public ArrayList<String> getContactName() {

        ArrayList<String> contactNames = new ArrayList<String> ();

        try {

            // SQL query set up to return the contactname field for all records in the contact table
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();

            // Primary difference - query can return more than one record to the cursor, so
            // you have to implement a loop to retrieve all the records.
            // Loop is set up to go through all the records in the cursor.
            // Loop starts at the first record in the cursor
            // Contact name is added to the array list and the cursor advances to the next record
            // by using cursor.moveToNext();
            while(!cursor.isAfterLast()) {

                contactNames.add(cursor.getString(0));
                cursor.moveToNext();

            }

            cursor.close();

        } catch (Exception e) {

            // In case it crashes, the arraylist is set to a new empty arraylist.
            // This way, the calling Activity can test for an empty list to determine if the
            // retrieve was successful
            contactNames = new ArrayList<String>();

        }

        return contactNames;

    }

    //----------------------------------------------------------------------------------------------

    public ArrayList<Contact> getContacts() {

        // A new Contact object is instantiated for each record in the cursor. All the values in the
        // record are added to the appropriate attribute in the new object.
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        try {

            String query = "";
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                newContact = new Contact();

                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));

                // A new Time object is created to hold the contact's birthday. Dates are stored in
                // millis, so the time object is set to the proper date using the set(long millis)
                // method. After the birthday is created, it is inserted into the Contact object.

                Time t = new Time();
                t.set(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(t);

                contacts.add(newContact);
                cursor.moveToNext();

            }
            cursor.close();

        } catch (Exception e) {

            contacts = new ArrayList<Contact>();

        }

        return contacts;

    }

    public boolean deleteContact(int contactId) {
        boolean didDelete = false;

        try {

            didDelete = database.delete("contact", "_id=" + contactId, null) > 0;

        } catch (Exception e) {

            // Do nothing - return value already set to false

        }
        return didDelete;
    }

}
