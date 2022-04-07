package com.example.go4lunch.activities.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.databinding.ActivityRestaurantBinding;
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

// 1µ = CHANGEMENTS DU BIND DES VIEWS DE L'XML ; AVANT UTILISATION DE BUTTERKNIFE ET MAINTENANT
// UTILISATION DE L'HERITAGE DE LA CLASSE BASEACTIVITY QUI ELLE S'OCCUPE DE BINDER LES VIEWS
// CHAQUE CHANGEMENT EST INDIQUE PAR 1µ AU DEBUT

// 1µ : AVANT CHANGEMENT : extends AppCompatActivity et @BindView avec le recyclerview, le layout et tous les elements du XML
// APRES CHANGEMENT : extends BaseActivity<ActivityRestaurantBinding> (le bind du xml activity_main)
public class RestaurantActivity extends BaseActivity<ActivityRestaurantBinding> implements Serializable {


    /*@BindView(R.id.header_pic_resto)
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
    RelativeLayout mRelativeLayout;*/


    String GOOGLE_MAP_API_KEY = BuildConfig.API_KEY;

    private String placeId;
    private RequestManager mGlide;
    private static final String SELECTED = "SELECTED";
    private static final String UNSELECTED = "UNSELECTED";
    //change after update (put final at db)
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //change after update (put final at collectionUsers)
    private final CollectionReference collectionUsers = db.collection("users");
    private RestaurantAdapter restaurantAdapter;
    private String formattedPhoneNumber;
    private Disposable mDisposable;
    private static final int REQUEST_CALL=100;

    //1µ : DEBUT AJOUT (inflate du layout de activity_restaurant)
    @Override
    ActivityRestaurantBinding getViewBinding() {
        return ActivityRestaurantBinding.inflate(getLayoutInflater());
    }
    //1µ :FIN AJOUT

    // 1µ : AVANT CHANGEMENT : protected void onCreate (au lieu de public) et setContentView(R.layout.activity_restaurant)
    // et ButterKnife.bind(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);*/

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

                            //1µ : remplacement de mStarButton (qui etait lié avec @BindView(R.id.star_btn) Button mStarButton;
                            // par binding.starBtn (star_btn (id de l'xml) sans le _)

