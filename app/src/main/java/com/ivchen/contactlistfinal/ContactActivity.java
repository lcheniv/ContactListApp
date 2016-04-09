package com.ivchen.contactlistfinal;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.text.format.DateFormat;

import java.sql.SQLException;
//import java.text.DateFormat;

// Extends Fragment Activity - makes your contactactivity a subclass of the FragmentActivity
// This is required to use DialogFragments

// FragmentActivity class is in the android.support.v4.app library, which is a set of code that provides objects to
// make some features in newer Android operating systems work in older versions
public class ContactActivity extends FragmentActivity implements DatePickerDialog.SaveDateListener{

    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Using the method
        initListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        initSaveButton();
        initTextChangedEvents();
        //initChangeDateButton();

        setForEditing(true);

        currentContact = new Contact();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initSettingsButton() {
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsImageButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactSettingsActivity.class);

                // Flag - not to make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void initMapButton() {
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImageButton);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactMapActivity.class);

                // Flag - not to make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void initListButton() {
        ImageButton listButton = (ImageButton) findViewById(R.id.contactImageButton);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactListActivity.class);

                // Flag - not to make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    /*
    The basic save operation opens the database, checks if this is a new contact to be inserted or
    if it should be updated, and if the save was successful, to change the screen back to view
    rather than editing mode.
     */

    public void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard();

                // A new ContactDataSource object is instantiated
                ContactDataSource ds = new ContactDataSource(ContactActivity.this);
                try {
                    // The database is opened. Good practice to open just prior to using it and
                    // closing it as soon as you are done
                    ds.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // A boolean variable is declared and set to false. This variable captures the
                // return value of the ContactDataSource methods and is used to determine the
                // operations that should be performed upon success or failure of the method
                boolean wasSuccessful = false;

                // Only new contacts will have a value of -1
                // if it is a new contact then currentContact is inserted
                // if it is an existing contact then currentContact gets updated
                if(currentContact.getContactID()== -1) {
                    wasSuccessful = ds.insertContact(currentContact);

                    // Uses the newly created retrieval method to get the newly inserted contact's
                    // ID. Sets the currentContact object's ID to th
                    int newId = ds.getLastContactId();
                    currentContact.setContactID(newId);

                } else {
                    wasSuccessful = ds.updateContact(currentContact);
                }

                // The database is closed as soon as possible. It is very important to close
                // the database! Errors can occur if it's not closed.
                ds.close();

                // If the save operation was successful, the ToggleButton is toggled to viewing
                // mode, and the screen is set for viewing. If not, the activity remains in
                // editing mode
                if(wasSuccessful) {
                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.changeButton);
                    editToggle.toggle();
                    //setForViewing();
                }
            }
        });

    }

//    private void initChangeDateButton() {
//        Button changeDate = (Button) findViewById(R.id.birthdayButton);
//        changeDate.setOnClickListener(new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//                DatePickerDialog datePickerDialog = new DatePickerDialog();
//                datePickerDialog.show(fm, "DatePick");
//            }
//
//            @Override
//            public void onClick(View v) {
//                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//                DatePickerDialog datePickerDialog = new DatePickerDialog();
//                datePickerDialog.show(fm, "DatePick");
//            }
//        });
//    }

    public void initTextChangedEvents() {
        // Declared final because it is used inside the event code
        // A reference to the nameEditText field is made to contactName

        // A text changed listener is added to the contact Name edit text
        // The TextWatcher object requires that three methods that are implemented
        // Only needed afterTextChanged method

//------------------------CONTACTNAME------ADDTEXTCHANGEDLISTENER-----------------------------------

        final EditText contactName = (EditText) findViewById(R.id.nameEditText);
        contactName.addTextChangedListener(new TextWatcher() {

            // beforeTextChanged method is executed when the user presses down on a key to enter it into
            // an EditText but before the value in the EditText is actually changed
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // The onTextChanged method is also required TextWatcher method. The method is executed
            // after each and every character change in an EditText
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // This method is called after the user completes editing the data and leaves the EditText
            // This is the event that this app uses to capture the data the user entered
            // This code is executed when the user ends editing of the EditText. It gets the text in
            // EditText, converts it to as tring, and sets the contactName attribute of the currentContact
            // object to that value
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(contactName.getText().toString());
            }
        });

