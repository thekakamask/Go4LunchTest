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

import com.example.go4lunch.R;
import com.google.rpc.context.AttributeContext;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.main_activity_login_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.google_btn)
    Button mGoogleBtn;
    @BindView(R.id.facebook_btn)
    Button mFacebookBtn;

    private static final int RC_SIGN_IN = 50;
    //pk ce chiffre et pas un autre?



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

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
    }


    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn(View v) {
        this.startSigningWithGoogle();


    }

    @OnClick(R.id.facebook_btn)
    public void onClickFacebookBtn(View v) {
        this.startSigningWithFacebook();


    }

    private void startSigningWithGoogle() {
        startActivityFromFragment(

        );


    }
    private void startSigningWithFacebook() {
        startActivityFromFragment();

    }

    private void createUserInFirestore() {

    }

}