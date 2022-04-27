package com.example.go4lunch.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.RestaurantActivity;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.StreamRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.restaurant_user_photo)
    ImageView mRestaurantUserPhoto;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.restaurant_user_name)
    TextView mRestaurantUserName;

    private Disposable mDisposable;
    private String restoName;
    private String idResto;
    private PlaceDetail detail;
    private String userName;


    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        //FOR RESTAURANT SHEET ON CLICK
        itemView.setOnClickListener(v -> {
            if (detail != null) {
                Intent intent = new Intent (v.getContext(), RestaurantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("placeDetailsResult", detail.getResult());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void updateWithDetails(User users, RequestManager glide) {
        //NAME AND ID RESTO FOR REQUEST
        userName = users.getUsername();
        idResto = users.getIdOfPlace();
        Log.d("idRestoUser", "idRestoUsers" + "" + idResto);
        executeHttpRequestWithRetrofit();
        //FOR USER PHOTO
        if (users.getUrlPicture() != null && !users.getUrlPicture().isEmpty()) {
            glide.load(users.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mRestaurantUserPhoto);
        } else {
            mRestaurantUserPhoto.setImageResource(R.drawable.no_pic);
        }

        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();

    }

    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = StreamRepository.streamFetchDetails(idResto)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {

                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                        detail=placeDetail;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErrorWorkPartner", Log.getStackTraceString(e));
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete() {
                        if(idResto !=null) {
                            restoName = detail.getResult().getName();
                            mRestaurantUserName.setText(userName + " " + itemView.getContext().getString(R.string.eat_at) + " " + restoName);
                            Log.d("onCompleteRestoName", "restoName" + idResto);
                        } else{
                            mRestaurantUserName.setText(userName + " " + itemView.getContext().getString(R.string.no_decision));
                            Log.d("RestoName", "noResto" + userName);

                        }

                    }
                });
    }


}
