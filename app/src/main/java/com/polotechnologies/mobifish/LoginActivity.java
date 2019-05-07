package com.polotechnologies.mobifish;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.polotechnologies.mobifish.dataModels.UserAccount;
import com.polotechnologies.mobifish.ui.CreateAccountActivity;
import com.polotechnologies.mobifish.ui.CustomerMainActivity;
import com.polotechnologies.mobifish.ui.FisherManActivity;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Creating an android Button object with the name -->> buttonLogin
    Button buttonLogin;
    Button buttonSignUp;

    FirebaseAuth auth;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Query query;

    //Integer Value to represent the Login with Phone Request
    private static final int RC_PHONE_LOGIN = 100;
    static final int RC_PHONE_SIGNUP = 101;

    //Creating Fire object with the name -->> buttonLogin
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.btn_login);
        buttonSignUp = findViewById(R.id.btn_signUp);

        mFirebaseAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_PHONE_SIGNUP);
    }

    private void loginUser() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_PHONE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);
        switch (requestCode){
            case RC_PHONE_LOGIN:
                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    query = databaseReference.child("users").orderByChild("userID")
                            .equalTo(user.getUid());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot userDetails : dataSnapshot.getChildren()){
                                    UserAccount userAccount =userDetails.getValue(UserAccount.class);

                                    assert userAccount != null;
                                    if(userAccount.userCategory.equals("Fisher Man")){
                                        Intent startActivityForFisherMan = new Intent(LoginActivity.this, FisherManActivity.class);
                                        startActivity(startActivityForFisherMan);
                                        finish();
                                    }else{
                                        Intent startActivityForCustomer = new Intent(LoginActivity.this, CustomerMainActivity.class);
                                        startActivity(startActivityForCustomer);
                                        finish();
                                    }
                                }

                            }else{
                                Toast.makeText(LoginActivity.this, "Invalid User: Kindly Register", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {

                    // Sign in failed. If response is null the user canceled the
                    if ((response != null ? response.getError().getErrorCode() : 0) == ErrorCodes.NO_NETWORK) {
                        printMessage("No Network");
                    } else {
                        printMessage("Cancelled by User");
                    }

                }
                break;
            case RC_PHONE_SIGNUP:
                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    assert user != null;
                    String userID = user.getUid();

                    Intent startAccountCreation = new Intent(LoginActivity.this, CreateAccountActivity.class);
                    startAccountCreation.putExtra("userID",userID);
                    startActivity(startAccountCreation);

                } else {
                    // Sign in failed. If response is null the user canceled the
                    if ((response != null ? response.getError().getErrorCode() : 0) == ErrorCodes.NO_NETWORK) {
                        printMessage("No Network");
                    } else {
                        printMessage("Cancelled by User");
                    }

                }
                break;



        }
        if (requestCode == RC_PHONE_LOGIN) {

        }
    }

    private void printMessage(String message) {
        Toast.makeText(this, "Login Failed: "+ message, Toast.LENGTH_SHORT).show();
    }

}
