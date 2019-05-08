package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.adapters.CartFishRecyclerAdapter;
import com.polotechnologies.mobifish.adapters.ProductFishRecyclerAdapter;
import com.polotechnologies.mobifish.dataModels.Cart;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements  CartFishRecyclerAdapter.CartItemClickListener{

    Button placeOrder;

    DatabaseReference mDatabaseReference;
    Query query;
    FirebaseAuth mAuth;

    Context mContext;
    String contactNumber;

    RecyclerView cartFishRecyclerView;
    CartFishRecyclerAdapter cartRecyclerAdapter;
    List<Cart> mCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mContext = this;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        cartFishRecyclerView = findViewById(R.id.cartRecycler);

        cartFishRecyclerView.setHasFixedSize(false);
        cartFishRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCartItems = new ArrayList<>();


        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mCartItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    Cart myCart = newFishDataSnapshot.getValue(Cart.class);

                    mCartItems.add(myCart);


                }
                cartRecyclerAdapter = new CartFishRecyclerAdapter(mContext,mCartItems, CartActivity.this);
                cartFishRecyclerView.setAdapter(cartRecyclerAdapter);

            }

        });


    }

    public interface OnDataReceiveCallback {
        void onDataReceived(DataSnapshot mDataSnapshot);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mCartItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    Cart myCart = newFishDataSnapshot.getValue(Cart.class);

                    mCartItems.add(myCart);


                }
                cartRecyclerAdapter = new CartFishRecyclerAdapter(mContext,mCartItems, CartActivity.this);
                cartFishRecyclerView.setAdapter(cartRecyclerAdapter);

            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                mCartItems.clear();
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    Cart myCart = newFishDataSnapshot.getValue(Cart.class);

                    mCartItems.add(myCart);


                }
                cartRecyclerAdapter = new CartFishRecyclerAdapter(mContext,mCartItems, CartActivity.this);
                cartFishRecyclerView.setAdapter(cartRecyclerAdapter);

            }

        });
    }

    private void getLatestData(final CustomerMainActivity.OnDataReceiveCallback callback) {

        query = mDatabaseReference.child("fishCart").orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid());
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
    public void onFishItemClick(String fishName, String fishQuantity, String fishPriceEach, String totalPrice, String contactNumber, String ImageUrl, String cartId) {
        Intent startCartDetailsActivity = new Intent(CartActivity.this, CartDetailsActivity.class);
        startCartDetailsActivity.putExtra("fishName", fishName);
        startCartDetailsActivity.putExtra("fishQuantity", fishQuantity);
        startCartDetailsActivity.putExtra("fishPriceEach", fishPriceEach);
        startCartDetailsActivity.putExtra("totalPrice", totalPrice);
        startCartDetailsActivity.putExtra("contactNumber", contactNumber);
        startCartDetailsActivity.putExtra("ImageUrl", ImageUrl);
        startCartDetailsActivity.putExtra("cartId", cartId);
        startActivity(startCartDetailsActivity);
    }


}
