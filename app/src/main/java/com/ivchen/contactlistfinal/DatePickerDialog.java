package com.ivchen.contactlistfinal;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.text.format.Time;


/**
 * Created by Owner on 2/1/2016.
 *
 * Save the DatePickerDialog class. This pattern is always used with custom dialogs. Each dialog needs a listener interface and associated
 * method, a constructor, an onCreateView method, and a call to the listener method. The call to the listener method does not
 * necessarily have to be in its own method as it is here. Finally, the dialog must be dismissed at the end of every code path in the DialogFragment
 *
 * Before the dialog can be tested it must be implemented in the activity that uses it. - ContactActivity
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatePickerDialog extends DialogFragment {

    public void show(FragmentManager fm, String datePick) {
    }

    // A listener must be created with the DialogFragment. This is how the dialog communicates the user's actions on the
    // dialog back to the activity that displayed the dialog.
    // The listener must have a method to report the results of the dialog.
    // The activity will have to implement the listener to handle the user actions
    public interface SaveDateListener {

        void didFinishDatePickerDialong(Time selectedTime);
    }

    // A constructor for the class is required. It almost always is empty.
    public DatePickerDialog() {

        //Empty constructor required for DialogFragment

    }


    // Creating the dialog box
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Guessing this creates the view - and inflates it on front of the user
        final View view = inflater.inflate(R.layout.dataselect, container);
        getDialog().setTitle("Select Date");

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.birthdayPicker);

        Button saveButton = (Button) view.findViewById(R.id.btnOk);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // New time object is created
                Time selectedTime = new Time();
                // Taking the day, month and year
                selectedTime.set(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                saveItem(selectedTime);

            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Closes the dialog if canceled is clicked
                getDialog().dismiss();

            }
        });

        return view;
    }

    // Method to report the selection to the main activity
    private void saveItem(Time selectedTime){

        // Gets a reference to the listener and calls its method to report the results of the dialog
        SaveDateListener activity = (SaveDateListener) getActivity();

        activity.didFinishDatePickerDialong(selectedTime);
        getDialog().dismiss();

    }


}
