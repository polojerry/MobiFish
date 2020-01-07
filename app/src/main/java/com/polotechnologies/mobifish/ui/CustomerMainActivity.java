package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
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

public class CustomerMainActivity extends AppCompatActivity implements ProductFishRecyclerAdapter.FishItemClickListener {

    RecyclerView productFishRecyclerView;
    ProductFishRecyclerAdapter productFishRecyclerAdapter;
    List<NewFish> mFishItems;
    DatabaseReference mDatabaseReference;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        mContext = getApplicationContext();
        productFishRecyclerView = findViewById(R.id.rvCustomerMain);

        productFishRecyclerView.setHasFixedSize(false);
        productFishRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mFishItems = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("newFish");

        getLatestData(new OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems,CustomerMainActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getLatestData(new OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems,CustomerMainActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLatestData(new OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems,CustomerMainActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_customer_sign_out :
                signOut();
                finish();
                return true;
            case R.id.action_view_cart:
                Intent startCartActivity = new Intent(CustomerMainActivity.this, CartActivity.class);
                startActivity(startCartActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }


    public interface OnDataReceiveCallback {
        void onDataReceived(DataSnapshot mDataSnapshot);
    }

    private void getLatestData(final OnDataReceiveCallback callback) {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onDataReceived(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onFishItemClick(String fishName, String fishDescription, String fishPrice, String fishImageUrl, String fishId, String fisherManContactNumber) {
        Intent startFishDetailsActivity = new Intent(CustomerMainActivity.this, FishDetailsActivity.class);
        startFishDetailsActivity.putExtra("fishName", fishName);
        startFishDetailsActivity.putExtra("fishDescription", fishDescription);
        startFishDetailsActivity.putExtra("fishPrice", fishPrice);
        startFishDetailsActivity.putExtra("fishImageUrl", fishImageUrl);
        startFishDetailsActivity.putExtra("fisherManContactNumber", fisherManContactNumber);
        startActivity(startFishDetailsActivity);
    }
}
