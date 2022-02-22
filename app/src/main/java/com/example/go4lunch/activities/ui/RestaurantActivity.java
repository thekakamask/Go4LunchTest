package com.example.go4lunch.activities.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsResult;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.UserManager;
import com.example.go4lunch.views.RestaurantAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.example.go4lunch.utils.DatesHours.getCurrentTime;

public class RestaurantActivity extends AppCompatActivity implements Serializable {


    @BindView(R.id.header_pic_resto)
    ImageView mRestoPhoto;
    @BindView(R.id.floating_act_btn)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.resto_name)
    TextView mRestoName;
    @BindView(R.id.resto_address)
    TextView mRestoAddress;
    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.phone_btn)
    Button mPhoneButton;
    @BindView(R.id.star_btn)
    Button mStarButton;
    @BindView(R.id.internet_btn)
    Button mInternetButton;
    @BindView(R.id.restaurant_RV)
    RecyclerView mRestaurantRecyclerView;
    @BindView(R.id.restaurant_activity_layout)
    RelativeLayout mRelativeLayout;

    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;

    private String placeId;
    private RequestManager mGlide;
    private static final String SELECTED = "SELECTED";
    private static final String UNSELECTED = "UNSELECTED";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = db.collection("users");
    private RestaurantAdapter restaurantAdapter;
    private String formattedPhoneNumber;
    private Disposable mDisposable;
    private static final int REQUEST_CALL=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);

        this.starBtn();
        this.floatingBtn();
        this.setUpRV(placeId);
        this.retrieveData();

        //retrieve data when activity is open
        Intent intent= this.getIntent();
        Bundle bundle = intent.getExtras();

        //for like when activity is open
        PlaceDetailsResult placeDetailsResult = null;
        if(bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            final String placeRestaurantId = placeDetailsResult.getPlaceId();
            //UserManager.getInstance().getUserData(Objects.requireNonNull(UserManager.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            UserManager.getInstance().getUserData(UserManager.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null){
                        if (user.getLike() != null && !user.getLike().isEmpty() && user.getLike().contains(placeRestaurantId)) {
                            mStarButton.setBackgroundColor(Color.BLUE);
                        } else {
                            mStarButton.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }
            });
        }

        //for action bar hiding
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }


    }

    // LIKE BUTTON
    private void starBtn() {
        mStarButton.setOnClickListener(view -> restaurantLiked());
    }

    // LIKE/DISLIKE
    public void restaurantLiked() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;
        if (bundle != null) {
            placeDetailsResult= (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            final String placeRestaurantId = placeDetailsResult.getPlaceId();
            UserManager.getInstance().getUserData(UserManager.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                if (user!= null) {
                    if(!user.getLike().isEmpty() && user.getLike().contains(placeRestaurantId)) {
                        UserManager.deleteLike(UserManager.getCurrentUser().getUid(), placeRestaurantId);
                        mStarButton.setBackgroundResource(R.color.starButt_transparent);
                    }else{
                        UserManager.updateLike(UserManager.getCurrentUser().getUid(), placeRestaurantId);
                        mStarButton.setBackgroundResource(R.color.starButt_yellow);
                    }
                }
            });
        }

    }

    //FLOATING BUTTON
    private void floatingBtn() {
        mFloatingActionButton.setOnClickListener( v -> {
            if(v.getId() == R.id.floating_act_btn)
                if (SELECTED.equals(mFloatingActionButton.getTag())) {
                    selectedRestaurant();
                }else if (mFloatingActionButton.isSelected()) {
                    selectedRestaurant();
                } else {
                    removeRestaurant();
                }
        });
    }

    // RETRIEVING SELECTED RESTAURANT
    public void selectedRestaurant() {
        Intent intent= this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;
        if (bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }

        if(placeDetailsResult != null) {
            UserManager.updatePlaceId(Objects.requireNonNull(UserManager.getCurrentUser()).getUid(), placeDetailsResult.getPlaceId(), getCurrentTime());
            mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fui_ic_check_circle_black_128dp));   ;
            mFloatingActionButton.setTag(UNSELECTED);
        }
    }

    // REMOVING RESTAURANT CHOICE
    public void removeRestaurant() {
        UserManager.deletePlaceId(Objects.requireNonNull(Objects.requireNonNull(UserManager.getCurrentUser().getUid())));
        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.valid_done_68dp));
        mFloatingActionButton.setTag(UNSELECTED);
    }

    private void setUpRV(String placeId) {

        Query query = collectionUsers.whereEqualTo("place id", placeId);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.restaurantAdapter = new RestaurantAdapter(options, Glide.with(this));
        mRestaurantRecyclerView.setHasFixedSize(true);
        mRestaurantRecyclerView.setAdapter(restaurantAdapter);
        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // RETRIEVE DATA FOR LISTFRAGMENT
    private void retrieveData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;
        if (bundle!=null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            updateUI(placeDetailsResult,mGlide);
            placeId = placeDetailsResult.getPlaceId();
        }
    }

    // UPDATE UI
    private void updateUI(PlaceDetailsResult placeDetailsResult, RequestManager glide) {
        mGlide=glide;

        //photos with Glide
        if (placeDetailsResult.getPhotos() != null && !placeDetailsResult.getPhotos().isEmpty()) {
            Glide.with(this)
                 .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo_reference=" +
                         placeDetailsResult.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                 .apply(RequestOptions.centerCropTransform())
                 .into(mRestoPhoto);
        }else{
            mRestoPhoto.setImageResource(R.drawable.no_pic);
        }
        //RESTAURANT NAME
        mRestoName.setText(placeDetailsResult.getName());
        //RESTAURANT ADRESS
        mRestoAddress.setText(placeDetailsResult.getVicinity());
        //RESTAURANT RATING
        restaurantRating(placeDetailsResult);
        //RESTAURANT PHONE
        String formattedPhoneNumber = placeDetailsResult.getFormattedPhoneNumber();
        phoneButton(formattedPhoneNumber);
        //RESTAURANT WEBSITE
        String url = placeDetailsResult.getWebsite();
        webButton(url);
    }

    private void restaurantRating(PlaceDetailsResult placeDetailsResult) {
        if (placeDetailsResult.getRating() != null) {
            double restaurantRating = placeDetailsResult.getRating();
            double rating = (restaurantRating/5) *3;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);

        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }

    }

    private void phoneButton(String formattedPhoneNumber) {
        mPhoneButton.setOnClickListener(view -> makePhoneCall(formattedPhoneNumber));

    }

    private void makePhoneCall (String formattedPhoneNumber) {

        if (ContextCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RestaurantActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else if (formattedPhoneNumber != null && !formattedPhoneNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel" + formattedPhoneNumber));
            Log.d("PhoneNumber", formattedPhoneNumber);
            startActivity(intent);
        } else {
            showSnackBar(getString(R.string.error_no_phone_available));
        }
    }



    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(formattedPhoneNumber);
            } else {
                Toast.makeText(this, R.string.error_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void webButton(String url) {
        //mInternetButton.setOnClickListener(view -> makeWebView(url));
        mInternetButton.setOnClickListener(view -> openWebPage(url));
    }

    /*private void makeWebView(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(RestaurantActivity.this, WebViewActivity.class);
            intent.putExtra("website", url);
            Log.d("website",url);
            startActivity(intent);
        } else {
            showSnackBar(getString(R.string.error_no_website));
        }
    }*/

    public void openWebPage(String webUrl) {
        if (webUrl != null && webUrl.startsWith("http")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                showSnackBar(getString(R.string.error_no_website));
            }
        }
    }

    private void showSnackBar (String message) {
        Snackbar.make(mRelativeLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        restaurantAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }


}
