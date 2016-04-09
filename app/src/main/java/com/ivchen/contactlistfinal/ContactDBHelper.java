package com.ivchen.contactlistfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Owner on 2/9/2016.
 */

// ContactDBHelper is a subclass of SQLiteOpenHelper and inherits all of it's functions
public class ContactDBHelper extends SQLiteOpenHelper{

    // Use .db extension for database name
    // Version number is very important - Every time the database is accessed, the existing
    // database version is compared to the one here. If the number is higher, the onUpgrade method is executed.
    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    // This String variable is an SQL command that creates the table
    // It is good practice to define the table definitions in this manner so that when you change to a table
    // needs to be made, all you have to do is change the definition in one place and increment the version #
    private static final String CREATE_TABLE_CONTACT = "create table contact (_id integer primary key autoincrement, "
            + "contactname text not null, streetaddress text, "
            + "city text, state text, zipcode text, "
            + "phonenumber text, cellnumber text, "
            + "email text, birthday text);";

    // This is the Constructor method
    // Calls the super class's construct method.
    // Constructor creates a new instance of ContactDBHelper
    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // This method is called the first time the database is opened.
    // If the database named in the DATABASE_NAME variable does not exist, this method is executed.
    // The method executes the SQL assigned to the CREATE_TABLE_CONTACT variable
    // db.execSQL runs the String variable which was SQL code
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    // This method is executed when the database is opened and the current version number in the code
    // is higher than the version number of the current database.
    // This method first deletes the contact table and then executes the onCreate method to create
    // a new version of the table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
