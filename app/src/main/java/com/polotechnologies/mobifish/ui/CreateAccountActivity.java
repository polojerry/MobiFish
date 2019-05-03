package com.polotechnologies.mobifish.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.LoginActivity;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.dataModels.UserAccount;

public class CreateAccountActivity extends AppCompatActivity {

    Button buttonSignUpUser;
    TextInputEditText userFullName;
    TextInputEditText userIdNumber;
    Spinner userCategorySpinner;

    ProgressBar accountCreationProgressBar;

    String selectedUserCategory;
    String mUserID;

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Query query;

    String mUserFullName;
    String mUserIdNumber;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        buttonSignUpUser = findViewById(R.id.btn_signUpUser);
        userFullName = findViewById(R.id.userFullName);
        userIdNumber = findViewById(R.id.userIdNumber);
        userCategorySpinner = findViewById(R.id.userCategorySpinner);
        accountCreationProgressBar = findViewById(R.id.accountCreationProgressBar);

        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();

        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();

        Intent getData = getIntent();
        mUserID = getData.getStringExtra("userID");

        ArrayAdapter<CharSequence> categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_category, android.R.layout.simple_spinner_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userCategorySpinner.setAdapter(categorySpinnerAdapter);
        userCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserCategory = parent.getItemAtPosition(position).toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserCategory = "Fisher Man";
            }
        });

        buttonSignUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {

        accountCreationProgressBar.setVisibility(View.VISIBLE);
        assertTextViews();

        query = databaseReference.child("users").orderByChild("userID")
                .equalTo(auth.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(CreateAccountActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    //create new user here
                    UserAccount  userAccount = new UserAccount(
                            mUserID,
                            selectedUserCategory,
                            mUserIdNumber
                    );

                    databaseReference.child("users").push().setValue(userAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            accountCreationProgressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateAccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent startLoginActivity = new Intent (CreateAccountActivity.this, LoginActivity.class);
                            startActivity(startLoginActivity);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            accountCreationProgressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateAccountActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void assertTextViews() {
        if (userFullName.getText().toString().trim().isEmpty()) {
            setTextError(userFullName);
        } else {
            mUserFullName = userFullName.getText().toString().trim();
        }

        if (userIdNumber.getText().toString().trim().isEmpty()) {
            setTextError(userIdNumber);
        } else {
            mUserIdNumber = userIdNumber.getText().toString().trim();
        }

    }

    private void setTextError(TextInputEditText textInputEditText) {
        textInputEditText.setError("Required Field");
    }
}
