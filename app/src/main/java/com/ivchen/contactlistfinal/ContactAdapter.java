package com.ivchen.contactlistfinal;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Owner on 2/15/2016.
 */

// ContactAdapterClass is declared as a subclass of ArrayAdapter that has been parametrized to hold
// only Contact objects
public class ContactAdapter extends ArrayAdapter<Contact> {

    // Two variables are declared for the class. The items variable holds the ArrayList of Contact
    // objects that have been retrieved from the database. The adapterContext variable holds a
    // reference to the context, in this case the ContactListActivity, where the list is being
    // displayed.

    private ArrayList<Contact> items;
    private Context adapterContext;


    // The constructor method for the ContactAdapter class is passed the context and the ArrayList
    // of contacts. It calls its super class constructor method (ArrayAdapter), passing it the
    // context, contacts, and the layout file used for the items. It then assigns the contacts,
    // and the context that were passed in to the items and c variables, respectively.
    public ContactAdapter(Context context, ArrayList<Contact> items) {
        super(context, R.layout.list_item, items);
        Context c = context;
        this.items = items;
    }


    // This method is the workhorse of this class. This method is called for every item in the
    // underlying data source up to the number of list items that can be displayed in the ListView.
    // As the user scrolls through the list, this method is called to display contacts in the
    // ArrayList as they are scrolled into view. The ListView passes the index of the item to be
    // displayed and the View, which is the lsit item layout if it exits, null if it does not.
    // This is done so that the list item views can be reused as they scroll off the screen rather
    // than re-creating a new view. This saves system resources.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        try {

            Contact contact = items.get(position);

            if(v == null) {

                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);

            }

            TextView contactName = (TextView) v.findViewById(R.id.textContactName);
            TextView contactNumber = (TextView) v.findViewById(R.id.textPhoneNumber);

            Button b = (Button) v.findViewById(R.id.buttonDeleteContact);

            contactName.setText(contact.getContactName());
            contactNumber.setText(contact.getPhoneNumber());
            b.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return v;
    }

    public void showDelete(final int position, final View convertView, final Context context, final Contact contact) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);

        if (b.getVisibility()==View.INVISIBLE) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView, context);
                    items.remove(contact);
                    deleteOption(contact.getContactID(), context);
                }

            });
        }
        else {
            hideDelete(position, convertView, context);
        }
    }

    private void deleteOption(int contactToDelete, Context context) {
        ContactDataSource db = new ContactDataSource(context);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.deleteContact(contactToDelete);
        db.close();
        this.notifyDataSetChanged();
    }

    private void hideDelete(int position, View convertView, Context context) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }
}
