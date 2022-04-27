package com.example.go4lunch.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsOpeningHoursPeriod;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsResult;
import com.example.go4lunch.utils.UserManager;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Calendar;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.go4lunch.utils.DatesHours.convertStringHours;
import static com.example.go4lunch.utils.DatesHours.getCurrentTime;

public class ListViewHolder extends RecyclerView.ViewHolder {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_name)
    TextView mName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_distance)
    TextView mDistance;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_adress)
    TextView mAdress;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_coworkers)
    TextView mCoworkers;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_open_hours)
    TextView mOpenHours;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_rating)
    RatingBar mRatingBar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.list_photo)
    ImageView mPhoto;

    String GOOGLE_MAP_API_KEY = BuildConfig.API_KEY;
    private final float[] distanceResults = new float[3];
    private final UserManager userManager = UserManager.getInstance();


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        getCurrentTime();
    }

    @SuppressLint("SetTextI18n")
    public void updateWithDetails(PlaceDetailsResult result, RequestManager glide, String mPosition) {

        //RESTAURANT NAME
        this.mName.setText(result.getName());
        //RESTAURANT ADRESS
        this.mAdress.setText(result.getVicinity());
        //RESTAURANT RATING
        restaurantRating(result);
        //RESTAURANT DISTANCE
        restaurantDistance(mPosition, result.getGeometry().getLocation());
        String distance = Math.round(distanceResults[0]) + "m";
        this.mDistance.setText(distance);
        Log.d("TestDistance", distance);
        //FOR NUMBER COWORKERS
        numberCoworkers(result.getPlaceId());
        //FOR OPENING HOURS (OPEN OR CLOSED)
        if (result.getPlaceDetailsOpeningHours() != null) {

            if (result.getPlaceDetailsOpeningHours().getOpenNow().toString().equals("false")) {
                this.mOpenHours.setText("closed");
                this.mOpenHours.setTextColor(Color.RED);
            } else if (result.getPlaceDetailsOpeningHours().getOpenNow().toString().equals("true")) {
                getHoursInfo(result);
            }
        }

        //ADDING PHOTO WITH GLIDE
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.circleCropTransform()).into(mPhoto);
        } else {
            mPhoto.setImageResource(R.drawable.no_pic);
        }

    }

    private void restaurantRating(PlaceDetailsResult result) {
        if(result.getRating() != null) {
            double restaurantRating = result.getRating();
            double rating = (restaurantRating /5) * 3;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);
        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getHoursInfo(PlaceDetailsResult result) {
        int[] days = {0,1,2,3,4,5,6};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) -1;

        if (result.getPlaceDetailsOpeningHours() != null && result.getPlaceDetailsOpeningHours().getPlaceDetailsOpeningHoursPeriods() != null) {
            for (PlaceDetailsOpeningHoursPeriod p : result.getPlaceDetailsOpeningHours().getPlaceDetailsOpeningHoursPeriods()) {
                String closeHour = p.getPeriodClose().getTime();
                Log.d("closeHour", String.valueOf(closeHour));
                int hourClose = Integer.parseInt(closeHour);
                Log.d("hourClose", String.valueOf(hourClose));
                int diff = getCurrentTime() - hourClose;
                Log.d("diff", String.valueOf(diff));

                if (p.getPeriodOpen().getDay() == days[day] && diff <-100) {
                    mOpenHours.setText(itemView.getContext().getString(R.string.open_until) + "" + convertStringHours(closeHour));
                    this.mOpenHours.setTextColor(itemView.getContext().getResources().getColor(R.color.colorOpen));
                    Log.d("Open Until", "Open Until" + " " + convertStringHours(closeHour));


                } else if (diff >= 100 && days[day] == p.getPeriodClose().getDay()) {
                    mOpenHours.setText(itemView.getContext().getString(R.string.closing_soon) + " " + "(" + convertStringHours(closeHour) + ")");
                    this.mOpenHours.setTextColor(itemView.getContext().getResources().getColor(R.color.colorOpen));
                    Log.d("Closing Soon", "closing soon" + convertStringHours(closeHour));

                }
            }
        }

    }

    private void restaurantDistance(String startLocation, com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsLocation endLocation ) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude= Double.parseDouble(separatedStart[0]);
        double startLongitude=Double.parseDouble(separatedStart[1]);
        double endLatitude=endLocation.getLat();
        double endLongitude=endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude,startLongitude, endLatitude,endLongitude,distanceResults);

    }

    private void numberCoworkers(String idOfPlace) {
        userManager.getUsersCollection()
                .whereEqualTo("idOfPlace", idOfPlace)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                            Log.d("numberWorkmates", documentSnapshot.getId() + " " + documentSnapshot.getData());
                        }
                        int numberCoworkers = task.getResult().size();
                        String coworkersNumber = "(" + numberCoworkers + ")";
                        mCoworkers.setText(coworkersNumber);
                    } else {
                        //ERREUR
                        //E/numberMatesError: Error getting documents:
                        //com.google.firebase.firestore.FirebaseFirestoreException: PERMISSION_DENIED: Missing or insufficient permissions.
                        //ERREUR REGLE SI JE CHANGE UNE AUTHORISATION DANS LES REGLES DE FIRESTORE.
                        Log.e("numberMatesError", "Error getting documents: ", task.getException());
                    }
                });
    }


}