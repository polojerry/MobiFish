package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.dataModels.Cart;

public class FishDetailsActivity extends AppCompatActivity {

    ImageView mFishImage;
    TextView mFishName;
    TextView mFishPrice;
    TextView mFishDescription;

    Button addQuantity;
    Button subtractQuantity;
    Button buttonQuantity;

    String imageUrl;
    String fishName;
    String fishDescription;
    String fishPrice;
    String fisherManContactNumber;

    DatabaseReference mDatabaseReference;

    int quantity = 0;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_details);

        Context mContext = getApplicationContext();
        mFishImage = findViewById(R.id.productFishDetailImage);
        mFishName = findViewById(R.id.productFishDetailName);
        mFishDescription = findViewById(R.id.productFishDetailDescription);
        mFishPrice = findViewById(R.id.productFishDetailPrice);

        addQuantity = findViewById(R.id.addQuantity);
        subtractQuantity = findViewById(R.id.subtractQuantity);
        buttonQuantity = findViewById(R.id.buttonQuantity);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("fishCart");
        Intent getFishDetails = getIntent();
        imageUrl = getFishDetails.getStringExtra("fishImageUrl");
        fishName = getFishDetails.getStringExtra("fishName");
        fishDescription = getFishDetails.getStringExtra("fishDescription");
        fishPrice = getFishDetails.getStringExtra("fishPrice");
        fisherManContactNumber = getFishDetails.getStringExtra("fisherManContactNumber");


        Glide.with(mContext)
                .load(imageUrl)
                .into(mFishImage);
        mFishName.setText(fishName);
        mFishPrice.setText(String.format("KSH: %s", fishPrice));
        mFishDescription.setText(fishDescription);

        buttonQuantity.setText(String.valueOf(quantity));
        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity+=1;
                buttonQuantity.setText(String.valueOf(quantity));
            }
        });
        subtractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity == 0){
                }else{
                    quantity-=1;
                    buttonQuantity.setText(String.valueOf(quantity));
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_to_cart){
            addToCart();

        }
        return super.onOptionsItemSelected(item);

    }

    private void addToCart() {
        int totalPrice = quantity * Integer.valueOf(fishPrice);
        String cartId = mDatabaseReference.push().getKey();

        Cart myCart = new Cart(
                fishName,
                imageUrl,
                String.valueOf(quantity),
                fishPrice,
                String.valueOf(totalPrice),
                cartId,
                fisherManContactNumber,
                mAuth.getCurrentUser().getUid()

        );

        mDatabaseReference.push().setValue(myCart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FishDetailsActivity.this, "Succesfully Added to Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FishDetailsActivity.this, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
