package com.example.saeed.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

// Dont need to inflate a view, default of ListFragment inflates a layout that defines a
// Full screen ListView.

public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> mCrimes;

    private static final String TAG = "CrimeListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getActivity() returns hosting activity, setTitle() sets title in action bar
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        // Create ArrayAdapter, make it adapter for CrimeListFragments ListView
        // Parameters => Context, ID of layout adapter will use to create view object, data set.
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);

        setListAdapter(adapter);
    }

    // Triggered when hardware button, soft key, or touch of finger takes place.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Get the crime from the adapter
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);

        // Start CrimePagerActivity with this crime
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    // Create subclass of ArrayAdapter to be able to work with crime objects to populate custom view objects
    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes); // pass 0 as we are not using pre-defined layout
        }

        // return view inflated from own custom layout and populate with correct Crime data
       // convertview parameter is existing list item that adapter can reconfigure and return
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /** If we were not given a view, inflate one (can reuse a view and
             *   change parameters rather then make new one)
             */
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            // Configure the view for this crime
            Crime c = getItem(position);

            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }

    }

    // Override onResume to update list when this activity is resumed to top of activity stack
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
