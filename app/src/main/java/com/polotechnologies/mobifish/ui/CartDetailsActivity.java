package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.R;

public class CartDetailsActivity extends AppCompatActivity {

    ImageView mCartFishImage;

    TextView mFishName;
    TextView mFishPriceEach;
    TextView mFishTotalPrice;
    TextView mFishQuantity;

    Button placeOrder;
    Button deleteFromCart;

    String contactNumber;
    String fishName;
    String quantity;
    String priceEach;
    String totalPrice;
    String cartId;

    DatabaseReference mDatabaseReference;
    Query query;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);

        mContext = this;

        mCartFishImage = findViewById(R.id.productFishCartImageDetails);

        mFishName= findViewById(R.id.productFishNameCartDetails);
        mFishPriceEach= findViewById(R.id.productFishPriceCartDetails);
        mFishTotalPrice= findViewById(R.id.productFishTotalPriceDetails);
        mFishQuantity = findViewById(R.id.productFishQuantityCartDetails);

        placeOrder= findViewById(R.id.orderFishDetails);
        deleteFromCart = findViewById(R.id.deleteFromCart);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Intent getData = getIntent();

        Glide.with(mContext)
                .load(getData.getStringExtra("ImageUrl"))
                .into(mCartFishImage);
        mFishName.setText(getData.getStringExtra("fishName"));
        mFishPriceEach.setText(String.format("Price Each: %s", getData.getStringExtra("fishPriceEach")));
        mFishQuantity.setText(String.format("Quantity: %s", getData.getStringExtra("fishQuantity")));
        mFishTotalPrice.setText(String.format("Total Price: %s", getData.getStringExtra("fishPriceEach")));

        contactNumber = getData.getStringExtra("contactNumber");
        fishName = getData.getStringExtra("fishName");
        quantity = getData.getStringExtra("fishQuantity");
        priceEach = getData.getStringExtra("fishPriceEach");
        totalPrice = getData.getStringExtra("fishPriceEach");
        cartId = getData.getStringExtra("cartId");



        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeSMSMessage();
            }
        });

        deleteFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });


    }

    public void composeSMSMessage() {

        String x = "New Fish Order \n" + "Fish Name: " + fishName + "\nQuantity: " + quantity + "\nPrice Each:  " + priceEach + "\nTotal Price: " + totalPrice;

        Intent sendSms = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactNumber, null));
        sendSms.putExtra("sms_body", x);
        startActivity(sendSms);
    }

    private void createDialog() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(mContext);

        builder.setMessage("Are you Sure?")
                .setTitle("Remove From Cart").setIcon(R.drawable.warning_icon);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Query applesQuery = mDatabaseReference.child("fishCart").orderByChild("cartId").equalTo(cartId);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            Toast.makeText(mContext, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
