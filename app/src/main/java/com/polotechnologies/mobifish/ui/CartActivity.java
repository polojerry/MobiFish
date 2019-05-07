package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.polotechnologies.mobifish.adapters.ProductFishRecyclerAdapter;
import com.polotechnologies.mobifish.dataModels.Cart;
import com.polotechnologies.mobifish.dataModels.NewFish;

public class CartActivity extends AppCompatActivity {

    ImageView cartImage;
    TextView cartFishName;
    TextView cartFishPrice;
    TextView cartFishTotalPrice;

    Button placeOrder;

    DatabaseReference mDatabaseReference;
    Query query;
    FirebaseAuth mAuth;

    Context mContext;
    String contactNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartImage = findViewById(R.id.productFishCart);
        cartFishName= findViewById(R.id.productFishNameCart);
        cartFishPrice= findViewById(R.id.productFishPriceCart);
        cartFishTotalPrice= findViewById(R.id.productFishTotalPrice);

        placeOrder = findViewById(R.id.orderFish);


        mContext = this;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        getLatestData(new CustomerMainActivity.OnDataReceiveCallback(){
            @Override
            public void onDataReceived(DataSnapshot mDataSnapshot) {
                for (DataSnapshot newFishDataSnapshot : mDataSnapshot.getChildren()){
                    Cart myCart = newFishDataSnapshot.getValue(Cart.class);

                    Glide.with(mContext)
                            .load(myCart.getFishImageUrl())
                            .into(cartImage);
                    cartFishName.setText(myCart.getFishName());
                    cartFishPrice.setText(myCart.getFishpriceEach());
                    cartFishTotalPrice.setText(myCart.getFishTotalPrice());

                    contactNumber = myCart.getConctactNumber();

                }
            }

        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public interface OnDataReceiveCallback {
        void onDataReceived(DataSnapshot mDataSnapshot);
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

    public void composeSMSMessage(String jobContactName, String jobContact ) {

        String x = "Hi "+ jobContactName +"i would like to apply for the Job: "  + " you Posted earlier";

        Intent sendSms = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", jobContact, null));
        sendSms.putExtra("sms_body", x);
        startActivity(sendSms);
    }

}
