package com.example.go4lunch.activities.ui;

import androidx.annotation.BinderThread;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.go4lunch.utils.UserManager;
import com.example.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.rpc.context.AttributeContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.main_activity_login_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.google_btn)
    Button mGoogleBtn;
    @BindView(R.id.facebook_btn)
    Button mFacebookBtn;

    private static final int RC_SIGN_IN = 100;
    //pk ce chiffre et pas un autre?

    private UserManager mUserManager = UserManager.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }



    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn(View v) {
        this.startSigningWithGoogle();


    }

    @OnClick(R.id.facebook_btn)
    public void onClickFacebookBtn(View v) {
        this.startSigningWithFacebook();


    }

    //CREATE USER IN FIREBASE FOR GOOGLE
    private void startSigningWithGoogle() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);;


    }

    //CREATE USER IN FIREBASE FOR FACEBOOK
    private void startSigningWithFacebook() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);

    }

    //CREATE MESSAGE AFTER USER CONNECTION USE BY THE RESPONSE AFTER SIGN IN
    private void showSnackBar (String message) {
        Snackbar.make(mRelativeLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //REPONSE AFTER SIGN IN
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            //SUCCESS
            if (resultCode == RESULT_OK) {
                showSnackBar(getString(R.string.connection_succeed));
                this.createUserInFirestore();
                Intent loginIntent = new Intent(this, MainActivity.class);
                startActivity(loginIntent);
            } else {
                //ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError()!= null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }


    }

    private void createUserInFirestore(){
        UserManager.createUser();

    }
}