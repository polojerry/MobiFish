package com.polotechnologies.mobifish.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.Objects;

public class NewFishActivity extends AppCompatActivity {

    //Integer variable for requesting image
    static final int REQUEST_IMAGE_GET = 1;

    //Database Reference
    DatabaseReference mDatabaseReference;

    //Firebase Authentication
    FirebaseAuth mAuth;

    //Storage Reference
    StorageReference mStorageReference;

    // Bitmap and Uri for User Selected Image
    Bitmap thumbnail;
    Uri fullPhotoUri;

    //String to hold the uploaded image Url
    String uploadedImageUrl;

    //Widgets to be Used
    TextInputEditText mFishName;
    TextInputEditText mFishPrice;
    TextInputEditText mFishDescriprion;
    Button mPostFish;
    ImageView mNewFishImage;
    ProgressBar newFishProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fish);

        mFishName =findViewById(R.id.newFishName);
        mFishPrice=findViewById(R.id.newFishPrice);
        mFishDescriprion=findViewById(R.id.newFishDescription);
        mPostFish=findViewById(R.id.btn_postFish);
        mNewFishImage = findViewById(R.id.newFishImage);
        newFishProgressBar= findViewById(R.id.progressNewFish);
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("newFish");
        mAuth = FirebaseAuth.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        mNewFishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mPostFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFish();
            }
        });

    }

    private void uploadFish() {
        newFishProgressBar.setVisibility(View.VISIBLE);
        uploadImage();
    }


    /*
        Method used to select image from Gallery
     */
    public void selectImage() {
        checkPermission();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    /*
         Method to check for permission to read External Storage
     */
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }

    }

    /*
       Overriding method to get the selected method using the REQUEST In
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {

            thumbnail = data.getParcelableExtra("data");
            fullPhotoUri = data.getData();

            // Do work with photo saved at fullPhotoUri
            Glide.with(getApplicationContext())
                    .load(fullPhotoUri)
                    .into(mNewFishImage);

        }
    }

    /*
        Method to upload image to firebase storage and getting the download url
     */
    public void uploadImage() {

        String mDownloadUrl;
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String userId = mCurrentUser.getUid();

        String id = mDatabaseReference.push().getKey();

        final StorageReference storageReference = mStorageReference.child("newFishImages/" + id + ".png");
        storageReference.putFile(fullPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String downloadUrl = uri.toString();
                        uploadedImageUrl = downloadUrl;

                        uploadImageUrl(downloadUrl);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewFishActivity.this, "Failed to get Download Link" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewFishActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*
        Method to upload image url to firebase storage and getting the download url
     */
    private void uploadImageUrl(String downloadUrl) {
        String id = mDatabaseReference.push().getKey();

        //Getting the details of the fish

        String newFishName = Objects.requireNonNull(mFishName.getText()).toString().trim();
        String newFishPrice=Objects.requireNonNull(mFishPrice.getText()).toString().trim();
        String newFishDescription=Objects.requireNonNull(mFishDescriprion.getText()).toString().trim();

        String userID = mAuth.getCurrentUser().getUid();
        String contact = mAuth.getCurrentUser().getPhoneNumber();
        NewFish newFish = new NewFish(
                id,
                newFishName,
                newFishPrice,
                newFishDescription,
                downloadUrl,
                userID,
                contact
        );

        assert id != null;
        mDatabaseReference.child(id).setValue(newFish).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                newFishProgressBar.setVisibility(View.GONE);
                Toast.makeText(NewFishActivity.this, "Success Posted Fish", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newFishProgressBar.setVisibility(View.GONE);
                Toast.makeText(NewFishActivity.this, "Failed to Post Fish", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
