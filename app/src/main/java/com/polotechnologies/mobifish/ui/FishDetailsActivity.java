package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.polotechnologies.mobifish.R;

public class FishDetailsActivity extends AppCompatActivity {

    ImageView mFishImage;
    TextView mFishName;
    TextView mFishPrice;
    TextView mFishDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_details);

        Context mContext = getApplicationContext();
        mFishImage = findViewById(R.id.productFishDetailImage);
        mFishName = findViewById(R.id.productFishDetailName);
        mFishDescription = findViewById(R.id.productFishDetailDescription);
        mFishPrice = findViewById(R.id.productFishDetailPrice);

        Intent getFishDetails = getIntent();
        String imageUrl = getFishDetails.getStringExtra("fishImageUrl");
        String fishName = getFishDetails.getStringExtra("fishName");
        String fishDescription = getFishDetails.getStringExtra("fishDescription");
        String fishPrice = getFishDetails.getStringExtra("fishPrice");


        Glide.with(mContext)
                .load(imageUrl)
                .into(mFishImage);
        mFishName.setText(fishName);
        mFishPrice.setText(String.format("KSH: %s", fishPrice));
        mFishDescription.setText(fishDescription);

    }
}
