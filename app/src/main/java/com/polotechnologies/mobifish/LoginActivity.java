package com.polotechnologies.mobifish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Creating an android Button object with the name -->> buttonLogin
    Button buttonLogin;

    //Integer Value to represent the Login with Phone Request
    int RC_PHONE_LOGIN = 100;

    //Creating Fire object with the name -->> buttonLogin
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.btn_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //If user is already authenticated, Fish activity is started
        if (mFirebaseAuth.getCurrentUser() != null){
            Intent openFishActivity = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(openFishActivity);
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
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

        if (requestCode == RC_PHONE_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT).show();

                Intent openFishActivity = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(openFishActivity);
                finish();
            } else {
                // Sign in failed. If response is null the user canceled the
                if ((response != null ? response.getError().getErrorCode() : 0) == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cancelled by User", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