                            binding.starBtn.setBackgroundColor(Color.BLUE);
                        } else {
                            binding.starBtn.setBackgroundColor(Color.TRANSPARENT);
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
    //1µ : remplacement de mStarButton (qui etait lié avec @BindView(R.id.star_btn) Button mStarButton;
    // par binding.starBtn (star_btn (id de l'xml) sans le _)
    private void starBtn() {
        binding.starBtn.setOnClickListener(view -> restaurantLiked());
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
                    //ERREUR
                    //java.lang.NullPointerException: Attempt to invoke virtual method 'boolean java.util.ArrayList.isEmpty()' on a null object reference
                    //        at com.example.go4lunch.activities.ui.RestaurantActivity.lambda$restaurantLiked$1$RestaurantActivity(RestaurantActivity.java:160)
                    if(!user.getLike().isEmpty() && user.getLike().contains(placeRestaurantId)) {
                        UserManager.deleteLike(UserManager.getCurrentUser().getUid(), placeRestaurantId);
                        //1µ : remplacement de mStarButton (qui etait lié avec @BindView(R.id.star_btn) Button mStarButton;
                        // par binding.starBtn (star_btn (id de l'xml) sans le _)
                        binding.starBtn.setBackgroundResource(R.color.starButt_transparent);
                    }else{
                        UserManager.updateLike(UserManager.getCurrentUser().getUid(), placeRestaurantId);
                        binding.starBtn.setBackgroundResource(R.color.starButt_yellow);
                    }
                }
            });
        }

    }

    //FLOATING BUTTON
    //1µ : remplacement de mFloatingActionButton (qui etait lié avec @BindView(R.id.floating_act_btn) FloatingActionButton mFloatingActionButton;
    // par binding.floatingActBtn (floating_act_btn (id de l'xml) sans le _)
    private void floatingBtn() {
        binding.floatingActBtn.setOnClickListener( v -> {
            if(v.getId() == R.id.floating_act_btn)
                if (SELECTED.equals(binding.floatingActBtn.getTag())) {
                    selectedRestaurant();
                }else if (binding.floatingActBtn.isSelected()) {
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

        //1µ : remplacement de mFloatingActionButton (qui etait lié avec @BindView(R.id.floating_act_btn) FloatingActionButton mFloatingActionButton;
        // par binding.floatingActBtn (floating_act_btn (id de l'xml) sans le _)
        if(placeDetailsResult != null) {
            UserManager.updateIdOfPlace(Objects.requireNonNull(UserManager.getCurrentUser()).getUid(), placeDetailsResult.getPlaceId(), getCurrentTime());
            binding.floatingActBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fui_ic_check_circle_black_128dp));
            binding.floatingActBtn.setTag(UNSELECTED);
        }
    }

    // REMOVING RESTAURANT CHOICE
    //1µ : remplacement de mFloatingActionButton (qui etait lié avec @BindView(R.id.floating_act_btn) FloatingActionButton mFloatingActionButton;
    // par binding.floatingActBtn (floating_act_btn (id de l'xml) sans le _)
    public void removeRestaurant() {
        UserManager.deleteIdOfPlace(Objects.requireNonNull(Objects.requireNonNull(UserManager.getCurrentUser().getUid())));
        binding.floatingActBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activity_restaurant_valid_done));
        binding.floatingActBtn.setTag(UNSELECTED);
    }

    //1µ : remplacement de mRestaurantRecyclerView (qui etait lié avec @BindView(R.id.restaurant_RV) RecyclerView mRestaurantRecyclerView;
    // par binding.restaurantRV (restaurant_RV (id de l'xml) sans le _)
    private void setUpRV(String placeId) {

        Query query = collectionUsers.whereEqualTo("place id", placeId);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.restaurantAdapter = new RestaurantAdapter(options, Glide.with(this));
        binding.restaurantRV.setHasFixedSize(true);
        binding.restaurantRV.setAdapter(restaurantAdapter);
        binding.restaurantRV.setLayoutManager(new LinearLayoutManager(this));
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
    //1µ : remplacement de mRestoPhoto (qui etait lié avec @BindView(R.id.header_pic_resto) ImageView mRestoPhoto;
    // par binding.headerPicResto (header_pic_resto (id de l'xml) sans le _)
    // meme chose pour mRestoName ( @BindView(R.id.resto_name) TextView mRestoName;) par binding.restoName
    // et mRestoAddress ( @BindView(R.id.resto_address) TextView mRestoAddress;) par binding.restoAddress
    private void updateUI(PlaceDetailsResult placeDetailsResult, RequestManager glide) {
        mGlide=glide;

        //photos with Glide
        if (placeDetailsResult.getPhotos() != null && !placeDetailsResult.getPhotos().isEmpty()) {
            Glide.with(this)
                 .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo_reference=" +
                         placeDetailsResult.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                 .apply(RequestOptions.centerCropTransform())
                 .into(binding.headerPicResto);
        }else{
            binding.headerPicResto.setImageResource(R.drawable.no_pic);
        }
        //RESTAURANT NAME
        binding.restoName.setText(placeDetailsResult.getName());
        //RESTAURANT ADRESS
        binding.restoAddress.setText(placeDetailsResult.getVicinity());
        //RESTAURANT RATING
        restaurantRating(placeDetailsResult);
        //RESTAURANT PHONE
        String formattedPhoneNumber = placeDetailsResult.getFormattedPhoneNumber();
        phoneButton(formattedPhoneNumber);
        //RESTAURANT WEBSITE
        String url = placeDetailsResult.getWebsite();
        webButton(url);
    }

    //1µ : remplacement de mRatingBar (qui etait lié avec @BindView(R.id.rating_bar) RatingBar mRatingBar;
    // par binding.ratingBar (rating_Bar (id de l'xml) sans le _)
    private void restaurantRating(PlaceDetailsResult placeDetailsResult) {
        if (placeDetailsResult.getRating() != null) {
            double restaurantRating = placeDetailsResult.getRating();
            double rating = (restaurantRating/5) *3;
            this.binding.ratingBar.setRating((float) rating);
            this.binding.ratingBar.setVisibility(View.VISIBLE);

        } else {
            this.binding.ratingBar.setVisibility(View.GONE);
        }

    }

    //1µ : remplacement de mPhoneButton (qui etait lié avec @BindView(R.id.phone_btn) Button mPhoneButton;
    // par binding.phoneBtn (phone_btn (id de l'xml) sans le _)
    private void phoneButton(String formattedPhoneNumber) {
        binding.phoneBtn.setOnClickListener(view -> makePhoneCall(formattedPhoneNumber));

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

    //1µ : remplacement de mInternetButton (qui etait lié avec @BindView(R.id.internet_btn) Button mInternetButton;
    // par binding.internetBtn (internet_btn (id de l'xml) sans le _)
    private void webButton(String url) {
        //mInternetButton.setOnClickListener(view -> makeWebView(url));
        binding.internetBtn.setOnClickListener(view -> openWebPage(url));
    }

    /* private void makeWebView(String url) {
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

    //1µ : remplacement de mRelativeLayout (qui etait lié avec @BindView(R.id.restaurant_activity_layout) RelativeLayout mRelativeLayout;
    // par binding.restaurantActivityLayout (restaurant_activity_layout (id de l'xml) sans le _)
    private void showSnackBar (String message) {
        Snackbar.make(binding.restaurantActivityLayout, message, Snackbar.LENGTH_SHORT).show();
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
