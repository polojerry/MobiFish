package com.polotechnologies.mobifish.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polotechnologies.mobifish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishDetailsFragment extends Fragment {


    public FishDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fish_details, container, false);
    }

}
