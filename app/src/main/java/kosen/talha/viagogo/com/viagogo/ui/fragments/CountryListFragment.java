package kosen.talha.viagogo.com.viagogo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kosen.talha.viagogo.com.viagogo.R;


/**
 * A CountryListFragment if needed list can be used as a fragment.
 */
public class CountryListFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "CountryListFragment";

    // Static factory to create fragment
    public static CountryListFragment getInstance() {
        return new CountryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);


        return rootView;
    }


    @Override
    public void onClick(View v) {

    }

}


