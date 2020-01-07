package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.adapters.ProductFishRecyclerAdapter;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.ArrayList;
import java.util.List;

public class FisherManActivity extends AppCompatActivity implements  ProductFishRecyclerAdapter.FishItemClickListener{

    RecyclerView productFishRecyclerView;
    ProductFishRecyclerAdapter productFishRecyclerAdapter;
    List<NewFish> mFishItems;


    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;

    Context mContext;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisher_man);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();
        productFishRecyclerView = findViewById(R.id.rvFisherMan);

        productFishRecyclerView.setHasFixedSize(false);
        productFishRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mFishItems = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems, FisherManActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });

        FloatingActionButton fab = findViewById(R.id.fab_new_Fish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFish();
            }
        });
    }

    private void addFish() {
        Intent startNewFishActivity = new Intent(FisherManActivity.this, NewFishActivity.class);
        startActivity(startNewFishActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            signOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems,FisherManActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mFishItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    NewFish latestFishProducts = newFishDataSnapshot.getValue(NewFish.class);

                    mFishItems.add(latestFishProducts);
                }

                productFishRecyclerAdapter = new ProductFishRecyclerAdapter(mContext,mFishItems,FisherManActivity.this);
                productFishRecyclerView.setAdapter(productFishRecyclerAdapter);
            }

        });
    }

    public interface OnDataReceiveCallback {
        void onDataReceived(DataSnapshot mDataSnapshot);
    }

    private void getLatestData(final CustomerMainActivity.OnDataReceiveCallback callback) {

        query = mDatabaseReference.child("newFish").orderByChild("fishermanId").equalTo(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
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
        Intent startFishDetailsActivity = new Intent(FisherManActivity.this, FishDetailsMainActivity.class);
        startFishDetailsActivity.putExtra("fishName", fishName);
        startFishDetailsActivity.putExtra("fishDescription", fishDescription);
        startFishDetailsActivity.putExtra("fishPrice", fishPrice);
        startFishDetailsActivity.putExtra("fishImageUrl", fishImageUrl);
        startFishDetailsActivity.putExtra("fishId", fishId);
        startActivity(startFishDetailsActivity);
    }


}
