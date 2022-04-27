package com.example.go4lunch.activities.ui;

import androidx.activity.result.ActivityResultLauncher;
import butterknife.OnClick;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.databinding.ActivityLoginBinding;
import com.example.go4lunch.utils.UserManager;
import com.example.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import java.util.Collections;
import java.util.List;

// 1µ = CHANGEMENTS DU BIND DES VIEWS DE L'XML ; AVANT UTILISATION DE BUTTERKNIFE ET MAINTENANT
// UTILISATION DE L'HERITAGE DE LA CLASSE BASEACTIVITY QUI ELLE S'OCCUPE DE BINDER LES VIEWS
// CHAQUE CHANGEMENT EST INDIQUE PAR 1µ AU DEBUT
import static android.content.ContentValues.TAG;

// 1µ : AVANT CHANGEMENT : extends AppCompatActivity et @BindView avec le layout et les 2 buttons
// APRES CHANGEMENT : extends BaseActivity<ActivityLoginBinding> (le bind du xml activity_login)
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    /*@BindView(R.id.main_activity_login_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.google_btn)
    Button mGoogleBtn;
    @BindView(R.id.facebook_btn)
    Button mFacebookBtn;*/

    private final UserManager userManager = UserManager.getInstance();

    //1µ : DEBUT AJOUT (inflate du layout de activity_login)
    @Override
    ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }
    //1µ :FIN AJOUT

    // 1µ : AVANT CHANGEMENT : protected void onCreate (au lieu de public) et setContentView(R.layout.activity_login)
    // et ButterKnife.bind(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: " + BuildConfig.API_KEY);

        //ButterKnife.bind(this);
    }



    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn(View v) {
        this.startSigningWithGoogle();
    }

    //CREATE USER IN FIREBASE FOR GOOGLE
    private void startSigningWithGoogle() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // launch the activity
        //REGISTER FOR ACTIVITY;
        // launch the activity
        //REGISTER FOR ACTIVITY;
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);


        /*startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);*/
    }



    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.facebook_btn)
    public void onClickFacebookBtn(View v) {
        this.startSigningWithFacebook();
    }


    //CREATE USER IN FIREBASE FOR FACEBOOK
    private void startSigningWithFacebook() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // launch the activity
        //REGISTER FOR ACTIVITY;
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);

        /*startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);*/

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }*/

    //UTILISER UNE LAMBA A LA PLACE DE LINSTANTIATION DE LINTERFACE (ACTIVITYRESULTCALLBACK) avec la variable (result)
    // VERIFIER QUE ACTIVITYRESULTCALLBACK EST BIEN UNE INTERFACE GENERIQUE
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    //CREATE MESSAGE AFTER USER CONNECTION USE BY THE RESPONSE AFTER SIGN IN
    // 1µ : REMPLACEMENT DE mRelativeLayout (le bind etait fait ligne 47/48)
    // par binding.main_activity_login_layout (l'id du relative layout dans l'xml : navigation_bottom  mais sans le _)
    private void showSnackBar (String message) {
        Snackbar.make(binding.mainActivityLoginLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //REPONSE AFTER SIGN IN
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            //SUCCESSFULLY SIGN IN
            showSnackBar(getString(R.string.connection_succeed));
            String uid = userManager.getCurrentUser().getUid();
            this.createUserInFirestore(uid);
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);


        }else {
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







    /*private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

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


    }*/

    private void createUserInFirestore(String uid){
        userManager.createUser(uid);

    }
}