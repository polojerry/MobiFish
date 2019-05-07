package com.polotechnologies.mobifish.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class FishDetailsMainActivity extends AppCompatActivity {

    ImageView mFishImage;
    TextView mFishName;
    TextView mFishPrice;
    TextView mFishDescription;

    Button deleteFish;

    Context mContext;

    String fishId;

    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_details_main);

        mFishImage = findViewById(R.id.productFishDetailImageFisherman);
        mFishName = findViewById(R.id.productFishDetailNameFisherman);
        mFishDescription = findViewById(R.id.productFishDetailDescriptionFisherman);
        mFishPrice = findViewById(R.id.productFishDetailPriceFisherman);
        deleteFish = findViewById(R.id.deleteFish);

        Intent getFishDetails = getIntent();
        String imageUrl = getFishDetails.getStringExtra("fishImageUrl");
        String fishName = getFishDetails.getStringExtra("fishName");
        String fishDescription = getFishDetails.getStringExtra("fishDescription");
        String fishPrice = getFishDetails.getStringExtra("fishPrice");
        fishId = getFishDetails.getStringExtra("fishId");

        mContext = this;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        Glide.with(mContext)
                .load(imageUrl)
                .into(mFishImage);
        mFishName.setText(fishName);
        mFishPrice.setText(String.format("KSH: %s", fishPrice));
        mFishDescription.setText(fishDescription);

        deleteFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }


    private void createDialog() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(mContext);

        builder.setMessage("Are you Sure?")
                .setTitle("Delete This Fish").setIcon(R.drawable.warning_icon);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Query applesQuery = mDatabaseReference.child("newFish").orderByChild("fishId").equalTo(fishId);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot fishSnapshot: dataSnapshot.getChildren()) {
                            fishSnapshot.getRef().removeValue();
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