//------------------------STREETADDRESS------ADDTEXTCHANGEDLISTENER---------------------------------

        final EditText streetAddress = (EditText) findViewById(R.id.addressEditText);
        streetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(streetAddress.getText().toString());
            }
        });

//------------------------CITY------ADDTEXTCHANGEDLISTENER------------------------------------------

        final EditText city = (EditText) findViewById(R.id.cityEditText);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(city.getText().toString());

            }
        });

//------------------------STATE------ADDTEXTCHANGEDLISTENER-----------------------------------------

        final EditText state = (EditText) findViewById(R.id.stateEditText);
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(state.getText().toString());

            }
        });

//------------------------ZIPCODE------ADDTEXTCHANGEDLISTENER---------------------------------------

        final EditText zipCode = (EditText) findViewById(R.id.cityEditText);
        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(zipCode.getText().toString());

            }
        });

//----------------------PHONE NUMBER------ADDTEXTCHANGEDLISTENER------------------------------------

        final EditText homePhone = (EditText) findViewById(R.id.homeEditText);
        homePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(homePhone.getText().toString());

            }
        });

//----------------------CELL NUMBER------ADDTEXTCHANGEDLISTENER-------------------------------------

        final EditText cellPhone = (EditText) findViewById(R.id.cellEditText);
        cellPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(cellPhone.getText().toString());

            }
        });

//----------------------E-MAIL------ADDTEXTCHANGEDLISTENER------------------------------------------

        final EditText eMail = (EditText) findViewById(R.id.emailEditText);
        eMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.seteMail(eMail.getText().toString());

            }
        });
//--------------------------------------------------------------------------------------------------
        // This code adds a listener to the phone number EditTexts that calls the
        // PhoneNumberFormattingTextWatcher object, which in turn adds the appropriate formatting
        // as the user types.
        homePhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

//--------------------------------------------------------------------------------------------------
    public void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.changeButton);

        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setForEditing(editToggle.isChecked());
                Log.i("Toggle Button: ", "clicked");
            }
        });
    }

//--------------------------------------------------------------------------------------------------
    private void setForEditing(boolean enabled){
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);
        EditText stateEditText = (EditText) findViewById(R.id.stateEditText);
        EditText zipCodeEditText = (EditText) findViewById(R.id.zipCodeEditText);
        EditText homeEditText = (EditText) findViewById(R.id.homeEditText);
        EditText cellEditText = (EditText) findViewById(R.id.cellEditText);
        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        Button buttonChange = (Button) findViewById(R.id.changeButton);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        nameEditText.setEnabled(enabled);
        addressEditText.setEnabled(enabled);
        cityEditText.setEnabled(enabled);
        stateEditText.setEnabled(enabled);
        zipCodeEditText.setEnabled(enabled);
        homeEditText.setEnabled(enabled);
        cellEditText.setEnabled(enabled);
        emailEditText.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if(enabled){

            nameEditText.requestFocus();

        } else {

            ScrollView s = (ScrollView) findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);

        }
    }

//--------------------------------------------------------------------------------------------------

    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EditText editName = (EditText) findViewById(R.id.nameEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editAddress = (EditText) findViewById(R.id.addressEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editCity = (EditText) findViewById(R.id.cityEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editState = (EditText) findViewById(R.id.stateEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editZipCode = (EditText) findViewById(R.id.zipCodeEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editHomePhone = (EditText) findViewById(R.id.homeEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editCellPhome = (EditText) findViewById(R.id.cellEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editEmail = (EditText) findViewById(R.id.emailEditText);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

    }

//--------------------------------------------------------------------------------------------------

    // Method is the code that will handle the data that the user selected.
    @Override
    public void didFinishDatePickerDialong(Time selectedTime) {

        TextView birthDay = (TextView) findViewById(R.id.birthdayText);

        // Needed to import android.text.format.DateFormat for .format to work
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime.toMillis(false)).toString());

        currentContact.setBirthday(selectedTime);
    }
}
