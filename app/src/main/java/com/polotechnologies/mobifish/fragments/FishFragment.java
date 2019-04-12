package com.polotechnologies.mobifish.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.adapters.ProductFishRecyclerAdapter;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishFragment extends Fragment {


    RecyclerView productFishRecyclerView;
    ProductFishRecyclerAdapter productFishRecyclerAdapter;
    List<NewFish> mFishItems;
    DatabaseReference mDatabaseReference;

    Context mContext;

    public FishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity().getApplicationContext();
        productFishRecyclerView = getActivity().findViewById(R.id.mainRecycler);

        productFishRecyclerView.setHasFixedSize(false);
        productFishRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mFishItems = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("newFish");
        getLatestData();


    }

    @Override
    public void onStart() {
        super.onStart();
        getLatestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLatestData();
    }

    private void getLatestData() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : dataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems);
        productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
    }
}
