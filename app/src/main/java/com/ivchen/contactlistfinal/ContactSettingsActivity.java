package com.ivchen.contactlistfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.sql.SQLException;

public class ContactSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListButton();
        initMapButton();
        initSortByClick();
        initSortOrderClick();
        initSettings();

        final RelativeLayout settingsLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        final RadioButton chooseColor1 = (RadioButton) findViewById(R.id.radioColor1);
        final RadioButton chooseColor2 = (RadioButton) findViewById(R.id.radioColor2);
        final RadioButton chooseColor3 = (RadioButton) findViewById(R.id.radioColor3);

        chooseColor1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                settingsLayout.setBackgroundResource(R.color.background1);

            }
        });

        chooseColor2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                settingsLayout.setBackgroundResource(R.color.background2);

            }
        });

        chooseColor3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                settingsLayout.setBackgroundResource(R.color.background3);

            }
        });




        //initSettingsButton();

    }

    public void initSettingsButton() {
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsImageButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactSettingsActivity.class);

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
                Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);

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
                Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);

                // Flag - not to make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    // This method gets the values tored in SharedPreferences to set the radiobuttons to the value that the user checks
    public void initSettings() {

        // A string variable is declared. Value for the field to sort contacts is retrieved by SharedPreferences
        // The getPreferences method is used to get the SharedPreferences object. SharedPreference file is opened as a private
        // object. The getString method is called on the SharedPreferences object to retrieve the string value associated with the
        // sortfield key. If there is not value stored for that key, the default value of contactname is assigned and same for 2nd line

        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        String chooseColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("choosecolor", "color");

        // A reference to each radio button is assigned to a variable
        // Value retrieved for sort field is evaluated to determine which radio button to be checked

        RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
        RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
        RadioButton rbBirthDay = (RadioButton) findViewById(R.id.radioBirthday);

        if(sortBy.equalsIgnoreCase("contactname")){

            rbName.setChecked(true);

        } else if(sortBy.equalsIgnoreCase("city")) {

            rbCity.setChecked(true);

        } else {

            rbBirthDay.setChecked(true);

        }


        RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
        RadioButton rbDescending = (RadioButton) findViewById(R.id.radioDescending);

        if(sortOrder.equalsIgnoreCase("ASC")) {

            rbAscending.setChecked(true);

        } else {

            rbDescending.setChecked(true);

        }

        RadioButton chooseColor1 = (RadioButton) findViewById(R.id.radioColor1);
        RadioButton chooseColor2 = (RadioButton) findViewById(R.id.radioColor2);
        RadioButton chooseColor3 = (RadioButton) findViewById(R.id.radioColor3);

        if(chooseColor.equalsIgnoreCase("color1")){

            chooseColor1.setChecked(true);

            if(chooseColor1.isChecked()){
                getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("choosecolor", "color1").commit();
            }

        } else if (chooseColor.equalsIgnoreCase("color2")){

            chooseColor2.setChecked(true);

            if(chooseColor2.isChecked()){
                getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("choosecolor", "color2").commit();
            }

        } else {

            chooseColor3.setChecked(true);

            if(chooseColor3.isChecked()){
                getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("choosecolor", "color3").commit();
            }

        }

    }

    private void initSortByClick() {

        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroup1);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
                RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);

                if(rbName.isChecked()) {

                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "contactname").commit();

                } else if(rbCity.isChecked()) {

                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "city").commit();

                } else {

                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "birthday").commit();

                }
            }
        });

    }

    private void initSortOrderClick() {

        RadioGroup rgSortOrder = (RadioGroup) findViewById(R.id.radioGroup2);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);

                if (rbAscending.isChecked()) {

                    // getPreferences gets a reference to the SharedPreferences object using private mode
                    // Then it sends the message edit() to the SharedPreferences object to open it for editing
                    // Next, the message putString is sent to the editable SharedPreferences object to save the value
                    // The first parameter in the putString method is the key and the second is the value to be saved.
                    // Finally commit() is sent to the changed SharedPreferences object to make the changes persist
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").commit();

                } else {

                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").commit();

                }
            }
        });

    }

}
